package com.modulus.uno.paysheet

import java.text.*
import grails.validation.Validateable

class PrePaysheetEmployeeIncidenceCommand implements Validateable{

  String incidence
  String type
  String schema
  String prePaysheetEmployeeId
  String exemptAmount
  String taxedAmount
  String extraHoursDays
  String extraHoursType
  String extraHoursQuantity
  String extraHoursAmount

  static constraints = {
    incidence nullable:false
    schema nullable:false
    exemptAmount nullable:false
    taxedAmount nullable:false
    extraHoursDays (nullable:true, validator: { val, obj ->
      (obj.incidence != PerceptionType.P019.toString() || val) ? true : false
    })
    extraHoursType (nullable:true, validator: { val, obj ->
      (obj.incidence != PerceptionType.P019.toString() || val) ? true : false
    })
    extraHoursQuantity (nullable:true, validator: { val, obj ->
      (obj.incidence != PerceptionType.P019.toString() || val) ? true : false
    })
    extraHoursAmount (nullable:true, validator: { val, obj ->
      (obj.incidence != PerceptionType.P019.toString() || val) ? true : false
    })
  }

  public PrePaysheetEmployeeIncidence createPrePaysheetEmployeeIncidence() {
    PrePaysheetEmployee prePaysheetEmployee = PrePaysheetEmployee.get(this.prePaysheetEmployeeId)
    IncidenceType incidenceType = IncidenceType.values().find { it.toString() == this.type }
    def incidenceObj = incidenceType == IncidenceType.DEDUCTION ? DeductionType.values().find { it.toString() == this.incidence } : PerceptionType.values().find { it.toString() == this.incidence }

    ExtraHourIncidence extraHourIncidence
    if (incidenceObj == PerceptionType.P019) {
      ExtraHourType extraHourType = ExtraHourType.values().find { it.toString() == this.extraHoursType  }
      extraHourIncidence = new ExtraHourIncidence(
        days: this.extraHoursDays.toInteger(),
        type: extraHourType.key,
        quantity: this.extraHoursQuantity.toInteger(),
        amount: getValueInBigDecimal(this.extraHoursAmount)
      )
    }

    new PrePaysheetEmployeeIncidence(
        internalKey:incidenceObj.name(),
        description:incidenceObj.description,
        keyType:incidenceObj.key,
        type: incidenceType,
        paymentSchema:PaymentSchema.values().find { it.toString() == this.schema },
        exemptAmount: getValueInBigDecimal(this.exemptAmount),
        taxedAmount: getValueInBigDecimal(this.taxedAmount),
        prePaysheetEmployee: prePaysheetEmployee,
        extraHourIncidence: extraHourIncidence
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
