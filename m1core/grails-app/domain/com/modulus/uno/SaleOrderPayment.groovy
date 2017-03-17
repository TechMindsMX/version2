package com.modulus.uno

class SaleOrderPayment {

  BigDecimal amount

  static belongsTo = [saleOrder:SaleOrder]

  Date dateCreated
  Date lastUpdated

}
