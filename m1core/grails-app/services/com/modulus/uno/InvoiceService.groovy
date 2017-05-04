package com.modulus.uno

import grails.util.Environment

class InvoiceService {

  def restService
  def grailsApplication
  def commissionsInvoiceService

  String generateFactura(SaleOrder saleOrder){
    def factura = createInvoiceFromSaleOrder(saleOrder)
    def result = restService.sendFacturaCommandWithAuth(factura, grailsApplication.config.modulus.facturaCreate)
    result.text
  }

  private def createInvoiceFromSaleOrder(SaleOrder saleOrder){
    def datosDeFacturacion = new DatosDeFacturacion(folio: "${saleOrder.id}", metodoDePago: "${saleOrder.paymentMethod}", moneda:saleOrder.currency, tipoDeCambio:saleOrder.changeType?:new BigDecimal(0))
    def emisor = new Contribuyente(datosFiscales:new DatosFiscales())
    def receptor = new Contribuyente(datosFiscales:new DatosFiscales())
    def command = new FacturaCommand(datosDeFacturacion:datosDeFacturacion, emisor:emisor, receptor:receptor)
    Company company = saleOrder.company
    Address address = company.addresses.find { addr -> addr.addressType == AddressType.FISCAL }
    command.emisor.datosFiscales.razonSocial = company.bussinessName
    command.emisor.datosFiscales.rfc = (Environment.current == Environment.PRODUCTION) ? company.rfc : "AAA010101AAA"
    command.emisor.datosFiscales.codigoPostal = address.zipCode
    command.emisor.datosFiscales.pais = address.country
    command.emisor.datosFiscales.ciudad = address.city
    command.emisor.datosFiscales.delegacion = address.federalEntity
    command.emisor.datosFiscales.colonia = address.neighboorhood ?: address.colony
    command.emisor.datosFiscales.calle = address.street
    command.emisor.datosFiscales.noExterior = address.streetNumber
    command.emisor.datosFiscales.noInterior = address.suite ?: "SN"
    command.emisor.datosFiscales.regimen = company.taxRegime.code

    command.emitter = company.rfc
    command.pdfTemplate = saleOrder.pdfTemplate
    command.observaciones = saleOrder.note

    command.receptor.datosFiscales.rfc = saleOrder.rfc
    command.receptor.datosFiscales.razonSocial = saleOrder.clientName
    command.receptor.datosFiscales.pais = saleOrder.addresses[0].country
    command.receptor.datosFiscales.ciudad = saleOrder.addresses[0].city
    command.receptor.datosFiscales.calle = saleOrder.addresses[0].street
    command.receptor.datosFiscales.delegacion = saleOrder.addresses[0].federalEntity
    command.receptor.datosFiscales.codigoPostal = saleOrder.addresses[0].zipCode
    command.receptor.datosFiscales.noExterior = saleOrder.addresses[0].streetNumber ?: "SN"
    command.receptor.datosFiscales.noInterior = saleOrder.addresses[0].suite ?: "SN"
    command.receptor.datosFiscales.colonia = saleOrder.addresses[0].neighboorhood ?: saleOrder.addresses[0].colony

    ClientLink client = ClientLink.findByClientRefAndCompany(saleOrder.rfc, company)

    if (saleOrder.paymentMethod == PaymentMethod.EFECTIVO || saleOrder.paymentMethod == PaymentMethod.CHEQUE_NOMINATIVO) {
      BankAccount bankAccount = company.banksAccounts.find {it.concentradora}
      datosDeFacturacion.numeroDeCuentaDePago = bankAccount ? "${bankAccount.branchNumber} - ${bankAccount.accountNumber} - ${bankAccount.banco}" : company.accounts[0].stpClabe
    } else {
      datosDeFacturacion.numeroDeCuentaDePago = client.stpClabe ?: company.accounts[0].stpClabe
    }

    if(Company.findByRfcAndStatus(command.receptor.datosFiscales.rfc, CompanyStatus.ACCEPTED)){
      command.betweenIntegrated = true
      command.datosDeFacturacion.addendaLabel = "Factura a nombre y cuenta de las empresas, como Emisor ${company.bussinessName} con RFC ${company.rfc} y como Receptor ${receptor.datosFiscales.razonSocial} con RFC ${receptor.datosFiscales.rfc}"
    } else {
      command.datosDeFacturacion.addendaLabel = "Factura a nombre y cuenta de ${company.bussinessName} con RFC ${company.rfc}"
    }

    def conceptos = []
    saleOrder.items.each { item ->
      conceptos.add(new Concepto(cantidad:item.quantity, valorUnitario:item.price, descuento:item.discount, descripcion:item.name, unidad:item.unitType))
    }

    command.conceptos = conceptos

    def impuestos = []
    saleOrder.items.each { item ->
      impuestos.add(new Impuesto(importe:item.quantity * item.priceWithDiscount * item.iva / 100, tasa:item.iva, impuesto:'IVA'))
    }

    command.impuestos = impuestos
    command
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

  String stampCommissionsInvoice(CommissionsInvoice invoice) {
    FacturaCommand factura = createCommandFromCommissionsInvoice(invoice)
    def result = restService.sendFacturaCommandWithAuth(factura, grailsApplication.config.modulus.facturaCreate)
    result.text
  }

  FacturaCommand createCommandFromCommissionsInvoice(CommissionsInvoice invoice) {
     DatosDeFacturacion datosDeFacturacion = new DatosDeFacturacion()
    def emisor = createEmisorForCommissionsInvoice()
    def receptor = createReceptorForCommissionsInvoice(invoice)
    def command = new FacturaCommand(datosDeFacturacion:datosDeFacturacion, emisor:emisor, receptor:receptor)
    command.emitter = emisor.datosFiscales.rfc
    command.pdfTemplate = "template_pdf.tof"
    command.observaciones = ""

    datosDeFacturacion.numeroDeCuentaDePago = grailsApplication.config.m1emitter.stpClabe

    command.conceptos = createConceptsFromCommissionsInvoice(invoice)
    command.impuestos = createTaxesFromConcepts(command.conceptos)
    command
  }

  private Contribuyente createEmisorForCommissionsInvoice() {
    DatosFiscales datosFiscales = new DatosFiscales(
      razonSocial:grailsApplication.config.m1emitter.businessName,
      regimen:"MORAL",
      rfc:grailsApplication.config.m1emitter.rfc,
      pais:grailsApplication.config.m1emitter.address.country,
      calle:grailsApplication.config.m1emitter.address.street,
      noInterior:grailsApplication.config.m1emitter.address.suite ?: "SN",
      noExterior:grailsApplication.config.m1emitter.address.streetNumber,
      ciudad:grailsApplication.config.m1emitter.address.city,
      colonia:grailsApplication.config.m1emitter.address.neighboorhood ?: grailsApplication.config.m1emitter.address.colony,
      delegacion:grailsApplication.config.m1emitter.address.town,
      codigoPostal:grailsApplication.config.m1emitter.address.zipCode
    )
    new Contribuyente(datosFiscales:datosFiscales)
  }

  private Contribuyente createReceptorForCommissionsInvoice(CommissionsInvoice invoice) {
    Company company = invoice.receiver
    Address address = company.addresses.find { addr -> addr.addressType == AddressType.FISCAL }
    DatosFiscales datosFiscales = new DatosFiscales(
      razonSocial:company.bussinessName,
      regimen:company.taxRegime.code,
      rfc:company.rfc,
      pais:address.country,
      calle:address.street,
      noInterior:address.suite ?: "SN",
      noExterior:address.streetNumber,
      ciudad:address.city,
      colonia:address.neighboorhood ?: address.colony,
      delegacion:address.town,
      codigoPostal:address.zipCode
    )
    new Contribuyente(datosFiscales:datosFiscales)
  }

  private List<Concepto> createConceptsFromCommissionsInvoice(invoice) {
    List<Concepto> conceptos = []
    List totalByCommissionType = commissionsInvoiceService.getCommissionsSummaryFromInvoice(invoice)
    totalByCommissionType.each { totalByType ->
      Concepto concepto = new Concepto(
       descripcion:totalByType.type == CommissionType.FIJA ? "Comisión Fija" : "Comisiones de ${totalByType.type}",
       unidad:"SERVICIO",
       valorUnitario:totalByType.total,
       descuento:new BigDecimal(0)
      )
      conceptos.add(concepto)
    }
    conceptos
  }

  private List<Impuesto> createTaxesFromConcepts(List<Concepto> conceptos) {
    List<Impuesto> impuestos = []
    BigDecimal iva = new BigDecimal(grailsApplication.config.iva)
    conceptos.each { concepto ->
      Impuesto impuesto = new Impuesto(
        importe:concepto.cantidad * concepto.valorUnitario * (iva/100),
        tasa:iva,
        impuesto:"IVA"
      )
      impuestos.add(impuesto)
    }
    impuestos
  }

}
