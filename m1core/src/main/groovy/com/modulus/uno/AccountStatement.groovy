package com.modulus.uno

class AccountStatement {
  Company company
  Balance balance
  BigDecimal balanceTransiting
  BigDecimal balanceSubjectToCollection
  List commissionsBalance
  Period period
  List<AccountStatementTransaction> transactions
}
