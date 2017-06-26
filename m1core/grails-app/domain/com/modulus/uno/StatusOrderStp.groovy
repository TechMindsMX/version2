package com.modulus.uno

class StatusOrderStp {

  String keyTransaction
  String company
  String trackingKey
  String status
  CauseRefundStp causeRefund = CauseRefundStp.DONT_SPECIFIED

  Date dateCreated
  Date lastUpdated

  static constraints = {
    keyTransaction nullable:false
    company nullable:false
    trackingKey nullable:true, blank:true
    status nullable:false
  }

}
