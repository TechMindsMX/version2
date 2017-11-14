package com.modulus.uno

import grails.validation.Validateable

class BankAccountCommand implements Validateable {
  String accountNumber
  String branchNumber
  String clabe
  String bank
  String cardNumber
  boolean concentradora
	String clientNumber

  BankAccount createBankAccount(){
    String accountNumberFull = this.accountNumber ? this.accountNumber.padLeft(11,"0") : ""
    new BankAccount(
      accountNumber:accountNumberFull,
      branchNumber:this.branchNumber,
      clabe:this.clabe ?: "",
      concentradora: this.concentradora,
      cardNumber: this.cardNumber ?: "",
      clientNumber: this.clientNumber,
      banco:Bank.findByBankingCode(this.bank)
    )
  }
}
