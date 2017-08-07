package com.modulus.uno.paysheet

class PaysheetEmployee {

  PrePaysheetEmployee prePaysheetEmployee
  BigDecimal salaryImss
  BigDecimal salaryAssimilable
  PaysheetEmployeeStatus status
  BreakdownPaymentEmployee breakdownPayment

  static belongsTo = [paysheet:Paysheet]

}
