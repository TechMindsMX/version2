package com.modulus.uno.saleorder

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import com.modulus.uno.catalogs.UnitType
import com.modulus.uno.Company
import com.modulus.uno.ModulusUnoAccount
import com.modulus.uno.ClientLink
import com.modulus.uno.BankAccount
import com.modulus.uno.Bank
import com.modulus.uno.CommissionTransaction
import com.modulus.uno.PaymentWay
import com.modulus.uno.PaymentMethod
import com.modulus.uno.AddressType
import com.modulus.uno.InvoicePurpose
import com.modulus.uno.Address
import com.modulus.uno.RestService
import com.modulus.uno.RestException

@TestFor(InvoiceService)
@Mock([SaleOrder, SaleOrderItem, Company, ModulusUnoAccount, ClientLink, CommissionTransaction, BankAccount, Bank, UnitType])
class InvoiceServiceSpec extends Specification {

  GrailsApplicationMock grailsApplication = new GrailsApplicationMock()

  def restService = Mock(RestService)

  def setup(){
    service.grailsApplication = grailsApplication
    service.restService = restService
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
      Company company = new Company(rfc:'AAA010101AAA', bussinessName:'Integradora de Emprendimientos Culturales S.A. de C.V.', employeeNumbers:10, grossAnnualBilling:100000, addresses:[address],accounts:[account]).save(validate:false)
    and:"Sale Order item"
      UnitType unitType = new UnitType(company:company, name:"UNIDADES", unitKey:"01").save(validate:false)
      SaleOrderItem saleOrderItem = new SaleOrderItem(sku:'sku1',name:'name', price:100, discount:0, ivaRetention:0, iva:16, quantity:2, unitType:"UNIDADES")
    and:"An sale order"
      def saleOrder = new SaleOrder(rfc:'XXXX010101XXX',clientName:'clientName',addresses:[address], items:[saleOrderItem], company:company, paymentWay: PaymentWay.TRANSFERENCIA_ELECTRONICA, currency:"MXN", changeType:new BigDecimal(1), paymentMethod:PaymentMethod.PPD, invoicePurpose:InvoicePurpose.G01).save(validate:false)
    and:"A client"
      ClientLink client = new ClientLink(clientRef:'XXXX010101XXX', company:company).save(validate:false)
      ClientLink.metaClass.static.findByClientRefAndCompany = { client }
    when:"We create an invoice from sale order"
      def result = service.createInvoiceFromSaleOrder(saleOrder)
    then:"We expect factura data"
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
      result.receptor.datosFiscales.usoCFDI == "G01"

      result.datosDeFacturacion.metodoDePago.clave == "PPD"
      result.datosDeFacturacion.formaDePago.clave == "03"

      result.conceptos.size() == 1

      result.conceptos[0].cantidad == 2
      result.conceptos[0].valorUnitario == 100
      result.conceptos[0].descripcion == 'name'
      result.conceptos[0].unidad == 'UNIDADES'
      result.conceptos[0].claveUnidad == '01'

      result.conceptos[0].impuestos[0].importe == 32
      result.conceptos[0].impuestos[0].tasa == 0.160000
      result.conceptos[0].impuestos[0].impuesto == '002'
      result.conceptos[0].impuestos[0].base == 200
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
      Company company = new Company(rfc:'AAA010101AAA', bussinessName:'Integradora de Emprendimientos Culturales S.A. de C.V.', employeeNumbers:10, grossAnnualBilling:100000, addresses:[address],accounts:[account], banksAccounts:[bankAccount]).save(validate:false)
    and:"Sale Order item"
      UnitType unitType = new UnitType(company:company, name:"UNIDADES", unitKey:"01").save(validate:false)
      SaleOrderItem saleOrderItem = new SaleOrderItem(sku:'sku1',name:'name', price:100, discount:0, ivaRetention:0, iva:16, quantity:2, unitType:"UNIDADES")
    and:"An sale order"
      def saleOrder = new SaleOrder(rfc:'XXXX010101XXX',clientName:'clientName',addresses:[address], items:[saleOrderItem], company:company, paymentWay: PaymentWay.EFECTIVO, currency:"MXN", changeType:new BigDecimal(0)).save(validate:false)
    and:"A client"
      ClientLink client = new ClientLink(clientRef:'XXXX010101XXX', stpClabe:"999988887777666655", company:company).save(validate:false)
      ClientLink.metaClass.static.findByClientRefAndCompany = { client }
    when:"We create an invoice from sale order"
      def result = service.createInvoiceFromSaleOrder(saleOrder)
    then:"We expect factura data"
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

      result.conceptos[0].cantidad == 2
      result.conceptos[0].valorUnitario == 100
      result.conceptos[0].descripcion == 'name'
      result.conceptos[0].unidad == 'UNIDADES'
      result.conceptos[0].claveUnidad == '01'

      result.conceptos[0].impuestos[0].importe == 32
      result.conceptos[0].impuestos[0].tasa == 0.160000
      result.conceptos[0].impuestos[0].impuesto == '002'
      result.conceptos[0].impuestos[0].base == 200
  }

  def "Should throw a exception when cancel a sale order invoiced and service return a fail"() {
    given:
      Company company = new Company(rfc:"RODS861224HNE").save(validate:false)
    and:"A Sale order to cancel"
      SaleOrder saleOrder = new SaleOrder(uuid:'uuid',company:company, folio:'folioSat').save(validate:false)
    when:
      def result = service.cancelBill(saleOrder)
    then:
      result == null
  }

}
