package com.modulus.uno

class CashOutOrder {

  BigDecimal amount
  String timoneUuid
  String timoneAccount
  BankAccount account
  RejectReason rejectReason
  String comments
  String uuid = UUID.randomUUID().toString().replace('-','')[0..15]

  CashOutOrderStatus status = CashOutOrderStatus.CREATED

  Date dateCreated
  Date lastUpdated

  static belongsTo = [company: Company]

  static hasMany = [authorizations:Authorization]

  static constraints = {
    amount nullable:false
    account nullable:false
    timoneAccount nullable:false,blank:false
    timoneUuid nullable:false,blank:false
    rejectReason nullable:true
    comments nullable:true
  }
}
