package com.modulus.uno

enum LoanOrderStatus {
  CREATED("loanorder.created"),
  VALIDATE("loanorder.validate"),
  AUTHORIZED("loanorder.authorized"),
  REJECTED("loanorder.rejected"),
  EXECUTED("loanorder.executed"),
  APPROVED("loanorder.approved"),
  ACCEPTED("loanorder.accepted"),
  CANCELED("loanorder.canceled")

  private final String code

  LoanOrderStatus(String code){
    this.code = code
  }

  String getCode(){ return this.code }
}
