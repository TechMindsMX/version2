package com.modulus.uno

class PaymentM1Emitter {
  BigDecimal amount
  PaymentStatus status = PaymentStatus.PENDING

  Date dateCreated
  Date lastUpdated

  static constraints = {
    amount nullable:false
  }
}
