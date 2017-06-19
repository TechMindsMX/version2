package com.modulus.uno

import groovy.transform.Sortable

@Sortable(includes = "date")
class AccountStatementTransaction {

  BankAccount account
  Date date
  String concept
  String transactionId
  BigDecimal amount
  TransactionType type
  BigDecimal balance

}
