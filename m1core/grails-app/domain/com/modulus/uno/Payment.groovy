package com.modulus.uno

class Payment {

  BigDecimal amount
  PaymentStatus status = PaymentStatus.PENDING
  SaleOrder saleOrder
  String rfc

  Transaction transaction

  Date dateCreated
  Date lastUpdated

  static belongsTo = [company: Company]

  static constraints = {
    amount nullable:false
    saleOrder nullable:true
    rfc nullable:true
    transaction nullable:true
  }
}
