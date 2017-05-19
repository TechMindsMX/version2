package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(InvoiceService)
@Mock([SaleOrder, SaleOrderItem, Company, ModulusUnoAccount, ClientLink, CommissionsInvoice, CommissionTransaction, BankAccount, Bank])
class InvoiceServiceSpec extends Specification {

  GrailsApplicationMock grailsApplication = new GrailsApplicationMock()

  def restService = Mock(RestService)
  CommissionsInvoiceService commissionsInvoiceService = Mock(CommissionsInvoiceService)

  def setup(){
    service.grailsApplication = grailsApplication
    service.restService = restService
    service.commissionsInvoiceService = commissionsInvoiceService
  }

  void "create an invoice from sale order"(){
    given:"A address"
      def address = new Address(street:"Tiburcio Montiel",
                                streetNumber:"266",
                                suite:"B3",
                                zipCode:"11850",
                                colony:"Reforma",
                                town:"Miguel Hidalgo",
                                city:"Ciudad de México",
                                country:"México",
                                federalEntity:"México",
                                addressType:AddressType.FISCAL)
    and:"A modulusuno account"
      ModulusUnoAccount account = new ModulusUnoAccount(
        account:'account',
        balance:'0.00',
        integraUuid:'integraUuid',
        stpClabe:'1234567890',
        timoneUuid:'timoneUuid'
      ).save(validate:false)
    and:"A company"
      Company company = new Company(rfc:'AAD990814BP7', bussinessName:'Integradora de Emprendimientos Culturales S.A. de C.V.', employeeNumbers:10, grossAnnualBilling:100000, addresses:[address],accounts:[account]).save(validate:false)
    and:"Sale Order item"
      SaleOrderItem saleOrderItem = new SaleOrderItem(sku:'sku1',name:'name', price:100, ieps:0, iva:16, quantity:2, unitType:UnitType.UNIDADES)
    and:"An sale order"
      def saleOrder = new SaleOrder(rfc:'XXXX010101XXX',clientName:'clientName',addresses:[address], items:[saleOrderItem], company:company, paymentMethod: PaymentMethod.TRANSFERENCIA_ELECTRONICA, currency:"MXN", changeType:new BigDecimal(1)).save(validate:false)
    and:"A client"
      ClientLink client = new ClientLink(clientRef:'XXXX010101XXX', company:company).save(validate:false)
      ClientLink.metaClass.static.findByClientRefAndCompany = { client }
    when:"We create an invoice from sale order"
      def result = service.createInvoiceFromSaleOrder(saleOrder)
    then:"We expect factura data"
      result.datosDeFacturacion.folio
      result.datosDeFacturacion.formaDePago == 'PAGO EN UNA SOLA EXHIBICION'
      result.datosDeFacturacion.tipoDeComprobante == 'ingreso'
      result.datosDeFacturacion.lugarDeExpedicion == 'CIUDAD DE MEXICO'
      result.datosDeFacturacion.metodoDePago == '03 - TRANSFERENCIA ELECTRONICA'
      result.datosDeFacturacion.numeroDeCuentaDePago == '1234567890'
      result.datosDeFacturacion.moneda == 'MXN'

      result.emisor.datosFiscales.razonSocial == 'Integradora de Emprendimientos Culturales S.A. de C.V.'
      result.emisor.datosFiscales.rfc == 'AAA010101AAA'
      result.emisor.datosFiscales.codigoPostal == '11850'
      result.emisor.datosFiscales.pais == 'México'
      result.emisor.datosFiscales.ciudad == 'Ciudad de México'
      result.emisor.datosFiscales.delegacion == 'México'
      result.emisor.datosFiscales.colonia == 'Reforma'
      result.emisor.datosFiscales.calle == 'Tiburcio Montiel'
      result.emisor.datosFiscales.noExterior == '266'
      result.emisor.datosFiscales.noInterior == 'B3'
      result.receptor.datosFiscales.noExterior == '266'
      result.receptor.datosFiscales.colonia == 'Reforma'

      result.conceptos.size() == 1
      result.impuestos.size() == 1

      result.conceptos[0].cantidad == 2
      result.conceptos[0].valorUnitario == 100
      result.conceptos[0].descripcion == 'name'
      result.conceptos[0].unidad == 'UNIDADES'

      result.impuestos[0].importe == 32
      result.impuestos[0].tasa == 16
      result.impuestos[0].impuesto == 'IVA'
  }

