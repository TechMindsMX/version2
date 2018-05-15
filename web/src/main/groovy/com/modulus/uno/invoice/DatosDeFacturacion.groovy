package com.modulus.uno.invoice

class DatosDeFacturacion {
  String folio
  String serie
  FormaDePago formaDePago
  String tipoDeComprobante = 'I'
  String lugarDeExpedicion
  MetodoDePago metodoDePago
  String numeroDeCuentaDePago = 'DESCONOCIDO'
  String moneda = 'MXN'
  BigDecimal tipoDeCambio = new BigDecimal(1)
  String addendaLabel
  String tipoRelacion
  String cfdiRelacionado
}
