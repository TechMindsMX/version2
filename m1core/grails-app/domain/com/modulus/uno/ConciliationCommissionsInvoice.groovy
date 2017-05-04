package com.modulus.uno

class ConciliationCommissionsInvoice {

  Date dateCreated
  Date lastUpdated

  PaymentM1Emitter payment
  BigDecimal amount
  ConciliationStatus status = ConciliationStatus.TO_APPLY
  CommissionsInvoice invoice

  static constraints = {
    amount nullable:false
  }

}
