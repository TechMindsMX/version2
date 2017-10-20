package com.modulus.uno.paysheet

import com.modulus.uno.BusinessEntity
import com.modulus.uno.User
import com.modulus.uno.Company

class PaysheetContract {
  
  Date dateCreated
  Date dateUpdated
  
  BusinessEntity client
  Date initDate
  User executive

  static belongsTo = [company:Company]
  static hasMany = [employees:BusinessEntity, projects:PaysheetProject]

  static constraint = {
    client nullable:false
  }
}
