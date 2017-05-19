package com.modulus.uno

class FeeCommand implements MessageCommand {

  String companyId
  BigDecimal amount
  String type
  String transactionId

  CommissionTransaction createCommissionTransaction() {
    new CommissionTransaction(
      type:CommissionType."${this.type}",
      amount:this.amount,
      company:Company.get(this.companyId),
      transaction:this.transactionId ? Transaction.get(this.transactionId) : null
    )
  }
}
