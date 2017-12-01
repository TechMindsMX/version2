package com.modulus.uno

import grails.util.Environment
import com.modulus.uno.invoice.*

class InvoiceService {

  def restService
  def grailsApplication

  String generateFactura(SaleOrder saleOrder){
    def factura = createInvoiceFromSaleOrder(saleOrder)
    def result = restService.sendFacturaCommandWithAuth(factura, grailsApplication.config.modulus.facturaCreate)
    result.text
  }

  private def createInvoiceFromSaleOrder(SaleOrder saleOrder){
    FacturaCommand facturaCommand = new FacturaCommand(
      datosDeFacturacion: getDatosDeFacturacion(saleOrder), 
      emisor: buildEmitterFromSaleOrder(saleOrder), 
      receptor:buildReceiverFromSaleOrder(saleOrder),
      emitter: this.emisor.rfc,
      pdfTemplate: saleOrder.pdfTemplate,
      observaciones: saleOrder.note,
      betweenIntegrated: false,
      conceptos: buildConceptsFromSaleOrder(saleOrder),
    )
    facturaCommand.totalesImpuestos = buildSummaryTaxes(facturaCommand)
    facturaCommand
  }

  private DatosDeFacturaction getDatosDeFacturacion(SaleOrder saleOrder) {
    Company company = saleOrder.company
    ClientLink client = ClientLink.findByClientRefAndCompany(saleOrder.rfc, company)
    String accountNumber = ""
    if (saleOrder.paymentMethod == PaymentMethod.EFECTIVO || saleOrder.paymentMethod == PaymentMethod.CHEQUE_NOMINATIVO) {
      BankAccount bankAccount = company.banksAccounts.find {it.concentradora}
      accountNumber = bankAccount ? "${bankAccount.branchNumber} - ${bankAccount.accountNumber} - ${bankAccount.banco}" : company.accounts[0].stpClabe
    } else {
      accountNumber = client?.stpClabe ?: company.accounts[0].stpClabe
    }

    new DatosDeFacturacion(
      metodoDePago: new MetodoDePago(clave:saleOrder.paymentMethod.getKey(), descripcion:saleOrder.paymentMethod.getDescription()),
      moneda: saleOrder.currency,
      tipoDeCambio: saleOrder.changeType?:new BigDecimal(0),
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
        regimen: company.taxRegime.code
      )
    )
  }

  private Contribuyente buildReceiverFromSaleOrder(SaleOrder saleOrder) {
    new Contribuyente(
      datosFiscales: new DatosFiscales(
        rfc: saleOrder.rfc,
        razonSocial: saleOrder.clientName,
        pais: saleOrder.addresses[0].country,
        ciudad: saleOrder.addresses[0].city,
        calle: saleOrder.addresses[0].street,
        delegacion: saleOrder.addresses[0].federalEntity,
        codigoPostal: saleOrder.addresses[0].zipCode,
        noExterior: saleOrder.addresses[0].streetNumber ?: "SN",
        noInterior: saleOrder.addresses[0].suite ?: "SN",
        colonia: saleOrder.addresses[0].neighboorhood ?: saleOrder.addresses[0].colony
      )
    )
  }

  private List<Concepto> buildConceptsFromSaleOrder(SaleOrder saleOrder) {
    def conceptos = []
    saleOrder.items.each { item ->
      Concepto concepto = new Concepto(
        cantidad:item.quantity, 
        valorUnitario:item.price, 
        descuento:item.discount, 
        descripcion:item.name, 
        unidad:item.unitType,
        claveUnidad:getUnitKeyFromItem(item),
        impuestos:buildTaxesFromItem(item),
        retenciones:buildTaxWithholdingsFromItem(item)
      )
      conceptos.add(concepto)
    }
  }

  private String getUnitKeyFromItem(SaleOrderItem item) {
    UnitType unitType = UnitType.findByCompanyAndName(item.saleOrder.company, item.unitType)
    unitType ? unitType.unitKey : "XNA"
  }

  private List<Impuesto> buildTaxesFromItem(SaleOrderItem item) {
    List<Impuesto> taxes = []
    
    if (item.iva){
      taxes.add(new Impuesto(base:item.price, importe:item.quantity * item.priceWithDiscount * item.iva / 100, tasa:item.iva/100, impuesto:'002', tipoFactor:"Tasa"))
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
      impuestos: buildSummaryForTaxes(facturaCommand)
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
    def allTaxes = factura.conceptos.impuestos.flatten()
    def typeTaxes = allTaxes.impuesto.unique().sort()
    typeTaxes.each { type ->
      def totalType = allTaxes.findAll { it.impuesto == type }.importe.sum()
        summary.add(new Impuesto(impuesto:type, importe:totalType, tipoFactor:"Tasa", tasa:))
    }
  }

  def generatePreviewFactura(SaleOrder saleOrder){
    def factura = createInvoiceFromSaleOrder(saleOrder)
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
