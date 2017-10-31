package com.modulus.uno.quotation

import com.modulus.uno.BusinessEntity
import com.modulus.uno.Company
import com.modulus.uno.quotation.QuotationContract
import grails.validation.Validateable
import java.text.*

class QuotationContractCommand implements Validateable {

  String clients
  String commission
  String initDate

  QuotationContract getQuotationContract(Company company){
    new QuotationContract(
                          client: BusinessEntity.get(clients.toInteger()),
                          commission:getValueInBigDecimal(commission),
                          initDate: new Date().parse("dd/MM/yyyy", initDate),
                          company:company
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
