package com.modulus.uno.paysheet

import com.modulus.uno.Company

class BillerPaysheetProject {
  
  Company company
  PaymentSchema paymentSchema

  static belongsTo = [paysheetProject:PaysheetProject]

  Date dateCreated
  Date lastUpdated
}
