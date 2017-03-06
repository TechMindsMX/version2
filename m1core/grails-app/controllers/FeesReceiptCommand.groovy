package com.modulus.uno
import java.text.*

import grails.validation.Validateable

class FeesReceiptCommand implements Validateable {
  String id
  String amount
  String iva
  String isr
  String ivaWithHolding

  static constraints = {
    id nullable:true
  }

  FeesReceipt createFeesReceipt(){
    new FeesReceipt(
      amount:getValueAsBigDecimal(this.amount)
    )
  }

  private def getValueAsBigDecimal(String value) {
    Locale.setDefault(new Locale("es","MX"))
    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance()
    df.setParseBigDecimal(true)
    BigDecimal bd = (BigDecimal) df.parse(value)
    bd
  }
}
