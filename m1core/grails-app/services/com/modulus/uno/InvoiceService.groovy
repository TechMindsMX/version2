package com.modulus.uno

import grails.util.Environment
import com.modulus.uno.invoice.*
import com.modulus.uno.catalogs.UnitType

class InvoiceService {

  def restService
  def grailsApplication

  String generateFactura(SaleOrder saleOrder){
    def factura = createInvoiceFromSaleOrder(saleOrder)
    log.info "Factura command to send: ${factura.dump()}"
    log.info "Datos de Facturación to send: ${factura.datosDeFacturacion.dump()}"
    log.info "Emisor to send: ${factura.emisor.dump()}"
    log.info "Receptor to send: ${factura.receptor.dump()}"
    log.info "Conceptos to send:"
    factura.conceptos.each { concepto->
      log.info "${concepto.dump()}"
    }
    log.info "Totales impuestos:"
    factura.totalesImpuestos.each { ti ->
      log.info "${ti.dump()}" 
    }

    def result = restService.sendFacturaCommandWithAuth(factura, grailsApplication.config.modulus.facturaCreate)
    result.text
  }

  private def createInvoiceFromSaleOrder(SaleOrder saleOrder){
    FacturaCommand facturaCommand = new FacturaCommand(
      datosDeFacturacion: getDatosDeFacturacion(saleOrder), 
      emisor: buildEmitterFromSaleOrder(saleOrder), 
      receptor:buildReceiverFromSaleOrder(saleOrder),
      pdfTemplate: saleOrder.pdfTemplate,
      observaciones: saleOrder.note,
      betweenIntegrated: false,
      conceptos: buildConceptsFromSaleOrder(saleOrder)
    )
    facturaCommand.emitter = facturaCommand.emisor.datosFiscales.rfc
    facturaCommand.totalesImpuestos = buildSummaryTaxes(facturaCommand)
    facturaCommand
  }

  private DatosDeFacturacion getDatosDeFacturacion(SaleOrder saleOrder) {
    Company company = saleOrder.company
    ClientLink client = ClientLink.findByClientRefAndCompany(saleOrder.rfc, company)
    String accountNumber = ""
    if (saleOrder.paymentWay == PaymentWay.EFECTIVO || saleOrder.paymentWay == PaymentWay.CHEQUE_NOMINATIVO) {
      BankAccount bankAccount = company.banksAccounts.find {it.concentradora}
      accountNumber = bankAccount ? "${bankAccount.branchNumber} - ${bankAccount.accountNumber} - ${bankAccount.banco}" : company.accounts[0].stpClabe
    } else {
      accountNumber = client?.stpClabe ?: company.accounts[0].stpClabe
    }

    new DatosDeFacturacion(
      metodoDePago: new MetodoDePago(clave:"PUE", descripcion:"Pago en una sóla exhibición"),
      formaDePago: new FormaDePago(clave:saleOrder.paymentWay.getKey(), descripcion:saleOrder.paymentWay.getDescription()),
      moneda: saleOrder.currency,
      tipoDeCambio: saleOrder.changeType?:new BigDecimal(1),
      numeroDeCuentaDePago: accountNumber,
      addendaLabel: "Factura a nombre y cuenta de ${company.bussinessName} con RFC ${company.rfc}"
    )
  }

  private Contribuyente buildEmitterFromSaleOrder(SaleOrder saleOrder) {
    Company company = saleOrder.company
    Address address = company.addresses.find { addr -> addr.addressType == AddressType.FISCAL }
    new Contribuyente(
      datosFiscales: new DatosFiscales(
        razonSocial: company.bussinessName,
        rfc: (Environment.current == Environment.PRODUCTION) ? company.rfc : "AAA010101AAA",
        codigoPostal: address.zipCode,
        pais: address.country,
        ciudad: address.city,
        delegacion: address.federalEntity,
        colonia: address.neighboorhood ?: address.colony,
        calle: address.street,
        noExterior: address.streetNumber,
        noInterior: address.suite ?: "SN",
        regimen: new RegimenFiscal(clave:company.taxRegime.key, descripcion:company.taxRegime.description)
      )
    )
  }

  private Contribuyente buildReceiverFromSaleOrder(SaleOrder saleOrder) {
    //TODO: Implementar catálogo usos cfdi para selección al ejecutar la orden de venta
    new Contribuyente(
      datosFiscales: new DatosFiscales(
        rfc: (Environment.current == Environment.PRODUCTION) ? saleOrder.rfc : "LAN7008173R5",
        razonSocial: saleOrder.clientName,
        pais: saleOrder.addresses[0].country,
        ciudad: saleOrder.addresses[0].city,
        calle: saleOrder.addresses[0].street,
        delegacion: saleOrder.addresses[0].federalEntity,
        codigoPostal: saleOrder.addresses[0].zipCode,
        noExterior: saleOrder.addresses[0].streetNumber ?: "SN",
        noInterior: saleOrder.addresses[0].suite ?: "SN",
        colonia: saleOrder.addresses[0].neighboorhood ?: saleOrder.addresses[0].colony,
        usoCFDI: "P01" 
      )
    )
  }

