package com.modulus.uno

class PaymentToPurchase {

  BigDecimal amount
  PaymentToPurchaseStatus status = PaymentToPurchaseStatus.APPLIED
	SourcePayment source = SourcePayment.MODULUS_UNO

  Transaction transaction

  Date dateCreated
  Date lastUpdated

    static constraints = {
      transaction nullable:true
      amount nullable:false
    }
}