  void "create an invoice from sale order for client with stpClabe"(){
    given:"A address"
      def address = new Address(street:"Tiburcio Montiel",
                                streetNumber:"266",
                                suite:"B3",
                                zipCode:"11850",
                                colony:"Reforma",
                                town:"Miguel Hidalgo",
                                city:"Ciudad de México",
                                country:"México",
                                federalEntity:"México",
                                addressType:AddressType.FISCAL)
    and:"A modulusuno account"
      ModulusUnoAccount account = new ModulusUnoAccount(
        account:'account',
        balance:'0.00',
        integraUuid:'integraUuid',
        stpClabe:'1234567890',
        timoneUuid:'timoneUuid'
      ).save(validate:false)
    and:"A bank account"
      Bank bank = new Bank(name:"BANCO").save(validate:false)
      BankAccount bankAccount = new BankAccount(accountNumber:"2233445566", branchNumber:"999", banco:bank, concentradora:true).save(validate:false)
    and:"A company"
      Company company = new Company(rfc:'AAD990814BP7', bussinessName:'Integradora de Emprendimientos Culturales S.A. de C.V.', employeeNumbers:10, grossAnnualBilling:100000, addresses:[address],accounts:[account], banksAccounts:[bankAccount]).save(validate:false)
    and:"Sale Order item"
      SaleOrderItem saleOrderItem = new SaleOrderItem(sku:'sku1',name:'name', price:100, ieps:0, iva:16, quantity:2, unitType:UnitType.UNIDADES)
    and:"An sale order"
      def saleOrder = new SaleOrder(rfc:'XXXX010101XXX',clientName:'clientName',addresses:[address], items:[saleOrderItem], company:company, paymentMethod: PaymentMethod.EFECTIVO, currency:"MXN", changeType:new BigDecimal(0)).save(validate:false)
    and:"A client"
      ClientLink client = new ClientLink(clientRef:'XXXX010101XXX', stpClabe:"999988887777666655", company:company).save(validate:false)
      ClientLink.metaClass.static.findByClientRefAndCompany = { client }
    when:"We create an invoice from sale order"
      def result = service.createInvoiceFromSaleOrder(saleOrder)
    then:"We expect factura data"
      result.datosDeFacturacion.folio
      result.datosDeFacturacion.formaDePago == 'PAGO EN UNA SOLA EXHIBICION'
      result.datosDeFacturacion.tipoDeComprobante == 'ingreso'
      result.datosDeFacturacion.lugarDeExpedicion == 'CIUDAD DE MEXICO'
      result.datosDeFacturacion.metodoDePago == '01 - EFECTIVO'
      result.datosDeFacturacion.numeroDeCuentaDePago == '999 - 2233445566 - BANCO'
      result.datosDeFacturacion.moneda == 'MXN'

      result.emisor.datosFiscales.razonSocial == 'Integradora de Emprendimientos Culturales S.A. de C.V.'
      result.emisor.datosFiscales.rfc == 'AAA010101AAA'
      result.emisor.datosFiscales.codigoPostal == '11850'
      result.emisor.datosFiscales.pais == 'México'
      result.emisor.datosFiscales.ciudad == 'Ciudad de México'
      result.emisor.datosFiscales.delegacion == 'México'
      result.emisor.datosFiscales.colonia == 'Reforma'
      result.emisor.datosFiscales.calle == 'Tiburcio Montiel'
      result.emisor.datosFiscales.noExterior == '266'
      result.emisor.datosFiscales.noInterior == 'B3'
      result.receptor.datosFiscales.noExterior == '266'
      result.receptor.datosFiscales.colonia == 'Reforma'

      result.conceptos.size() == 1
      result.impuestos.size() == 1

      result.conceptos[0].cantidad == 2
      result.conceptos[0].valorUnitario == 100
      result.conceptos[0].descripcion == 'name'
      result.conceptos[0].unidad == 'UNIDADES'

      result.impuestos[0].importe == 32
      result.impuestos[0].tasa == 16
      result.impuestos[0].impuesto == 'IVA'
  }

  def "Should throw a exception when cancel a sale order invoiced and service return a fail"() {
    given:
      Company company = new Company(rfc:"RODS861224HNE").save(validate:false)
    and:"A Sale order to cancel"
      SaleOrder saleOrder = new SaleOrder(uuid:'uuid',company:company, folio:'folioSat').save(validate:false)
    when:
      service.cancelBill(saleOrder)
    then:
      thrown RestException
  }

  void "Should create command to stamp a commissions invoice"() {
    given:"A commissions invoice"
      Address address = new Address(street:"Tiburcio Montiel",
                                streetNumber:"266",
                                suite:"B3",
                                zipCode:"11850",
                                colony:"Reforma",
                                town:"Miguel Hidalgo",
                                city:"Ciudad de México",
                                country:"México",
                                federalEntity:"México",
                                addressType:AddressType.FISCAL)
      Company receiver = new Company(rfc:"XXX010101AAA", addresses:[address]).save(validate:false)
      CommissionTransaction fixed = new CommissionTransaction(type:CommissionType.FIJA, amount:new BigDecimal(1000), company:receiver).save(validate:false)
      CommissionTransaction payments = new CommissionTransaction(type:CommissionType.PAGO, amount:new BigDecimal(100), company:receiver).save(validate:false)
      CommissionsInvoice invoice = new CommissionsInvoice(receiver:receiver, status:CommissionsInvoiceStatus.CREATED).save(validate:false)
      invoice.addToCommissions(fixed)
      invoice.addToCommissions(payments)
      invoice.save(validate:false)
    and:
      commissionsInvoiceService.getCommissionsSummaryFromInvoice(_) >> [[type:CommissionType.FIJA, total:1000.00],[type:CommissionType.PAGO, total:100.00]]
    when:
      def command = service.createCommandFromCommissionsInvoice(invoice)
    then:
      command.emitter == "AAA010101AAA"
      command.emisor.datosFiscales.rfc == "AAA010101AAA"
      command.receptor.datosFiscales.rfc == "XXX010101AAA"
      command.conceptos.size() == 2
      command.impuestos.size() == 2
  }
}
