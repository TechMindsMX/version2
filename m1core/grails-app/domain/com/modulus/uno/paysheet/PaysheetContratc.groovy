package com.modulus.uno.paysheet

class PaysheetContract {
  
  Date dateCreated
  Date dateUpdated
  
  BusinessEntity client
  Date initDate

  static hasMany = [employees:BusinessEntity, projects:PaysheetProject]

  static constraint = {
    client nullable:false
  }
}
