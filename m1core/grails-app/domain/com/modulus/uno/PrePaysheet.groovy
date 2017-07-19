package com.modulus.uno

class PrePaysheet {
  String paysheetProject
  String paymentPeriod
  String accountExecutive
  PrePaysheetStatus status = PrePaysheetStatus.CREATED

  Date dateCreated
  Date lastUpdated

  static belongsTo = [company:Company]
  static hasMany = [employees:PrePaysheetEmployee]

  BigDecimal getTotal() {
    employees*.netPayment.sum() ?: 0
  }
}
