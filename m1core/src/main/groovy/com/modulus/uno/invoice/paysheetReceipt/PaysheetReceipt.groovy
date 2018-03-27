package com.modulus.uno.invoice.paysheetReceipt

import com.modulus.uno.invoice.DatosDeFacturacion
import com.modulus.uno.Contribuyente
import com.modulus.uno.Concepto

class PaysheetReceipt {
  DatosDeFacturacion datosDeFacturacion
  Contribuyente emisor
  Empleado receptor
  Concepto concepto
  Nomina nomina
  String esquema
  String id
}
