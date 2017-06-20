package com.modulus.uno

class AccountStatementBankAccountService {

  S3AssetService s3AssetService

  AccountStatementBankAccount saveAccountStatementForBankAccount(AccountStatementBankAccount accountStatement, def document) {
    def asset = s3AssetService.createTempFilesOfMultipartsFiles(document, "accountStatementBankAccount")
    accountStatement.document = asset
    accountStatement.save()
    accountStatement
  }

  List<AccountStatementBankAccount> listAccountStatementForBankAccount(BankAccount bankAccount) {
    AccountStatementBankAccount.findAllByBankAccount(bankAccount,[sort:"month", order:"desc"])
  }

}
