package com.modulus.uno.paysheet

import java.text.*
import grails.validation.Validateable

class PaysheetProjectCommand implements Validateable {

  String contractId
  String name
  String description
  String integrationFactor
  String occupationalRiskRate
  String commission

  static constrains = {
    contractId nullable:false
    name nullable:false, blank:false
    description nullable:true, blank:true
    integrationFactor nullable:false
    occupationalRiskRate nullable:false
    commission nullable:false
  }

  PaysheetProject createPaysheetProject() {
    PaysheetContract paysheetContract = PaysheetContract.get(this.contractId)
    new PaysheetProject(
      paysheetContract:paysheetContract,
      name:this.name,
      description:this.description,
      integrationFactor:getValueInBigDecimal(this.integrationFactor),
      occupationalRiskRate:getValueInBigDecimal(this.occupationalRiskRate),
      commission:getValueInBigDecimal(this.commission)
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
