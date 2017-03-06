package com.modulus.uno

class AccountStatement {
  Company company
  Balance balance
  BigDecimal balanceTransiting
  BigDecimal balanceSubjectToCollection
  Date startDate
  Date endDate
  def transactions
}
