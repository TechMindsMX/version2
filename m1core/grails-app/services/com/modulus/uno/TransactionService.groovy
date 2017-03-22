package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class TransactionService {

  Transaction saveTransaction(Transaction transaction) {
    transaction.save()
    transaction
  }

}
