package com.modulus.uno.stp

import grails.transaction.Transactional

class FinalTransactionResultService {
  
  @Transactional
  def createFinalTransactionResult(Map dataResult){
    ExecutionMode executionMode = getExecutionMode(dataResult.dateTransaction)
    FinalTransactionResult result = new FinalTransactionResult(
      company:dataResult.company,
      transactionDate:dataResult.dateTransaction,
      comment:dataResult.comment,
      status:dataResult.status,
      executionMode:executionMode
    )
    result.save()
    log.info "Final Transaction Result saved: ${result.dump()}"
    result
  }

  private ExecutionMode getExecutionMode(Date dateTransaction) {
    new Date().format("ddMMyyyy") == dateTransaction.format("ddMMyyyy") ? ExecutionMode.AUTOMATIC : ExecutionMode.MANUAL
  }

  List<FinalTransactionResult> getFinalTransactionResultsAtDate(Date date) {
    FinalTransactionResult.findAllByTransactionDate(date)
  }

}
