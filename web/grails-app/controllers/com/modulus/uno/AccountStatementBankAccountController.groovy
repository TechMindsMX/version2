package com.modulus.uno

import grails.transaction.Transactional

class AccountStatementBankAccountController {

  AccountStatementBankAccountService accountStatementBankAccountService

  def list(BankAccount bankAccount) {
    [bankAccount:bankAccount, accountStatements:accountStatementBankAccountService.listAccountStatementForBankAccount(bankAccount), baseUrlDocuments:grailsApplication.config.grails.url.base.images]
  }

  def create(BankAccount bankAccount) {
    respond new AccountStatementBankAccount(bankAccount:bankAccount)
  }

  @Transactional
  def save(AccountStatementBankAccount accountStatementBankAccount) {
    def document = params.pdfDocument
    accountStatementBankAccount = accountStatementBankAccountService.saveAccountStatementForBankAccount(accountStatementBankAccount, document)
    redirect action:"list", id:accountStatementBankAccount.bankAccount.id
  }

}
