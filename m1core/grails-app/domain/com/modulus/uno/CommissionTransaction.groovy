package com.modulus.uno

class CommissionTransaction {

  String uuid = UUID.randomUUID().toString().replace('-','')[0..15]
  CommissionType type
  BigDecimal amount
  CommissionTransactionStatus status = CommissionTransactionStatus.PENDING
  String saleOrderOfCharge
  Company company

  Date dateCreated
  Date lastUpdated

  static constraints = {
    type nullable:false
    amount nullable:false, min:0.01
    company nullable:false
    saleOrderOfCharge nullable:true
  }

}
