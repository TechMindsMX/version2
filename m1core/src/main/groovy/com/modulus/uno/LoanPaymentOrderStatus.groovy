package com.modulus.uno

enum LoanPaymentOrderStatus {
  CREATED("loanPaymentOrder.created"),
  VALIDATE("loanPaymentOrder.validate"),
  AUTHORIZED("loanPaymentOrder.authorized"),
  REJECTED("loanPaymentOrder.rejected"),
  EXECUTED("loanPaymentOrder.executed"),
  CANCELED("loanPaymentOrder.canceled")

  private final String code

  LoanPaymentOrderStatus(String code){
    this.code = code
  }

  String getCode(){ return this.code }
}
