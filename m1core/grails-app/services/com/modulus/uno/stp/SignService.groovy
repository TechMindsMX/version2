package com.modulus.uno.stp

import com.stp.h2h.CryptoHandler.STPCryptoHandler
import com.modulus.uno.SignException

class SignService {

  def grailsApplication

  def generateSign(Map data){
    def listKeys = ["institucionContraparte","empresa",
    "fechaDeOperacion","folioOrigen","claveDeRastreo",
    "institucionOperante","montoDelPago","tipoDelPago",
    "tipoDeLaCuentaDelOrdenante","nombreDelOrdenante",
    "cuentaDelOrdenante","rfcCurpDelOrdenante",
    "tipoDeCuentaDelBeneficiario","nombreDelBeneficiario",
    "cuentaDelBeneficiario","rfcCurpDelBeneficiario",
    "emailDelBeneficiario","tipoDeCuentaDelBeneficiario2",
    "nombreDelBeneficiario2","cuentaDelBeneficiario2",
    "rfcCurpDelBeneficiario2","conceptoDelPago",
    "conceptoDelPago2","claveDelCatalogoDeUsuario1",
    "claveDelCatalogoDeUsuario2","claveDelPago",
    "referenciaDeCobranza","referenciaNumerica",
    "tipoDeOperacion","topologia","usuario",
    "medioDeEntrega","prioridad","iva"]
    if (!(listKeys == data*.key.intersect(listKeys))){
      throw new SignException("Datos faltantes para la generación de la firma")
    }
    def sign = listKeys.collect{ String key ->
      data."$key"
    }.join("|")
    "||${sign}||"
  }

  def encodeSign(String sign){
    def jks = grailsApplication.config.stp.jks
    def username = grailsApplication.config.stp.username
    def password = grailsApplication.config.stp.password
    def cripto = new STPCryptoHandler()
    cripto.sign(jks, password, username, sign).replaceAll("\n", "")
  }

}
