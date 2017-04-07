package com.modulus.uno

import grails.transaction.Transactional
import org.springframework.transaction.annotation.Propagation

class TransactionService {

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  Transaction saveTransaction(Transaction transaction) {
    transaction.save()
    transaction
  }

  List<Transaction> getTransactionsAccountForPeriod(keyAccount, startDate, endDate) {
    List<Transaction> transactions = Transaction.findAllByKeyAccountAndDateCreatedBetween(keyAccount,startDate,endDate)
    BigDecimal balance = 0.0
    transactions.sort{ it.dateCreated }.collect{ transaction ->
      balance = transaction.transactionType == TransactionType.WITHDRAW ? (balance - transaction.amount) : (balance + transaction.amount)
      transaction.balance = balance
      transaction
    }
  }

}
