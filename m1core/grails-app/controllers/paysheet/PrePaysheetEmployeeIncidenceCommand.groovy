package com.modulus.uno.paysheet

import java.text.*
import grails.validation.Validateable

class PrePaysheetEmployeeIncidenceCommand implements Validateable{

  String description
  String type
  String schema
  String prePaysheetEmployeeId
  String amount

  static constraints = {
    description nullable:false
    amount nullable:false
  }

  public PrePaysheetEmployeeIncidence createPrePaysheetEmployeeIncidence() {
    PrePaysheetEmployee prePaysheetEmployee = PrePaysheetEmployee.get(this.prePaysheetEmployeeId)
    new PrePaysheetEmployeeIncidence(
        description:this.description,
        type:IncidenceType.values().find { it.toString() == this.type },
        schema:PaymentSchema.values().find { it.toString() == this.schema },
        amount: getValueInBigDecimal(this.amount),
        prePaysheetEmployee: prePaysheetEmployee
    )
  }

  private def getValueInBigDecimal(String value) {
    Locale.setDefault(new Locale("es","MX"))
    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance()
    df.setParseBigDecimal(true)
    BigDecimal bd = (BigDecimal) df.parse(value)
    bd
  }
}
