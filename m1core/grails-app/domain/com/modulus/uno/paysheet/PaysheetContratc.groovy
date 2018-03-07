package com.modulus.uno.paysheet

import com.modulus.uno.BusinessEntity
import com.modulus.uno.User
import com.modulus.uno.Company

class PaysheetContract {
  
  Date dateCreated
  Date lastUpdated
  
  BusinessEntity client
  Date initDate
  User executive
  String employerRegistration

  static belongsTo = [company:Company]
  static hasMany = [employees:BusinessEntity, projects:PaysheetProject, users:User]

  static constraint = {
    client nullable:false
  }
}
