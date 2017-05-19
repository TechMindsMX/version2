package com.modulus.uno

class AccountStatement {
  Company company
  Balance balance
  BigDecimal balanceTransiting
  BigDecimal balanceSubjectToCollection
  List commissionsBalance
  Date startDate
  Date endDate
  def transactions
}
