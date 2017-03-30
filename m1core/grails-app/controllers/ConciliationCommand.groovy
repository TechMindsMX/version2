package com.modulus.uno

import java.text.*
import grails.validation.Validateable

class ConciliationCommand implements Validateable {

  String paymentId
  String bankingTransactionId
  String saleOrderId
  String changeType
  String amountToApply
  String comment

  static constraints = {
    paymentId nullable:true
    bankingTransactionId nullable:true
    saleOrderId nullable:true
    comment nullable:true
  }

  Conciliation createConciliation() {
    new Conciliation(
      payment:this.paymentId ? Payment.get(this.paymentId) : null,
      saleOrder:this.saleOrderId ? SaleOrder.get(this.saleOrderId) : null,
      bankingTransaciont:this.bankingTransaciontId ? MovimientosBancarios.get(this.bankingTransaciontId) : null,
      changeType:getValueInBigDecimal(this.changeType),
      amount:getValueInBigDecimal(this.amountToApply),
      comment:this.comment
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