  private List<Concepto> buildConceptsFromSaleOrder(SaleOrder saleOrder) {
    def conceptos = []
    saleOrder.items.toList().sort{it.name}.each { item ->
      Concepto concepto = new Concepto(
        cantidad:item.quantity, 
        valorUnitario:item.price, 
        descuento:item.discount, 
        claveProd:item.satKey ?: "01010101",
        descripcion:item.name, 
        unidad:item.unitType,
        claveUnidad:getUnitKeyFromItem(item),
        impuestos:buildTaxesFromItem(item),
        retenciones:buildTaxWithholdingsFromItem(item)
      )
      conceptos.add(concepto)
    }
    conceptos
  }

  private String getUnitKeyFromItem(SaleOrderItem item) {
    UnitType unitType = UnitType.findByCompanyAndName(item.saleOrder.company, item.unitType)
    unitType ? unitType.unitKey : "XNA"
  }

  private List<Impuesto> buildTaxesFromItem(SaleOrderItem item) {
    List<Impuesto> taxes = []
    
    if (item.iva){
      taxes.add(new Impuesto(base:item.quantity * item.priceWithDiscount, importe:item.quantity * item.priceWithDiscount * item.iva / 100, tasa:item.iva/100, impuesto:'002', tipoFactor:"Tasa"))
    }

    taxes
  }

  private List<Impuesto> buildTaxWithholdingsFromItem(SaleOrderItem item) {
    List<Impuesto> holdings = []
    
    if (item.ivaRetention){
      holdings.add(new Impuesto(base:item.price, importe:item.ivaRetention, tasa:item.ivaRetention/item.price, impuesto:'002', tipoFactor:"Tasa"))
    }

    holdings
  }

  private TotalesImpuestos buildSummaryTaxes(FacturaCommand facturaCommand) {
    new TotalesImpuestos(
      totalImpuestosTrasladados: calculateTaxesTotal(facturaCommand),
      totalImpuestosRetenidos: calculateHoldingsTotal(facturaCommand),
      impuestos: buildSummaryForTaxes(facturaCommand),
      retenciones: buildSummaryForHoldings(facturaCommand)
    ) 
  }

  private BigDecimal calculateTaxesTotal(FacturaCommand facturaCommand) {
    BigDecimal total = 0
    facturaCommand.conceptos.each { concepto -> 
      total += concepto.impuestos*.importe.sum() ?: 0
    }
    total
  }

  private BigDecimal calculateHoldingsTotal(FacturaCommand facturaCommand) {
    BigDecimal total = 0
    facturaCommand.conceptos.each { concepto -> 
      total += concepto.retenciones*.importe.sum() ?: 0
    }
    total
  }

  private List<Impuesto> buildSummaryForTaxes(FacturaCommand facturaCommand) {
    List<Impuesto> summary = []
    def allTaxes = facturaCommand.conceptos.impuestos.flatten()
    def summaryTaxes = allTaxes.groupBy{ [impuesto:it.impuesto, tasa:it.tasa, tipoFactor:it.tipoFactor] }.collect { k, v ->
      [impuesto:k.impuesto, tasa:k.tasa, tipoFactor:k.tipoFactor, importe:v.collect { it.importe}.sum()]
    }
    summaryTaxes.each {
      summary.add(new Impuesto(importe:it.importe, tasa:it.tasa, impuesto:it.impuesto, tipoFactor:it.tipoFactor))
    }
    summary
  }

  private List<Impuesto> buildSummaryForHoldings(FacturaCommand facturaCommand) {
    List<Impuesto> summary = []
    def allTaxes = facturaCommand.conceptos.retenciones.flatten()
    def summaryTaxes = allTaxes.groupBy{ [impuesto:it.impuesto, tasa:it.tasa, tipoFactor:it.tipoFactor] }.collect { k, v ->
      [impuesto:k.impuesto, tasa:k.tasa, tipoFactor:k.tipoFactor, importe:v.collect { it.importe}.sum()]
    }
    summaryTaxes.each {
      summary.add(new Impuesto(importe:it.importe, tasa:it.tasa, impuesto:it.impuesto, tipoFactor:it.tipoFactor))
    }
    summary
  }

  def generatePreviewFactura(SaleOrder saleOrder){
    def factura = createInvoiceFromSaleOrder(saleOrder)
    log.info "Factura to preview: ${factura.dump()}"
    String file = "previo.pdf"
    String rfc = "${saleOrder.company.rfc}"
    def url = grailsApplication.config.modulus.showFactura
    url = url.replace('#rfc',rfc).replace('#file',file)
    def result = restService.sendFacturaCommandWithAuth(factura, url)
    log.info "Preview invoice generated for sale order ${saleOrder.id} with template ${saleOrder.pdfTemplate}"
    result.data
  }

  void cancelBill(SaleOrder saleOrder) {
    CancelBillCommand cancelCommand = new CancelBillCommand(uuid:"${saleOrder.folio.length()>36 ? saleOrder.folio.substring(0,36) : saleOrder.folio}", rfc:"${saleOrder.company.rfc}")
    def result = restService.sendFacturaCommandWithAuth(cancelCommand, grailsApplication.config.modulus.cancelFactura)
    if (!result) {
      throw new RestException("No se pudo realizar la cancelación, intente más tarde")
    }
  }

  void changeSerieAndInitialFolioToStampInvoiceForEmitter(Map params) {
    restService.updateSerieForEmitter(params)
  }

}
