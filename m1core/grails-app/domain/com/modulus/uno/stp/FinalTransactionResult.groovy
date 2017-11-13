package com.modulus.uno.stp

import com.modulus.uno.Company

class FinalTransactionResult {

  Company company
  Date transactionDate
  String comment
  FinalTransactionResultStatus status
  ExecutionMode executionMode

  Date dateCreated
  Date lastUpdated

  static constraints = {
    comment nullable:true
  }
}
