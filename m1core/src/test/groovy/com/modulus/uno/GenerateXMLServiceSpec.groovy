package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(GenerateXMLService)
class GenerateXMLServiceSpec extends Specification {

  void "Generate xml of order sale for request to stp"() {
    given:""
      def data = [
        institucionContraparte: "40002",
        empresa: "TECHMINDS",
        fechaDeOperacion: "20170306",  
        folioOrigen: "",
        claveDeRastreo: "1488820184033",
        institucionOperante: "90646",
        montoDelPago: "250.00",
        tipoDelPago: "1",
        tipoDeLaCuentaDelOrdenante: "",
        nombreDelOrdenante: "TECHMINDS",
        cuentaDelOrdenante: "",
        rfcCurpDelOrdenante: "",
        tipoDeCuentaDelBeneficiario: "40",
        nombreDelBeneficiario: "Provider Soft Temoc Uno",
        cuentaDelBeneficiario: "002180000201612076",
        rfcCurpDelBeneficiario: "NA",
        emailDelBeneficiario: "mailBeneficiary@mail.com",
        tipoDeCuentaDelBeneficiario2: "",
        nombreDelBeneficiario2: "",
        cuentaDelBeneficiario2: "",
        rfcCurpDelBeneficiario2: "",
        conceptoDelPago: "PAGO A PROVEEDOR",
        conceptoDelPago2: "",
        claveDelCatalogoDeUsuario1: "",
        claveDelCatalogoDeUsuario2: "",
        claveDelPago: "",
        referenciaDeCobranza: "",
        referenciaNumerica: "1170306",
        tipoDeOperación: "",
        topologia: "",
        usuario: "",
        medioDeEntrega: "",
        prioridad: "",
        iva: ""
      ]     
    when:""
      def xml = service.xmlOrderSaleRequest(data)
    then:""
      xml.replaceAll("\n", "").replaceAll(" ", "") == """
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:h2h="http://h2h.integration.spei.enlacefi.lgec.com/">
          <soapenv:Header/>
          <soapenv:Body>
             <h2h:registraOrden>
                <ordenPago>
                   <fechaOperacion>20170306</fechaOperacion>
                   <institucionOperante>90646</institucionOperante>
                   <institucionContraparte>40002</institucionContraparte>
                   <claveRastreo>1488820184033</claveRastreo>
                   <monto>250.00</monto>
                   <tipoPago>1</tipoPago>
                   <empresa>TECHMINDS</empresa>
                   <nombreOrdenante>TECHMINDS</nombreOrdenante>
                   <nombreBeneficiario>Provider Soft Temoc Uno</nombreBeneficiario>
                   <tipoCuentaBeneficiario>40</tipoCuentaBeneficiario>
                   <cuentaBeneficiario>002180000201612076</cuentaBeneficiario>
                   <rfcCurpBeneficiario>NA</rfcCurpBeneficiario>
                   <conceptoPago>PAGO A PROVEEDOR</conceptoPago>
                   <referenciaNumerica>1170306</referenciaNumerica>
                   <firma></firma>
                   <emailBeneficiario>mailBeneficiary@mail.com</emailBeneficiario>
               </ordenPago>
           </h2h:registraOrden>
          </soapenv:Body>
        </soapenv:Envelope>
      """.replaceAll("\n", "").replaceAll(" ", "")
  }

}
