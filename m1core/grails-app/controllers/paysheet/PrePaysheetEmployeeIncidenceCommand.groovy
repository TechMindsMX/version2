package com.modulus.uno.paysheet

import java.text.*
import grails.validation.Validateable

class PrePaysheetEmployeeIncidenceCommand implements Validateable{

  String internalKey
  String description
  String keyType
  String type
  String schema
  String prePaysheetEmployeeId
  String exemptAmount
  String taxedAmount

  static constraints = {
    description nullable:false
    exemptAmount nullable:false
    taxedAmount nullable:false
  }

  public PrePaysheetEmployeeIncidence createPrePaysheetEmployeeIncidence() {
    PrePaysheetEmployee prePaysheetEmployee = PrePaysheetEmployee.get(this.prePaysheetEmployeeId)
    new PrePaysheetEmployeeIncidence(
        internalKey:this.internalKey,
        description:this.description,
        keyType:this.keyType,
        type:IncidenceType.values().find { it.toString() == this.type },
        paymentSchema:PaymentSchema.values().find { it.toString() == this.schema },
        exemptAmount: getValueInBigDecimal(this.amount),
        taxedAmount: getValueInBigDecimal(this.taxedAmount),
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
