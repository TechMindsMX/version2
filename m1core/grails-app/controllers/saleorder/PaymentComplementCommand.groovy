package com.modulus.uno.saleorder

import com.modulus.uno.invoice.*
import com.modulus.uno.MessageCommand

class PaymentComplementCommand implements MessageCommand {
  String id
  DatosDeFacturacion datosDeFacturacion
  Contribuyente emisor
  Contribuyente receptor
  String emitter
  String pdfTemplate

  Payment payment
}
