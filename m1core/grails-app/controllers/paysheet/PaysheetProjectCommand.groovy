package com.modulus.uno.paysheet

import java.text.*
import grails.validation.Validateable
import com.modulus.uno.Company

class PaysheetProjectCommand implements Validateable {

  String companyId
  String name
  String description
  String integrationFactor
  String occupationalRiskRate

  static constrains = {
    companyId nullable:false
    name nullable:false, blank:false
    description nullable:true, blank:true
    integrationFactor nullable:false
    occupationalRiskRate nullable:false
  }

  PaysheetProject createPaysheetProject() {
    Company company = Company.get(this.companyId)
    new PaysheetProject(
      company:company,
      name:this.name,
      description:this.name,
      integrationFactor:getValueInBigDecimal(this.integrationFactor),
      occupationalRiskRate:getValueInBigDecimal(this.occupationalRiskRate)
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
