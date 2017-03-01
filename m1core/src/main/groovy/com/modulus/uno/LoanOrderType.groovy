package com.modulus.uno

enum LoanOrderType {
  DEBTOR("loanorder.debtor"),
  CREDITOR("loanorder.creditor")

  private final String code

  LoanOrderType(String code){
    this.code = code
  }

  String getCode(){ return this.code }
}
