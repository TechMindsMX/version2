package com.modulus.uno.paysheet

class PrePaysheetEmployeeIncidence {

  String description
  IncidenceType type = IncidenceType.PERCEPTION
  PaymentSchema paymentSchema = PaymentSchema.IMSS
  BigDecimal amount = new BigDecimal(0)

  static belongsTo = [prePaysheetEmployee:PrePaysheetEmployee]

  static constraints = {
    description nullable:false
    amount nullable:false
  }
  
}
