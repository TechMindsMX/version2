package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(GenerateXMLService)
class GenerateXMLServiceSpec extends Specification {

  void "Generate xml for pay order for request to stp"() {
    given:""
      def data = [
        institucionContraparte: "40002",
        empresa: "TECHMINDS",
        fechaDeOperacion: "20170306",
        folioOrigen: "",
        claveDeRastreo: "1488820184033",
        institucionOperante: "90646",
        montoDelPago: 250.00,
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
        tipoDeOperacion: "",
        topologia: "",
        usuario: "",
        medioDeEntrega: "",
        prioridad: "",
        iva: "",
        firma: "Yik10Fo+yReNXvSfeYvbIlthR2e05PETN+4WXqnOsfStHBYTo/QCRsDJCsCgaNOLfGArByCWGwDA9Lx5htWRB0KKnyZVIDE1qBrsjfb7MEY+sqCqiNDw4SAihuKEPZteG9Ej0Ku9z3R8wMyTMXq8Uu70iB7SOWY13mBcSVnt5CQ="
      ]
    when:""
      def xml = service.xmlPayOrderRequest(data)
    then:""
      xml.replaceAll("\n", "").replaceAll(" ", "") == """
        <soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:h2h='http://h2h.integration.spei.enlacefi.lgec.com/'>
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
                   <firma>Yik10Fo+yReNXvSfeYvbIlthR2e05PETN+4WXqnOsfStHBYTo/QCRsDJCsCgaNOLfGArByCWGwDA9Lx5htWRB0KKnyZVIDE1qBrsjfb7MEY+sqCqiNDw4SAihuKEPZteG9Ej0Ku9z3R8wMyTMXq8Uu70iB7SOWY13mBcSVnt5CQ=</firma>
                   <emailBeneficiario>mailBeneficiary@mail.com</emailBeneficiario>
               </ordenPago>
           </h2h:registraOrden>
          </soapenv:Body>
        </soapenv:Envelope>
      """.replaceAll("\n", "").replaceAll(" ", "")
  }

  void "Generate xml of incorrect pay order"() {
    given:""
      def data = [
        institucionContraparte: "40002",
        empresa: "TEXTO muy grande mas de lo permitidooooooooooooooooo",
        fechaDeOperacion: "20170306",
        folioOrigen: "",
        claveDeRastreo: "1488820184033",
        institucionOperante: "90646",
        montoDelPago: 250.00,
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
        tipoDeOperacion: "",
        topologia: "",
        usuario: "",
        medioDeEntrega: "",
        prioridad: "",
        iva: "",
        firma: "Yik10Fo+yReNXvSfeYvbIlthR2e05PETN+4WXqnOsfStHBYTo/QCRsDJCsCgaNOLfGArByCWGwDA9Lx5htWRB0KKnyZVIDE1qBrsjfb7MEY+sqCqiNDw4SAihuKEPZteG9Ej0Ku9z3R8wMyTMXq8Uu70iB7SOWY13mBcSVnt5CQ="
      ]
    when:""
      service.xmlPayOrderRequest(data)
    then:""
      thrown XMLException
  }

  void "Should generate the xml to send for get transactions conciliation from account"() {
    given:""
      def data = [
        initDate: "2017-04-01 00:00:00",
        endDate: "2017-04-30 23:59:59",
        account: "646180132400000001",
        costsCenter: "TECHMINDS",
        sign: "encryptedSign"
     ]
    when:""
      def xml = service.xmlSignedConciliationRequest(data)
    then:""
      xml.replaceAll("\n", "").replaceAll(" ", "") == """
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:h2h="http://h2h.integration.spei.enlacefi.lgec.com/">
        <soapenv:Header/>
        <soapenv:Body>
          <h2h:conciliacionServiceFirma>
            <fechaInicial>2017-04-01 00:00:00</fechaInicial>
            <fechaFinal>2017-04-30 23:59:59</fechaFinal>
            <cuenta>646180132400000001</cuenta>
            <empresa>TECHMINDS</empresa>
            <recorreSubempresas>1</recorreSubempresas>
            <firma>encryptedSign</firma>
          </h2h:conciliacionServiceFirma>
      </soapenv:Body>
      </soapenv:Envelope>
      """.replaceAll("\n", "").replaceAll(" ", "")
  }

