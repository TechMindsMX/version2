package com.modulus.uno.paysheet

import com.modulus.uno.S3Asset

class Paysheet {

  PrePaysheet prePaysheet
  PaysheetStatus status = PaysheetStatus.CREATED
  String rejectReason

  Date dateCreated
  Date lastUpdated

  static belongsTo = [paysheetContract:PaysheetContract]
  static hasMany = [employees:PaysheetEmployee, dispersionFiles:S3Asset]

  static constraints = {
    rejectReason nullable:true
  }

  BigDecimal getTotal() {
    employees*.totalToInvoice.sum() ?: 0
  }
}
