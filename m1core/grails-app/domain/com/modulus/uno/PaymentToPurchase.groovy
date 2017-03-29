package com.modulus.uno

class PaymentToPurchase {

  BigDecimal amount

  Transaction transaction

  Date dateCreated
  Date lastUpdated

    static constraints = {
      transaction nullable:true
      amount nullable:false
    }
}
