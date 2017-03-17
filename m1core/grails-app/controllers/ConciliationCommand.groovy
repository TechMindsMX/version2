package com.modulus.uno

import java.text.*
import grails.validation.Validateable

class ConciliationCommand implements Validateable {

  String paymentId
  String saleOrderId
  String changeType
  String amountToApply
  String comment

  static constraints = {
    paymentId nullable:true
    saleOrderId nullable:true
    comment nullable:true
  }

  Conciliation createConciliation() {
    new Conciliation(
      payment:this.payment ? Payment.get(this.payment) : null,
      saleOrder:this.saleOrderId ? SaleOrder.get(this.saleOrderId) : null,
      changeType:getValueInBigDecimal(this.changeType),
      amount:getValueInBigDecimal(this.amountApply),
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
