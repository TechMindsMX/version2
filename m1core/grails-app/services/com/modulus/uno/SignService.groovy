package com.modulus.uno

class SignService {

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
    "tipoDeOperación","topologia","usuario",
    "medioDeEntrega","prioridad","iva"]
    if (!(listKeys == data*.key.intersect(listKeys)))
      throw new SignException("Datos faltantes para la generación de la firma")
    def sign = listKeys.collect{ String key ->
      data."$key"
    }.join("|")
    "||${sign}||"
  }
}