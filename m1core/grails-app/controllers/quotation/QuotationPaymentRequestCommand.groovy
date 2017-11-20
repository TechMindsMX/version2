package com.modulus.uno.quotation

import grails.validation.Validateable
import java.text.*
import com.modulus.uno.PaymentMethod

class QuotationPaymentRequestCommand implements Validateable  {

  String quotation
  String amount
  String paymentWay
  String note


  QuotationPaymentRequest getQuotationPaymentRequest(){
    new QuotationPaymentRequest(
                               quotationContract:  QuotationContract.get(quotation.toInteger()),
                               amount:getValueInBigDecimal(amount),
                               paymentMethod:getPaymentWay(paymentWay),
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

  PaymentMethod getPaymentWay(String paymentWay){
    PaymentMethod.values().find(){it.toString() == paymentWay }
    //PaymentMethod.EFECTIVO
 }

}
