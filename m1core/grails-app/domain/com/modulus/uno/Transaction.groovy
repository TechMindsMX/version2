package com.modulus.uno

class Transaction {

  String uuid = UUID.randomUUID().toString().replace('-','')[0..15]
  String keyTransaction
  String trackingKey
  BigDecimal amount
  String paymentConcept
  String keyAccount
  String referenceNumber
  TransactionType transactionType
  TransactionStatus transactionStatus
  BigDecimal balance

  Date dateCreated
  Date lastUpdated

  static transients = ['balance']

  static constraints = {
    keyTransaction()
    trackingKey()
    amount()
    paymentConcept()
    keyAccount()
    referenceNumber()
    dateCreated()
    lastUpdated()
    transactionType()
    transactionStatus()
  }

}
