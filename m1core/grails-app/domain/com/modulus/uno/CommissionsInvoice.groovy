package com.modulus.uno

class CommissionsInvoice {

  Company receiver
  CommissionsInvoiceStatus status = CommissionsInvoiceStatus.CREATED
  String folioSat

  static hasMany = [commissions:CommissionTransaction]

  Date dateCreated
  Date lastUpdated

  static constraints = {
    folioSat nullable:true
  }
}
