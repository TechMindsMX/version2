package com.modulus.uno.paysheet

class PrePaysheetEmployeeIncidence {

  String internalKey
  String description
  String keyType
  IncidenceType type = IncidenceType.PERCEPTION
  PaymentSchema paymentSchema = PaymentSchema.IMSS
  BigDecimal exemptAmount = new BigDecimal(0)
  BigDecimal taxedAmount = new BigDecimal(0)

  static belongsTo = [prePaysheetEmployee:PrePaysheetEmployee]

  static constraints = {
    description nullable:false
    exemptAmount nullable:false
    taxedAmount nullable:false
  }
  
}
