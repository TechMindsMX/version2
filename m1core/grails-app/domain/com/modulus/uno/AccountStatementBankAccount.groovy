package com.modulus.uno

class AccountStatementBankAccount {

  S3Asset document
  Date month

  static belongsTo = [bankAccount:BankAccount]

  Date dateCreated
  Date lastUpdated

}
