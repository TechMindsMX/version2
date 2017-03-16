package com.modulus.uno

import java.text.*
import grails.validation.Validateable

class ConciliationCommand implements Validateable {

  String paymentId
  String saleOrderId
  String changeType
  String amountToApply

  private def getValueInBigDecimal(String value) {
    Locale.setDefault(new Locale("es","MX"));
    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
    df.setParseBigDecimal(true);
    BigDecimal bd = (BigDecimal) df.parse(value);
    bd
  }

}
