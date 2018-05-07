package com.modulus.uno

import grails.transaction.Transactional
import java.math.RoundingMode

import com.modulus.uno.saleorder.SaleOrder

class ConciliationController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def saleOrderService
  def conciliationService
	def purchaseOrderService

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

    if (conciliation.payment) {
      redirect action:"chooseInvoiceToConciliate", id:command.paymentId
      return
    } else if (conciliation.bankingTransaction) {
      redirect action:"chooseInvoiceToConciliateWithBankingDeposit", id:command.bankingTransactionId
      return
    }
  }

  @Transactional
  def deleteConciliation(Conciliation conciliation) {
    log.info "Deleting conciliation to apply: ${conciliation.dump()}"
    Payment payment = conciliation.payment
    MovimientosBancarios bankingTransaction = conciliation.bankingTransaction

    conciliationService.deleteConciliation(conciliation)

    if (payment) {
      redirect action:"chooseInvoiceToConciliate", id:payment.id
      return
    } else if (bankingTransaction) {
      redirect action:"chooseInvoiceToConciliateWithBankingTransaction", id:bankingTransaction.id
      return
    }
  }

  @Transactional
  def cancelConciliation(Payment payment) {
    log.info "Canceling conciliation for payment: ${payment.id}"
    conciliationService.cancelConciliationsForPayment(payment)
    redirect controller:"payment", action:"conciliation"
  }

  @Transactional
  def cancelConciliationBankingTransaction(MovimientosBancarios bankingTransaction) {
    log.info "Canceling conciliation for bankingTransaction: ${bankingTransaction.id}"
    conciliationService.cancelConciliationsForBankingTransaction(bankingTransaction)
    redirect controller:"payment", action:"conciliation"
  }

  @Transactional
  def applyConciliationsForPayment(Payment payment) {
    log.info "Applying conciliations for payment: ${payment.id}"
    conciliationService.applyConciliationsForPayment(payment)
    redirect controller:"payment", action:"conciliation"
  }

  @Transactional
  def applyConciliationsForBankingTransaction(MovimientosBancarios bankingTransaction) {
    log.info "Applying conciliations for banking transaction: ${bankingTransaction.id}"
    conciliationService.applyConciliationsForBankingTransaction(bankingTransaction)
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
      if (!conciliations.find { conciliation -> conciliation.saleOrder?.id == saleOrder.id }){
        saleOrder
      }
    }
    saleOrdersFiltered
  }

  def chooseInvoiceToConciliateWithBankingDeposit(MovimientosBancarios bankingTransaction) {
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

  def choosePaymentToPurchaseToConciliateWithBankingWithdraw(MovimientosBancarios bankingTransaction) {
    log.info "Banking Transaction to conciliate: ${bankingTransaction.dump()}"
    BigDecimal toApply = conciliationService.getTotalToApplyForBankingTransaction(bankingTransaction)
    List<Conciliation> conciliations = conciliationService.getConciliationsToApplyForBankingTransaction(bankingTransaction)
    Map paymentsToPurchase = getPaymentsToPurchaseToListForBankingTransaction(bankingTransaction)

    [bankingTransaction:bankingTransaction, paymentsToPurchase:paymentsToPurchase, toApply:toApply, conciliations:conciliations]
  }

  private Map getPaymentsToPurchaseToListForBankingTransaction(MovimientosBancarios bankingTransaction) {
    Company company = Company.get(session.company)
		Map paymentsAndPurchases = [:]
    List<PaymentToPurchase> payments = purchaseOrderService.findBankingPaymentsToPurchaseToConciliateForCompany(company)
    List<Conciliation> conciliations = Conciliation.findAllByCompanyAndStatus(company, ConciliationStatus.TO_APPLY)
    List<PaymentToPurchase> paymentsFiltered = payments.findAll { payment ->
      if (!conciliations.find { conciliation -> conciliation.paymentToPurchase?.id == payment.id }){
        payment
      }
    }
		List<PurchaseOrder> purchaseOrders = []
		paymentsFiltered.each { payment ->
			purchaseOrders.add(purchaseOrderService.getPurchaseOrderOfPaymentToPurchase(payment))
		}
    paymentsAndPurchases.paymentsFiltered = paymentsFiltered
		paymentsAndPurchases.purchaseOrders = purchaseOrders
		paymentsAndPurchases
  }

  @Transactional
  def addPaymentToPurchaseToConciliate(ConciliationCommand command) {
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

    if (conciliation.payment) {
      redirect action:"chooseInvoiceToConciliate", id:command.paymentId
      return
    } else if (conciliation.bankingTransaction) {
      redirect action:"choosePaymentToPurchaseToConciliateWithBankingWithdraw", id:command.bankingTransactionId
      return
    }
  }

}
