package com.modulus.uno

import grails.transaction.Transactional
import org.springframework.transaction.annotation.Propagation

class TransactionService {

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  Transaction saveTransaction(Transaction transaction) {
    transaction.save()
    transaction
  }

  BigDecimal getBalanceByKeyAccountPriorToDate(String keyAccount, Date date) {
    def totalDeposits = getTransactionsSumByKeyAccountAndTransactionTypePriorToDate(keyAccount, TransactionType.DEPOSIT, date) ?: new BigDecimal(0)
    def totalWithDrawals = getTransactionsSumByKeyAccountAndTransactionTypePriorToDate(keyAccount, TransactionType.WITHDRAW, date) ?: new BigDecimal(0)
    totalDeposits - totalWithDrawals
  }

  BigDecimal getTransactionsSumByKeyAccountAndTransactionTypePriorToDate(String keyAccount, TransactionType transactionType, Date date) {
    BigDecimal total = Transaction.createCriteria().get {
      and {
        eq("keyAccount", keyAccount)
        eq("transactionType", transactionType)
        ne("transactionStatus", TransactionStatus.REJECTED)
        lt("dateCreated", date)
      }
      projections {
        sum "amount"
      }
    }
    log.info "Total for ${transactionType}: ${total}"
    total
  }

  List<Transaction> getTransactionsAccountForPeriod(keyAccount, startDate, endDate) {
    List<Transaction> transactions = Transaction.findAllByKeyAccountAndDateCreatedBetween(keyAccount,startDate,endDate)
    BigDecimal balance = getBalanceByKeyAccountPriorToDate(keyAccount, startDate)
    transactions.sort{ it.dateCreated }.collect{ transaction ->
      balance = transaction.transactionType == TransactionType.WITHDRAW ? (balance - transaction.amount) : (balance + transaction.amount)
      transaction.balance = balance
      transaction
    }
  }

}
