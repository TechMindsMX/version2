package com.modulus.uno

import grails.transaction.Transactional
import java.math.RoundingMode

class ConciliationController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def saleOrderService
  def conciliationService

  def chooseInvoiceToConciliate(Payment payment) {
    log.info "Payment to conciliate: ${payment.dump()}"
    BigDecimal toApply = conciliationService.getTotalToApplyForPayment(payment)
    List<Conciliation> conciliations = conciliationService.getConciliationsToApplyForPayment(payment)
    List<SaleOrder> saleOrders = getSaleOrdersToListForPayment(payment)

    [payment:payment, saleOrders:saleOrders, toApply:toApply, conciliations:conciliations]
  }

  private List<SaleOrder> getSaleOrdersToListForPayment(Payment payment) {
    List<SaleOrder> saleOrders = payment.rfc ? saleOrderService.findOrdersToConciliateForCompanyAndClient(payment.company, payment.rfc) : saleOrderService.findOrdersToConciliateForCompany(payment.company)
    List<Conciliation> conciliations = Conciliation.findAllByCompanyAndStatus(payment.company, ConciliationStatus.TO_APPLY)
    List<SaleOrder> saleOrdersFiltered = saleOrders.findAll { saleOrder ->
      if (!conciliations.find { conciliation -> conciliation.saleOrder.id == saleOrder.id }){
        saleOrder
      }
    }
    saleOrdersFiltered
  }

  @Transactional
  def addSaleOrderToConciliate(ConciliationCommand command) {
    log.info "Adding conciliation to apply: ${command.dump()}"

    if (command.hasErrors()){
      transactionStatus.setRollbackOnly()
    }

    Conciliation conciliation = command.createConciliation()
    Company company = Company.get(session.company)
    try {
      conciliationService.saveConciliationForCompany(conciliation, company)
    } catch (BusinessException ex) {
      flash.message = ex.message
    }

    redirect action:"chooseInvoiceToConciliate", id:command.paymentId
  }

  @Transactional
  def deleteConciliation(Conciliation conciliation) {
    log.info "Deleting conciliation to apply: ${conciliation.dump()}"
    Payment payment = conciliation.payment

    conciliationService.deleteConciliation(conciliation)

    redirect action:"chooseInvoiceToConciliate", id:payment.id
  }

}
