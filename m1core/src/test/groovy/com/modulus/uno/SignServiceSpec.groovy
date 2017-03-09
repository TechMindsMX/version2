package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(SignService)
class SignServiceSpec extends Specification {

  void "Should generate sign for Sale Order"() {
    given:"The necessary and complete data"
      Map map = [
        institucionContraparte: "example1",
        empresa: "example2",
        fechaDeOperacion: "example3",  
        folioOrigen: "example4",
        claveDeRastreo: "example5",
        institucionOperante: "example6",
        montoDelPago: "example7",
        tipoDelPago: "example8",
        tipoDeLaCuentaDelOrdenante: "example9",
        nombreDelOrdenante: "example10",
        cuentaDelOrdenante: "example11",
        rfcCurpDelOrdenante: "example12",
        tipoDeCuentaDelBeneficiario: "example13",
        nombreDelBeneficiario: "example14",
        cuentaDelBeneficiario: "example15",
        rfcCurpDelBeneficiario: "example16",
        emailDelBeneficiario: "example17",
        tipoDeCuentaDelBeneficiario2: "example18",
        nombreDelBeneficiario2: "example19",
        cuentaDelBeneficiario2: "example20",
        rfcCurpDelBeneficiario2: "example21",
        conceptoDelPago: "example22",
        conceptoDelPago2: "example23",
        claveDelCatalogoDeUsuario1: "example24",
        claveDelCatalogoDeUsuario2: "example25",
        claveDelPago: "example26",
        referenciaDeCobranza: "example27",
        referenciaNumérica: "example28",
        tipoDeOperación: "example29",
        topologia: "example30",
        usuario: "example31",
        medioDeEntrega: "example32",
        prioridad: "example33"
      ]
    when:"call service to generate sign"
      def sign = service.generateSign(map)
    then:
      sign == "||example1|example2|example3|example4|example5|example6|example7|example8|example9|example10|example11|example12|example13|example14|example15|example16|example17|example18|example19|example20|example21|example22|example23|example24|example25|example26|example27|example28|example29|example30|example31|example32|example33||"
  }

  void "Should throw exception because the info is incorrect"() {
    given:"The incomplete data"
      Map map = [
        institucionContraparte: "example1",
        empresa: "example2",
        fechaDeOperacion: "example3",  
        folioOrigen: "example4",
        claveDeRastreo: "example5",
        institucionOperante: "example6",
      ]
    when:"call service to generate sign"
      service.generateSign(map)
    then:
      thrown SignException
  }

}
