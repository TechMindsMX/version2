package com.modulus.uno

class CommissionsInvoicePayment {
  Date dateCreated
  Date lastUpdated

  BigDecimal amount

  static belongsTo = [invoice:CommissionsInvoice]
}
