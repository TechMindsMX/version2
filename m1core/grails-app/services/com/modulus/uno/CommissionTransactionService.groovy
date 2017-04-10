package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class CommissionTransactionService {

  def saveCommissionTransaction (FeeCommand feeCommand) {
    CommissionTransaction commissionTransaction = feeCommand.createCommissionTransaction()
    commissionTransaction.save()
    commissionTransaction
  }
}

