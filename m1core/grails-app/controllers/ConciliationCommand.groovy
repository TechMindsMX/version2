package com.modulus.uno

import java.text.*
import grails.validation.Validateable

class ConciliationCommand implements Validateable {

  String paymentId
  String bankingTransactionId
  String saleOrderId
  String changeType
  String amountToApply
	String paymentToPurchaseId
  String comment

  static constraints = {
    paymentId nullable:true
    bankingTransactionId nullable:true
    saleOrderId nullable:true
		paymentToPurchaseId nullable:true
    comment nullable:true
  }

  Conciliation createConciliation() {
    new Conciliation(
      payment:this.paymentId ? Payment.get(this.paymentId) : null,
      saleOrder:this.saleOrderId ? SaleOrder.get(this.saleOrderId) : null,
      bankingTransaction:this.bankingTransactionId ? MovimientosBancarios.get(this.bankingTransactionId) : null,
      changeType:getValueInBigDecimal(this.changeType),
      amount:getValueInBigDecimal(this.amountToApply),
			paymentToPurchase:this.paymentToPurchaseId ? PaymentToPurchase.get(this.paymentToPurchaseId) : null,
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
