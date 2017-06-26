package com.modulus.uno

import grails.transaction.Transactional
import org.springframework.context.i18n.LocaleContextHolder as LCH
import reactor.spring.context.annotation.*

@Consumer
class StatusOrderStpConsumerService {

  def messageSource
  TransactionService transactionService
  PurchaseOrderService purchaseOrderService
  FeesReceiptService feesReceiptService
  CashOutOrderService cashOutOrderService

  @Selector('reverseTransaction')
  @Transactional
  void reverseTransaction(StatusOrderStp statusOrderStp) {
    log.info "Init reverse transaction for ${statusOrderStp.dump()}"
    Transaction transaction = Transaction.findByKeyTransaction(statusOrderStp.keyTransaction)
    if (transaction) {
      Transaction reverseTransaction = createAndSaveReverseTransactionFromOriginalTransaction(transaction, statusOrderStp.causeRefund)
      changeStatusOrderLinkedToTransactionToRefundPaymentStatus(transaction)
    } else {
      log.warn "Not Found transaction to reverse"
    }

    log.info "End reverse transaction"
  }

  private Transaction createAndSaveReverseTransactionFromOriginalTransaction(Transaction transaction, CauseRefundStp causeRefund) {
    Transaction reverseTransaction = new Transaction(
      keyTransaction:"${transaction.keyTransaction}-DEV",
      trackingKey:"${transaction.trackingKey}-DEV",
      amount:transaction.amount,
      paymentConcept:"DEVOLUCIÓN, Causa: "+messageSource.getMessage("stp.cause.refund.${causeRefund}", null, LCH.getLocale()),
      keyAccount:transaction.keyAccount,
      referenceNumber:"${transaction.referenceNumber}-DEV",
      transactionType:TransactionType.DEPOSIT,
      transactionStatus:transaction.transactionStatus
    )
    transactionService.saveTransaction(reverseTransaction)
    log.info "Reverse transaction saved: ${reverseTransaction.dump()}"
  }

  private void changeStatusOrderLinkedToTransactionToRefundPaymentStatus(Transaction transaction) {
    purchaseOrderService.reversePaymentPurchaseForTransaction(transaction)
    feesReceiptService.reverseFeesReceiptForTransaction(transaction)
    cashOutOrderService.reverseCashOutForTransaction(transaction)
  }

}
