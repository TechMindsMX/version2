package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(InvoiceService)
@Mock([SaleOrder, SaleOrderItem, Company, ModulusUnoAccount, ClientLink])
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
      Company company = new Company(rfc:'AAD990814BP7', bussinessName:'Integradora de Emprendimientos Culturales S.A. de C.V.', employeeNumbers:10, grossAnnualBilling:100000, addresses:[address],accounts:[account]).save(validate:false)
    and:"Sale Order item"
      SaleOrderItem saleOrderItem = new SaleOrderItem(sku:'sku1',name:'name', price:100, ieps:0, iva:16, quantity:2, unitType:UnitType.UNIDADES)
    and:"An sale order"
      def saleOrder = new SaleOrder(rfc:'XXXX010101XXX',clientName:'clientName',addresses:[address], items:[saleOrderItem], company:company, paymentMethod: PaymentMethod.TRANSFERENCIA_ELECTRONICA).save(validate:false)
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
      result.emisor.datosFiscales.delegacion == 'Miguel Hidalgo'
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
    and:"A company"
      Company company = new Company(rfc:'AAD990814BP7', bussinessName:'Integradora de Emprendimientos Culturales S.A. de C.V.', employeeNumbers:10, grossAnnualBilling:100000, addresses:[address],accounts:[account]).save(validate:false)
    and:"Sale Order item"
      SaleOrderItem saleOrderItem = new SaleOrderItem(sku:'sku1',name:'name', price:100, ieps:0, iva:16, quantity:2, unitType:UnitType.UNIDADES)
    and:"An sale order"
      def saleOrder = new SaleOrder(rfc:'XXXX010101XXX',clientName:'clientName',addresses:[address], items:[saleOrderItem], company:company, paymentMethod: PaymentMethod.EFECTIVO).save(validate:false)
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
      result.datosDeFacturacion.numeroDeCuentaDePago == '999988887777666655'
      result.datosDeFacturacion.moneda == 'MXN'

      result.emisor.datosFiscales.razonSocial == 'Integradora de Emprendimientos Culturales S.A. de C.V.'
      result.emisor.datosFiscales.rfc == 'AAA010101AAA'
      result.emisor.datosFiscales.codigoPostal == '11850'
      result.emisor.datosFiscales.pais == 'México'
      result.emisor.datosFiscales.ciudad == 'Ciudad de México'
      result.emisor.datosFiscales.delegacion == 'Miguel Hidalgo'
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

  def "Should cancel billing"() {
    given:
      Company company = new Company(rfc:"RODS861224HNE").save(validate:false)
    and:"A Sale order to cancel"
      SaleOrder saleOrder = new SaleOrder(uuid:'uuid',company:company).save(validate:false)
    when:
      service.cancelBill(saleOrder)
    then:
      1 * restService.sendFacturaCommandWithAuth(_,_)
  }
}
