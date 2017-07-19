package com.modulus.uno

class AccountStatementBankAccountService {

  S3AssetService s3AssetService

  AccountStatementBankAccount saveAccountStatementForBankAccount(AccountStatementBankAccount accountStatement, def document) {
    validateAccountStatementBankAccount(accountStatement)
    def asset = s3AssetService.createTempFilesOfMultipartsFiles(document, "accountStatementBankAccount")
    accountStatement.document = asset
    accountStatement.save()
    accountStatement
  }

  private void validateAccountStatementBankAccount(AccountStatementBankAccount accountStatement) {
    if (AccountStatementBankAccount.findByMonth(accountStatement.month)) {
      throw new BusinessException("Ya existe un estado de cuenta para el mes seleccionado")
    }

    Date current = new Date().parse("MM-yyyy", new Date().format("MM-yyyy"))
    if (accountStatement.month > current) {
      throw new BusinessException("El mes/año seleccionados son posteriores a la fecha actual")
    }

  }

  List<AccountStatementBankAccount> listAccountStatementForBankAccount(BankAccount bankAccount) {
    AccountStatementBankAccount.findAllByBankAccount(bankAccount,[sort:"month", order:"desc"])
  }

}
