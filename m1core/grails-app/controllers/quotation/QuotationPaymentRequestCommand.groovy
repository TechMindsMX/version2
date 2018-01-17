package com.modulus.uno.quotation

import grails.validation.Validateable
import java.text.*
import com.modulus.uno.PaymentWay

class QuotationPaymentRequestCommand implements Validateable  {

  String quotation
  String amount
  String paymentMethod
  String note


  QuotationPaymentRequest getQuotationPaymentRequest(){
    new QuotationPaymentRequest(
                               quotationContract:  QuotationContract.get(quotation.toInteger()),
                               amount:getValueInBigDecimal(amount),
                               paymentMethod:getPaymentWay(paymentMethod),
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

  PaymentWay getPaymentWay(String paymentMethod){
    PaymentWay.values().find(){it.toString() == paymentMethod }
 }

}
