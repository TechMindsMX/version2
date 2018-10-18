package com.modulus.uno

import grails.transaction.Transactional
import java.math.RoundingMode
import com.modulus.uno.status.ConciliationStatus
import com.modulus.uno.status.PaymentComplementStatus
import com.modulus.uno.saleorder.PaymentComplementService

@Transactional
class ConciliationService {

  def springSecurityService
  def saleOrderService
  def paymentService
  def movimientosBancariosService
  def purchaseOrderService
  PaymentComplementService paymentComplementService

  def getTotalToApplyForPayment(Payment payment) {
    def conciliations = getConciliationsToApplyForPayment(payment)
    BigDecimal applied = conciliations ? conciliations*.amount.sum() : new BigDecimal(0)
    payment.amount - applied
  }

  def getTotalToApplyForBankingTransaction(MovimientosBancarios bankingTransaction) {
    def conciliations = getConciliationsToApplyForBankingTransaction(bankingTransaction)
    BigDecimal applied = conciliations ? conciliations*.amount.sum() : new BigDecimal(0)
    bankingTransaction.amount - applied
  }

  List<Conciliation> getConciliationsToApplyForPayment(Payment payment) {
    Conciliation.findAllByPaymentAndStatus(payment, ConciliationStatus.TO_APPLY)
  }

  List<Conciliation> getConciliationsToApplyForBankingTransaction(MovimientosBancarios bankingTransaction) {
    Conciliation.findAllByBankingTransactionAndStatus(bankingTransaction, ConciliationStatus.TO_APPLY)
  }

  void saveConciliationForCompany(Conciliation conciliation, Company company) {
    if (!validateAmountToApply(conciliation)) {
      throw new BusinessException("El monto a conciliar (${conciliation.amount.setScale(2, RoundingMode.HALF_UP)}) no puede ser mayor al monto por pagar  (${conciliation.saleOrder ? conciliation.saleOrder.amountToPay.setScale(2, RoundingMode.HALF_UP) : conciliation.paymentToPurchase.amount.setScale(2, RoundingMode.HALF_UP)})")
    }

    conciliation.company = company
    conciliation.user = springSecurityService.currentUser
    log.info "Saving conciliation: ${conciliation.dump()}"
    conciliation.save()
  }

  private validateAmountToApply(Conciliation conciliation) {
    BigDecimal amountToConciliate = conciliation.amount
		BigDecimal maxAmount = conciliation.saleOrder ? conciliation.saleOrder.amountToPay : conciliation.paymentToPurchase.amount
    if (conciliation.saleOrder?.currency=="USD") {
      amountToConciliate = conciliation.amount/conciliation.changeType
    }
    amountToConciliate.setScale(2, RoundingMode.HALF_UP) <= maxAmount.setScale(2, RoundingMode.HALF_UP)
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

  void cancelConciliationsForBankingTransaction(MovimientosBancarios bankingTransaction) {
    List<Conciliation> conciliations = getConciliationsToApplyForBankingTransaction(bankingTransaction)
    conciliations.each {
      deleteConciliation(it)
    }
  }

  List<Conciliation> getConciliationsAppliedForPayment(Payment payment) {
    Conciliation.findAllByPaymentAndStatus(payment, ConciliationStatus.APPLIED)
  }

  List<Conciliation> getConciliationsAppliedForBankingTransaction(MovimientosBancarios bankingTransaction) {
    Conciliation.findAllByBankingTransactionAndStatus(bankingTransaction, ConciliationStatus.APPLIED)
  }

  void applyConciliationsForPayment(Payment payment) {
    List<Conciliation> conciliations = getConciliationsToApplyForPayment(payment)
    conciliations.each { conciliation ->
      applyConciliation(conciliation)
    }
    paymentService.conciliatePayment(payment)
  }

  void applyConciliationsForBankingTransaction(MovimientosBancarios bankingTransaction, Map params) {
    Map dataPaymentComplement = [:]
    if (params.chkPaymentComplement) {
      bankingTransaction.createPaymentComplement = true
      dataPaymentComplement.paymentWay = params.paymentWay
      dataPaymentComplement.bankId = params.sourceBank
      dataPaymentComplement.sourceAccount = params.sourceAccount
      dataPaymentComplement.company = Company.get(params.companyId)
    }

    List<Conciliation> conciliations = getConciliationsToApplyForBankingTransaction(bankingTransaction)
    conciliations.each { conciliation ->
      applyConciliation(conciliation)
    }

    movimientosBancariosService.conciliateBankingTransaction(bankingTransaction)
    if (bankingTransaction.createPaymentComplement) {
      dataPaymentComplement.conciliations = conciliations
      generatePaymentComplement(bankingTransaction, dataPaymentComplement)
    }
  }

  private def applyConciliation(Conciliation conciliation) {
		if (conciliation.saleOrder) {
    	saleOrderService.addPaymentToSaleOrder(conciliation.saleOrder, conciliation.amount, conciliation.changeType)
		}
		if (conciliation.paymentToPurchase) {
			purchaseOrderService.conciliatePaymentToPurchase(conciliation.paymentToPurchase)
		}
    conciliation.status = ConciliationStatus.APPLIED
    conciliation.save()
  }

  private def generatePaymentComplement(MovimientosBancarios bankingTransaction, Map dataPaymentComplement) {
    log.info "Generating payment complement for bankingTransaction: ${bankingTransaction.id}"
    String paymentComplementUuid = paymentComplementService.generatePaymentComplementForConciliatedBankingTransaction(bankingTransaction, dataPaymentComplement)
    bankingTransaction.paymentComplementUuid = paymentComplementUuid
    bankingTransaction.paymentComplementStatus = PaymentComplementStatus.XML_GENERATED
    bankingTransaction.save()
  }

  void generatePdfForPaymentComplementFromBankingTransaction(MovimientosBancarios bankingTransaction, Map params) {
    Map dataPaymentComplement = [:]
    dataPaymentComplement.paymentWay = params.paymentWay
    dataPaymentComplement.bankId = params.sourceBank
    dataPaymentComplement.sourceAccount = params.sourceAccount
    dataPaymentComplement.company = Company.get(params.companyId)
    dataPaymentComplement.conciliations = Conciliation.findAllByBankingTransactionAndStatus(bankingTransaction, ConciliationStatus.APPLIED)

    paymentComplementService.generatePdfForPaymentComplementWithUuid(bankingTransaction, dataPaymentComplement)
    bankingTransaction.paymentComplementStatus = PaymentComplementStatus.FULL_STAMPED
    bankingTransaction.save()
  }

  void applyConciliationWithoutInvoice(Conciliation conciliation) {
    conciliation.company = conciliation.payment.company
    conciliation.user = springSecurityService.currentUser
    conciliation.status = ConciliationStatus.APPLIED
    log.info "Saving applied conciliation: ${conciliation.dump()}"
    conciliation.save()
    paymentService.conciliatePayment(conciliation.payment)
  }

  void applyConciliationWithoutPayment(Conciliation conciliation) {
    saleOrderService.addPaymentToSaleOrder(conciliation.saleOrder, conciliation.amount, conciliation.changeType)
    conciliation.company = conciliation.saleOrder.company
    conciliation.user = springSecurityService.currentUser
    conciliation.status = ConciliationStatus.APPLIED
    log.info "Saving applied conciliation: ${conciliation.dump()}"
    conciliation.save()
  }

}
