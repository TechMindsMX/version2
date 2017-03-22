package com.modulus.uno

import grails.transaction.Transactional
import java.math.RoundingMode

@Transactional
class ConciliationService {

  def springSecurityService
  def saleOrderService
  def paymentService

  def getTotalToApplyForPayment(Payment payment) {
    def conciliations = getConciliationsToApplyForPayment(payment)
    BigDecimal applied = conciliations ? conciliations*.amount.sum() : new BigDecimal(0)
    payment.amount - applied
  }

  List<Conciliation> getConciliationsToApplyForPayment(Payment payment) {
    Conciliation.findAllByPaymentAndStatus(payment, ConciliationStatus.TO_APPLY)
  }

  void saveConciliationForCompany(Conciliation conciliation, Company company) {
    if (!validateAmountToApply(conciliation)) {
      throw new BusinessException("El monto a conciliar (${conciliation.amount.setScale(2, RoundingMode.HALF_UP)}) no puede ser mayor al monto por pagar de la factura (${conciliation.saleOrder.amountToPay.setScale(2, RoundingMode.HALF_UP)})")
    }

    conciliation.company = company
    conciliation.user = springSecurityService.currentUser
    log.info "Saving conciliation: ${conciliation.dump()}"
    conciliation.save()
  }

  private validateAmountToApply(Conciliation conciliation) {
    BigDecimal amountToConciliate = conciliation.amount
    if (conciliation.saleOrder.currency=="USD") {
      amountToConciliate = conciliation.amount/conciliation.changeType
    }
    amountToConciliate <= conciliation.saleOrder.amountToPay
  }

  void deleteConciliation(Conciliation conciliation) {
    conciliation.delete()
  }

  void cancelConciliationsForPayment(Payment payment) {
    List<Conciliation> conciliations = getConciliationsToApplyForPayment(payment)
    conciliations.each {
      deleteConciliation(it)
    }
  }

  List<Conciliation> getConciliationsAppliedForPayment(Payment payment) {
    Conciliation.findAllByPaymentAndStatus(payment, ConciliationStatus.APPLIED)
  }

  void applyConciliationsForPayment(Payment payment) {
    List<Conciliation> conciliations = getConciliationsToApplyForPayment(payment)
    conciliations.each { conciliation ->
      applyConciliation(conciliation)
    }
    paymentService.conciliatePayment(payment)
 }

  private applyConciliation(Conciliation conciliation) {
    saleOrderService.addPaymentToSaleOrder(conciliation.saleOrder, conciliation.amount)
    conciliation.status = ConciliationStatus.APPLIED
    conciliation.save()
  }

}
