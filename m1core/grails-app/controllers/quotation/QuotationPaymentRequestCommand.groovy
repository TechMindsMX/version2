package com.modulus.uno.quotation

import grails.validation.Validateable
import java.text.*
import com.modulus.uno.paysheet.PaymentWay

class QuotationPaymentRequestCommand implements Validateable  {

  String quotation
  String amount
  String paymentWay
  String note


  QuotationPaymentRequest getQuotationPaymentRequest(){
    new QuotationPaymentRequest(
                               quotationContract:  QuotationContract.get(quotation.toInteger()),
                               amount:getValueInBigDecimal(amount),
                               paymentWay:getPaymentWay(paymentWay),
                               note:note
                               )
  }


  private def getValueInBigDecimal(String value) {
    Locale.setDefault(new Locale("es","MX"));
    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
    df.setParseBigDecimal(true);
    BigDecimal bd = (BigDecimal) df.parse(value);
    bd
  }

  PaymentWay getPaymentWay(String paymentWay){
    PaymentWay."${paymentWay}"
 }

}
