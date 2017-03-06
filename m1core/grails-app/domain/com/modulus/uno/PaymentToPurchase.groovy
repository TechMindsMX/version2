package com.modulus.uno

class PaymentToPurchase {

  BigDecimal amount

  Date dateCreated
  Date lastUpdated

    static constraints = {
      amount nullable:false
    }
}
