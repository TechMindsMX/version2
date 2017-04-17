package com.modulus.uno

class CommissionsInvoice {

  Company companyEmitter
  Company companyReceiver
  CommissionsInvoiceStatus status = CommissionsInvoiceStatus.CREATED

  static hasMany = [commissions:CommissionTransaction]

  Date dateCreated
  Date lastUpdated

}
