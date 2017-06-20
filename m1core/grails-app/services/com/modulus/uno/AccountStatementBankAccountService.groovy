package com.modulus.uno

class AccountStatementBankAccountService {

  S3AssetService s3AssetService

  AccountStatementBankAccount saveAccountStatementForBankAccount(AccountStatementBankAccount accountStatement, def document) {
    if (AccountStatementBankAccount.findByMonth(accountStatement.month)) {
      throw new BusinessException("Ya existe un estado de cuenta para el mes seleccionado")
    }

    def asset = s3AssetService.createTempFilesOfMultipartsFiles(document, "accountStatementBankAccount")
    accountStatement.document = asset
    accountStatement.save()
    accountStatement
  }

  List<AccountStatementBankAccount> listAccountStatementForBankAccount(BankAccount bankAccount) {
    AccountStatementBankAccount.findAllByBankAccount(bankAccount,[sort:"month", order:"desc"])
  }

}
