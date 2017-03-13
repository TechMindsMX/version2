package com.modulus.uno

class LoanPaymentOrder {

  BigDecimal amountInterest
  BigDecimal amountIvaInterest
  BigDecimal amountToCapital
  Date datePayment = new Date()
  Integer daysPeriod
  LoanPaymentOrderStatus status = LoanPaymentOrderStatus.CREATED
  RejectReason rejectReason
  String rejectComment

  Date dateCreated
  Date lastUpdated

  static belongsTo = [company:Company, loanOrder:LoanOrder]
  static hasMany = [authorizations:Authorization]

  static constraints = {
    amountToCapital min:new BigDecimal(0)
    rejectReason nullable:true
    rejectComment nullable:true, blank:true
  }

  BigDecimal getTotalPayment(){
    this.amountToCapital + this.amountInterest + this.amountIvaInterest
  }

}