  void "Generate xml for replacement invoice pay order"() {
    given:""
      def data = [
        institucionContraparte: "40002",
        empresa: "TECHMINDS",
        fechaDeOperacion: "20170306",
        folioOrigen: "",
        claveDeRastreo: "1488820184033",
        institucionOperante: "90646",
        montoDelPago: 250.00,
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
        tipoDeOperacion: "",
        topologia: "",
        usuario: "",
        medioDeEntrega: "",
        prioridad: "",
        iva: "",
        uuidReplacement:"123",
        firma: "Yik10Fo+yReNXvSfeYvbIlthR2e05PETN+4WXqnOsfStHBYTo/QCRsDJCsCgaNOLfGArByCWGwDA9Lx5htWRB0KKnyZVIDE1qBrsjfb7MEY+sqCqiNDw4SAihuKEPZteG9Ej0Ku9z3R8wMyTMXq8Uu70iB7SOWY13mBcSVnt5CQ="
      ]
    when:""
      def xml = service.xmlPayOrderReplacementInvoice(data)
    then:""
      xml.replaceAll("\n", "").replaceAll(" ", "") == """
        <soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:h2h='http://h2h.integration.spei.enlacefi.lgec.com/'>
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
                   <firma>Yik10Fo+yReNXvSfeYvbIlthR2e05PETN+4WXqnOsfStHBYTo/QCRsDJCsCgaNOLfGArByCWGwDA9Lx5htWRB0KKnyZVIDE1qBrsjfb7MEY+sqCqiNDw4SAihuKEPZteG9Ej0Ku9z3R8wMyTMXq8Uu70iB7SOWY13mBcSVnt5CQ=</firma>
                   <emailBeneficiario>mailBeneficiary@mail.com</emailBeneficiario>
                   <uuidReplacement>123</uuidReplacement>
               </ordenPago>
           </h2h:registraOrden>
          </soapenv:Body>
        </soapenv:Envelope>
      """.replaceAll("\n", "").replaceAll(" ", "")
  }

  void "Should generate an incorrect xml"() {
    given:""
      def data = [
        institucionContraparte: "40002",
        empresa: "TEXTO muy grande mas de lo permitidooooooooooooooooo",
        fechaDeOperacion: "20170306",
        folioOrigen: "",
        claveDeRastreo: "1488820184033",
        institucionOperante: "90646",
        montoDelPago: 250.00,
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
        tipoDeOperacion: "",
        topologia: "",
        usuario: "",
        medioDeEntrega: "",
        prioridad: "",
        iva: "",
        uuidReplacement:"123",
        firma: "Yik10Fo+yReNXvSfeYvbIlthR2e05PETN+4WXqnOsfStHBYTo/QCRsDJCsCgaNOLfGArByCWGwDA9Lx5htWRB0KKnyZVIDE1qBrsjfb7MEY+sqCqiNDw4SAihuKEPZteG9Ej0Ku9z3R8wMyTMXq8Uu70iB7SOWY13mBcSVnt5CQ="
      ]
    when:""
      def xml = service.xmlPayOrderReplacementInvoice(data)
    then:""
      thrown XMLException
  }

  void "Should generate an incorrect xml, doesn't have uuid"() {
    given:""
      def data = [
        institucionContraparte: "40002",
        empresa: "TECHMINDS",
        fechaDeOperacion: "20170306",
        folioOrigen: "",
        claveDeRastreo: "1488820184033",
        institucionOperante: "90646",
        montoDelPago: 250.00,
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
        tipoDeOperacion: "",
        topologia: "",
        usuario: "",
        medioDeEntrega: "",
        prioridad: "",
        iva: "",
        uuidReplacement:"",
        firma: "Yik10Fo+yReNXvSfeYvbIlthR2e05PETN+4WXqnOsfStHBYTo/QCRsDJCsCgaNOLfGArByCWGwDA9Lx5htWRB0KKnyZVIDE1qBrsjfb7MEY+sqCqiNDw4SAihuKEPZteG9Ej0Ku9z3R8wMyTMXq8Uu70iB7SOWY13mBcSVnt5CQ="
      ]
    when:""
      def xml = service.xmlPayOrderReplacementInvoice(data)
    then:""
      thrown XMLException
  }

}
