package com.modulus.uno.quotation

import grails.validation.Validateable
import java.text.*
import com.modulus.uno.BusinessEntity
import com.modulus.uno.Company

class QuotationRequestCommand implements Validateable {

  String description
  String commission
  String quotation
  String biller
  String iva
  String total
  String subtotal
  String paymentWay
  String paymentMethod
  String invoicePurpose

  static constraints = {
    paymentWay nullable:true
    paymentMethod nullable:true
    invoicePurpose nullable:true
    biller nullable:true
  } 

  QuotationRequest getQuotationRequest(){
    new QuotationRequest(
        commission: QuotationContract.get(quotation?.toInteger()).commission,
        description: description,
        quotationContract:  QuotationContract.get(quotation.toInteger()),
        iva:getValueInBigDecimal(iva),
        subtotal: getValueInBigDecimal(subtotal),
        total: getValueInBigDecimal(total)
    )
  }

  private def getValueInBigDecimal(String value) {
    Locale.setDefault(new Locale("es","MX"));
    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
    df.setParseBigDecimal(true);
    BigDecimal bd = (BigDecimal) df.parse(value);
    bd
  }

  BigDecimal getCommission(String commission){
    getValueInBigDecimal(commission)
  }

}
