package com.modulus.uno

class CommissionsInvoice {

  CommissionsInvoiceEmitter emitter
  Company companyReceiver
  CommissionsInvoiceStatus status

  static hasMany = [items:CommissionTransaction, payments:CommissionsInvoicePayment]

  Date dateCreated
  Date lastUpdated

}
