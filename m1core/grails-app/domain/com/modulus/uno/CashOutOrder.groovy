package com.modulus.uno

class CashOutOrder {

  BigDecimal amount
  BankAccount account
  RejectReason rejectReason
  String comments
  String uuid = UUID.randomUUID().toString().replace('-','')[0..15]

  Transaction transaction

  CashOutOrderStatus status = CashOutOrderStatus.CREATED

  Date dateCreated
  Date lastUpdated

  static belongsTo = [company: Company]

  static hasMany = [authorizations:Authorization]

  static constraints = {
    amount nullable:false
    account nullable:false
    rejectReason nullable:true
    comments nullable:true
    transaction nullable:true
  }
}
