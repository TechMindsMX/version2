package com.modulus.uno

import grails.validation.Validateable

class SaleOrderSTP implements Validateable {

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
  String montoDelPago
  String nombreDelBeneficiario
  String nombreDelOrdenante
  String referenciaNumerica
  String rfcCurpDelBeneficiario
  String rfcCurpDelOrdenante
  String tipoDeCuentaDelBeneficiario
  String tipoDeLaCuentaDelOrdenante
  String tipoDelPago

  String tipoDeCuentaDelBeneficiario2, nombreDelBeneficiario2, 
  cuentaDelBeneficiario2, rfcCurpDelBeneficiario2, conceptoDelPago2, 
  claveDelCatalogoDeUsuario1, claveDelCatalogoDeUsuario2, 
  claveDelPago, referenciaDeCobranza, tipoDeOperación, topologia, 
  usuario, medioDeEntrega, prioridad, iva


  static constraints = {
    claveDeRastreo  blank:false, nullable:false, maxSize:30
    conceptoDelPago blank:false, nullable:false
    cuentaDelBeneficiario blank:false, nullable:false
    cuentaDelOrdenante blank:true, nullable:false
    emailDelBeneficiario blank:true, nullable:false
    empresa blank:false, nullable:false
    fechaDeOperacion blank:true, nullable:false
    // firma
    folioOrigen blank:true, nullable:false
    institucionContraparte blank:false, nullable:false
    institucionOperante blank:false, nullable:false
    montoDelPago blank:false, nullable:false //evaluar valor
    nombreDelBeneficiario blank:false, nullable:false
    nombreDelOrdenante blank:true, nullable:false
    referenciaNumerica blank:false, nullable:false
    rfcCurpDelBeneficiario blank:false, nullable:false
    rfcCurpDelOrdenante blank:true, nullable:false
    tipoDeCuentaDelBeneficiario blank:false, nullable:false
    tipoDeLaCuentaDelOrdenante blank:true, nullable:false
    tipoDelPago blank:false, nullable:false
    iva blank:true, nullable:false
  }

  Map asMap() {
    this.class.declaredFields.findAll { !it.synthetic }.collectEntries {
      [ (it.name):this."$it.name" ]
    }
  }

}