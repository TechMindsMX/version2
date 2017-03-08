package com.modulus.uno

class LoanOrder {

  BigDecimal amount = new BigDecimal(0)
  LoanOrderStatus status = LoanOrderStatus.CREATED
  BigDecimal rate = new BigDecimal(0)
  Company creditor
  RejectReason rejectReason
  Integer term = new Integer(12)
  String rejectComment

  static belongsTo = [company:Company]
  static hasMany = [
    authorizations: Authorization,
    documents:S3Asset,
    payments: LoanPaymentOrder
  ]

  Date dateCreated
  Date lastUpdated
  Date loanDate

  static constraints = {
    amount min:new BigDecimal(0)
    term min:1
    rate min:new BigDecimal(0)
    creditor nullable:true
    rejectReason nullable:true
    rejectComment nullable:true, blank:true
    loanDate nullable:true
  }

}
