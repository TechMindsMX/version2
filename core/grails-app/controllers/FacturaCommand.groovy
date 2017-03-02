package com.modulus.uno

class FacturaCommand implements MessageCommand {
  DatosDeFacturacion datosDeFacturacion
  Contribuyente emisor
  Contribuyente receptor
  String emitter
  String pdfTemplate

  List<Concepto> conceptos
  List<Impuesto> impuestos
  Boolean betweenIntegrated = false
  String observaciones
}
