package com.modulus.uno.paysheet

import com.modulus.uno.User
import com.modulus.uno.BusinessEntity

class UserEmployee {

  User user
  BusinessEntity businessEntity
  
  static belongsTo = [paysheetProject:PaysheetProject]

}
