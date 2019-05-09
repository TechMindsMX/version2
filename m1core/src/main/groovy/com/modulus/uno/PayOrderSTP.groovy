package com.modulus.uno

import grails.validation.Validateable

class PayOrderSTP implements Validateable {

  String claveDeRastreo
  String conceptoDelPago
  String cuentaDelBeneficiario
  String cuentaDelOrdenante
  String emailDelBeneficiario
  String empresa
  String fechaDeOperacion
  String folioOrigen
  String institucionContraparte
  String institucionOperante
  BigDecimal montoDelPago
  String nombreDelBeneficiario
  String nombreDelOrdenante
  String referenciaNumerica
  String rfcCurpDelBeneficiario
  String rfcCurpDelOrdenante
  String tipoDeCuentaDelBeneficiario
  String tipoDeLaCuentaDelOrdenante
  String tipoDelPago

  String firma

  //Valores no documentados en STP pero necesarios para la firma
  String tipoDeCuentaDelBeneficiario2
  String nombreDelBeneficiario2
  String cuentaDelBeneficiario2
  String rfcCurpDelBeneficiario2
  String conceptoDelPago2
  String claveDelCatalogoDeUsuario1
  String claveDelCatalogoDeUsuario2
  String claveDelPago
  String referenciaDeCobranza
  String tipoDeOperacion
  String topologia
  String usuario
  String medioDeEntrega
  String prioridad
  String iva
  String uuidReplacement


  static constraints = {
    claveDeRastreo  blank:false, nullable:false, maxSize:30
    conceptoDelPago blank:false, nullable:false, maxSize:210
    cuentaDelBeneficiario blank:false, nullable:false, maxSize:20
    cuentaDelOrdenante blank:true, nullable:false, maxSize:20
    emailDelBeneficiario blank:true, nullable:false, maxSize:120
    empresa blank:false, nullable:false, maxSize:15
    fechaDeOperacion blank:true, nullable:false, maxSize:8
    firma blank:false, nullable:false
    folioOrigen blank:true, nullable:false, maxSize:50
    institucionContraparte blank:false, nullable:false, maxSize:5
    institucionOperante blank:false, nullable:false, maxSize:5
    montoDelPago validator: {
      if (!(it.scale() == 2) || !(it.precision() <= 14)) return false
    }
    nombreDelBeneficiario blank:false, nullable:false, maxSize:120
    nombreDelOrdenante blank:true, nullable:false, maxSize:120
    referenciaNumerica blank:false, nullable:false, maxSize:7
    rfcCurpDelBeneficiario blank:false, nullable:false, maxSize:18
    rfcCurpDelOrdenante blank:true, nullable:false, maxSize:18
    tipoDeCuentaDelBeneficiario blank:false, nullable:false, maxSize:2
    tipoDeLaCuentaDelOrdenante blank:true, nullable:false, maxSize:2
    tipoDelPago blank:false, nullable:false, maxSize:2

    tipoDeCuentaDelBeneficiario2 blank:true, nullable:false
    nombreDelBeneficiario2  blank:true, nullable:false
    cuentaDelBeneficiario2 blank:true, nullable:false
    rfcCurpDelBeneficiario2 blank:true, nullable:false
    conceptoDelPago2 blank:true, nullable:false
    claveDelCatalogoDeUsuario1 blank:true, nullable:false
    claveDelCatalogoDeUsuario2 blank:true, nullable:false
    claveDelPago blank:true, nullable:false
    referenciaDeCobranza blank:true, nullable:false
    tipoDeOperacion blank:true, nullable:false
    topologia blank:true, nullable:false
    usuario blank:true, nullable:false
    medioDeEntrega blank:true, nullable:false
    prioridad blank:true, nullable:false
    iva blank:true, nullable:false
    uuidReplacement blank:true, nullable:true
  }

  Map asMap() {
    this.class.declaredFields.findAll { !it.synthetic }.collectEntries {
      [ (it.name):this."$it.name" ]
    }
  }

}
