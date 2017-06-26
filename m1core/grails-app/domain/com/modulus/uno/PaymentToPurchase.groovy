package com.modulus.uno

class PaymentToPurchase {

  BigDecimal amount
  PaymentToPurchaseStatus status = PaymentToPurchaseStatus.APPLIED

  Transaction transaction

  Date dateCreated
  Date lastUpdated

    static constraints = {
      transaction nullable:true
      amount nullable:false
    }
}
