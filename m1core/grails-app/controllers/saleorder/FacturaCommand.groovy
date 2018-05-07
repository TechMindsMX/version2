package com.modulus.uno.saleorder

import com.modulus.uno.invoice.*
import com.modulus.uno.MessageCommand

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
