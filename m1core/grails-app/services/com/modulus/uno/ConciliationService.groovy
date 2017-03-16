package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class ConciliationService {

  def getTotalToApplyForPayment(Payment payment) {
    BigDecimal applied = new BigDecimal(5000)//Conciliation.findAllByPaymentAndStatus(payment, ConciliationStatus.TO_APPLY).sum().amount
    payment.amount - applied
  }

}
