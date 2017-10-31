package com.modulus.uno.paysheet

import com.modulus.uno.PaymentPeriod

class PrePaysheet {
  String paysheetProject
  PaymentPeriod paymentPeriod = PaymentPeriod.BIWEEKLY
  Date initPeriod
  Date endPeriod
  String accountExecutive
  PrePaysheetStatus status = PrePaysheetStatus.CREATED

  Date dateCreated
  Date lastUpdated

  static belongsTo = [paysheetContract:PaysheetContract]
  static hasMany = [employees:PrePaysheetEmployee]

  BigDecimal getTotal() {
    employees*.netPayment.sum() ?: 0
  }
}
