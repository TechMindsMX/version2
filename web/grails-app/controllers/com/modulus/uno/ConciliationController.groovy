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
      redirect action:"chooseInvoiceToConciliate", id:command.payment.id
      return
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

  @Transactional
  def cancelConciliation(Payment payment) {
    log.info "Canceling conciliation for payment: ${payment.id}"
    conciliationService.cancelConciliationsForPayment(payment)
    redirect controller:"payment", action:"conciliation"
  }

  @Transactional
  def applyConciliationsForPayment(Payment payment) {
    log.info "Applying conciliations for payment: ${payment.id}"
    conciliationService.applyConciliationsForPayment(payment)
    redirect controller:"payment", action:"conciliation"
  }

  def conciliationWithoutInvoice(Payment payment) {
    log.info "Payment to conciliate without invoice: ${payment.dump()}"
    [payment:payment, toApply:new BigDecimal(0)]
  }

  @Transactional
  def applyConciliationWithoutInvoice(ConciliationCommand command) {
    log.info "Applying conciliation without invoice: ${command.dump()}"

    if (command.hasErrors()){
      transactionStatus.setRollbackOnly()
      render view:"conciliationWithoutInvoice", model:[payment:command.payment, toApply:new BigDecimal(0), errors:command.errors]
      return
    }

    Conciliation conciliation = command.createConciliation()
    conciliationService.applyConciliationWithoutInvoice(conciliation)

    redirect controller:"payment", action:"conciliation"
  }

  private List<SaleOrder> getSaleOrdersToListForBankingTransaction(MovimientosBancarios bankingTransaction) {
    Company company = Company.get(session.company)
    List<SaleOrder> saleOrders = saleOrderService.findOrdersToConciliateForCompany(company)
    List<Conciliation> conciliations = Conciliation.findAllByCompanyAndStatus(company, ConciliationStatus.TO_APPLY)
    List<SaleOrder> saleOrdersFiltered = saleOrders.findAll { saleOrder ->
      if (!conciliations.find { conciliation -> conciliation.saleOrder.id == saleOrder.id }){
        saleOrder
      }
    }
    saleOrdersFiltered
  }

  def chooseInvoiceToConciliateWithBankingTransaction(MovimientosBancarios bankingTransaction) {
    log.info "Banking Transaction to conciliate: ${bankingTransaction.dump()}"
    BigDecimal toApply = conciliationService.getTotalToApplyForBankingTransaction(bankingTransaction)
    List<Conciliation> conciliations = conciliationService.getConciliationsToApplyForBankingTransaction(bankingTransaction)
    List<SaleOrder> saleOrders = getSaleOrdersToListForBankingTransaction(bankingTransaction)

    [bankingTransaction:bankingTransaction, saleOrders:saleOrders, toApply:toApply, conciliations:conciliations]
  }

  @Transactional
  def applyConciliationWithoutPayment(ConciliationCommand command) {
    log.info "Applying conciliation without payment: ${command.dump()}"

    if (command.hasErrors()){
      transactionStatus.setRollbackOnly()
      render view:"conciliateInvoiceWithoutPayment", model:[saleOrder:command.saleOrder, errors:command.errors]
      return
    }

    Conciliation conciliation = command.createConciliation()
    conciliationService.applyConciliationWithoutPayment(conciliation)

    redirect controller:"payment", action:"conciliation"
  }

}
