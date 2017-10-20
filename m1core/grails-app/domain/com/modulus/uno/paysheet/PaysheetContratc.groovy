package com.modulus.uno.paysheet

import com.modulus.uno.BusinessEntity
import com.modulus.uno.User

class PaysheetContract {
  
  Date dateCreated
  Date dateUpdated
  
  BusinessEntity client
  Date initDate
  User executive

  static hasMany = [employees:BusinessEntity, projects:PaysheetProject]

  static constraint = {
    client nullable:false
  }
}
