package com.modulus.uno

class CommissionsInvoice {

  Company receiver
  CommissionsInvoiceStatus status = CommissionsInvoiceStatus.CREATED

  static hasMany = [commissions:CommissionTransaction]

  Date dateCreated
  Date lastUpdated

}
