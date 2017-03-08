package com.modulus.uno
import java.text.*
import grails.validation.Validateable

class CommissionCommand implements Validateable {

  String fee
  String percentage
  CommissionType type

  static constraints = {
    fee blank:false
    percentage blank:false
  }

  Commission createCommission(){
    new Commission(
      fee:getValueAsBigDecimal(this.fee),
      percentage:getValueAsBigDecimal(this.percentage),
      type:this.type
    )
  }

  private def getValueAsBigDecimal(String value) {
    Locale.setDefault(new Locale("es","MX"));
    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
    df.setParseBigDecimal(true);
    BigDecimal bd = (BigDecimal) df.parse(value);
    bd
  }

}
