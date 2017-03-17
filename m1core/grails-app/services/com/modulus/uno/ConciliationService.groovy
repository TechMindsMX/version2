package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class ConciliationService {

  def springSecurityService

  def getTotalToApplyForPayment(Payment payment) {
    def conciliations = getConciliationsToApplyForPayment(payment)
    BigDecimal applied = conciliations ? conciliations*.amount.sum() : new BigDecimal(0)
    payment.amount - applied
  }

  List<Conciliation> getConciliationsToApplyForPayment(Payment payment) {
    Conciliation.findAllByPaymentAndStatus(payment, ConciliationStatus.TO_APPLY)
  }

  List<Conciliation> saveConciliation(Conciliation conciliation) {
    conciliation.user = springSecurityService.currentUser
    log.info "Saving conciliation: ${conciliation.dump()}"
    conciliation.save()
    getConciliationsToApplyForPayment(conciliation.payment)
  }
}
