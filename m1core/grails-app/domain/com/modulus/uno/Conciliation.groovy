package com.modulus.uno

class Conciliation {

  Company company
  User user
  SaleOrder saleOrder
  Payment payment
  BigDecimal amount
  BigDecimal changeType
  ConciliationStatus status = ConciliationStatus.TO_APPLY
  String comment

  Date dateCreated
  Date lastUpdated

  static constraints = {
    company nullable:false
    amount nullable:false, min:0.01
    user nullable:false
    saleOrder nullable:true
    payment nullable:true
    comment nullable:true, blank:true
  }
}
