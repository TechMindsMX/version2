package com.modulus.uno

class Conciliation {

  User user
  SaleOrder saleOrder
  Payment payment
  BigDecimal amount
  ConciliationStatus status = ConciliationStatus.TO_APPLY

  Date dateCreated
  Date lastUpdated

}
