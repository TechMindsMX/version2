package com.modulus.uno.stp

import grails.test.mixin.TestFor
import spock.lang.Specification

import com.modulus.uno.SignException

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
        referenciaNumerica: "example28",
        tipoDeOperacion: "example29",
        topologia: "example30",
        usuario: "example31",
        medioDeEntrega: "example32",
        prioridad: "example33",
        iva: "example34"
      ]
    when:"call service to generate sign"
      def sign = service.generateSign(map)
    then:
      sign == "||example1|example2|example3|example4|example5|example6|example7|example8|example9|example10|example11|example12|example13|example14|example15|example16|example17|example18|example19|example20|example21|example22|example23|example24|example25|example26|example27|example28|example29|example30|example31|example32|example33|example34||"
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

  void "Should generate sign for real Sale Order"() {
    given:"The necessary and complete data"
      Map map = [
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
        tipoDeOperacion: "",
        topologia: "",
        usuario: "",
        medioDeEntrega: "",
        prioridad: "",
        iva: ""
      ]
    when:"call service to generate sign"
      def sign = service.generateSign(map)
    then:
      sign == "||40002|TECHMINDS|20170306||1488820184033|90646|250.00|1||TECHMINDS|||40|Provider Soft Temoc Uno|002180000201612076|NA|mailBeneficiary@mail.com|||||PAGO A PROVEEDOR||||||1170306||||||||"
  }

}
