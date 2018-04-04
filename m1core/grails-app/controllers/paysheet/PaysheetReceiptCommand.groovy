package com.modulus.uno

import com.modulus.uno.invoice.DatosDeFacturacion
import com.modulus.uno.invoice.Contribuyente
import com.modulus.uno.invoice.Concepto
import com.modulus.uno.invoice.paysheetReceipt.Nomina
import com.modulus.uno.invoice.paysheetReceipt.Empleado

class PaysheetReceiptCommand implements MessageCommand {
  DatosDeFacturacion datosDeFacturacion
  Contribuyente emisor
  Empleado receptor
  Concepto concepto
  Nomina nomina
  String esquema
  String id
}
