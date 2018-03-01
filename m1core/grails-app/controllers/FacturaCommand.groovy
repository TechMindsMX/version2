package com.modulus.uno

import com.modulus.uno.invoice.*

class FacturaCommand implements MessageCommand {
  String id
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
