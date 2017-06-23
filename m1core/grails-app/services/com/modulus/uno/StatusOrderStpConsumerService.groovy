package com.modulus.uno

import grails.transaction.Transactional
import reactor.spring.context.annotation.*

@Consumer
class StatusOrderStpConsumerService {

  TransactionService transactionService

  @Selector('reverseTransaction')
  @Transactional
  void reverseTransaction(StatusOrderStp statusOrderStp) {
    log.info "Init reverse transaction for ${statusOrderStp.dump()}"
    //obtener la transaccion asociada
    Transaction transaction = Transaction.findByKeyTransaction(statusOrderStp.keyTransaction)
    if (transaction) {
      def properties = [:] << transaction.properties
      //genera la transacción inversa
      Transaction reverseTransaction = new Transaction(
        keyTransaction:"${transaction.keyTransaction}-DEV",
        trackingKey:"${transaction.trackingKey}-DEV",
        amount:transaction.amount,
        paymentConcept:"DEVOLUCIÓN",
        keyAccount:transaction.keyAccount,
        referenceNumber:"${transaction.referenceNumber}-DEV",
        transactionType:TransactionType.DEPOSIT,
        transactionStatus:transaction.transactionStatus
      )
      transactionService.saveTransaction(reverseTransaction)
      log.info "Reverse transaction saved: ${reverseTransaction.dump()}"
      //buscar la orden asociada a la transacción
      //si es orden de venta, poner en estatus PAGO_DEVUELTO
      //si es recibo de honorarios, poner en estatus PAGO_DEVUELTO
      //si es un retiro, poner en estatus PAGO_DEVUELTO
    } else {
      log.warn "Not Found transaction to reverse"
    }

    log.info "End reverse transaction"
  }

}
