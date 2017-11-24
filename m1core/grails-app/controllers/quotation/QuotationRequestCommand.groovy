
package com.modulus.uno.quotation

import com.modulus.uno.BusinessEntity
import com.modulus.uno.Company
import grails.validation.Validateable
import java.text.*

class QuotationRequestCommand implements Validateable {

  String description
  String commission
  String quotation
  String biller
  String iva
  String total
  String subtotal

  QuotationRequest getQuotationRequest(){
    new QuotationRequest(
        commission: QuotationContract.get(quotation?.toInteger()).commission,
        description: description,
        biller:QuotationContract.get(quotation.toInteger()).company,
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
