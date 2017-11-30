package com.modulus.uno

class FacturaCommand implements MessageCommand {
  DatosDeFacturacion datosDeFacturacion
  Contribuyente emisor
  Contribuyente receptor
  String emitter
  String pdfTemplate

  List<Concepto> conceptos
  TotalesImpuestos totalesImpuestos
  Boolean betweenIntegrated = false
  String observaciones
}
