package com.modulus.uno

class SaleOrderPayment {

  BigDecimal amount
  Conciliation conciliation

  static belongsTo = [saleOrder:SaleOrder]

  Date dateCreated
  Date lastUpdated

}
