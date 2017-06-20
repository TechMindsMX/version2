package com.modulus.uno

class AccountStatementBankAccountController {

  AccountStatementBankAccountService accountStatementBankAccountService

  def list(BankAccount bankAccount) {
    [bankAccount:bankAccount, accountStatements:accountStatementBankAccountService.listAccountStatementForBankAccount(bankAccount)]
  }
}
