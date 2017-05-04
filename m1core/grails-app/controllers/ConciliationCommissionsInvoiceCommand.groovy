package com.modulus.uno

import java.text.*
import grails.validation.Validateable

class ConciliationCommissionsInvoiceCommand implements Validateable {

  String paymentId
  String invoiceId
  String amountToApply

  ConciliationCommissionsInvoice createConciliation() {
    new ConciliationCommissionsInvoice(
      payment:PaymentM1Emitter.get(this.paymentId),
      invoice:CommissionsInvoice.get(this.invoiceId),
      amount:getValueInBigDecimal(this.amountToApply)
    )
  }

  private def getValueInBigDecimal(String value) {
    Locale.setDefault(new Locale("es","MX"));
    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
    df.setParseBigDecimal(true);
    BigDecimal bd = (BigDecimal) df.parse(value);
    bd
  }

}
