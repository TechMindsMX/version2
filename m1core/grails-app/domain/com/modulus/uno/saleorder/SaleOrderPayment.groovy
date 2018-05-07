package com.modulus.uno.saleorder

class SaleOrderPayment {

  BigDecimal amount

  static belongsTo = [saleOrder:SaleOrder]

  Date dateCreated
  Date lastUpdated

}
