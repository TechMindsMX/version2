package com.modulus.uno.paysheet

import com.modulus.uno.Company

class PayerPaysheetProject {
  
  Company company
  PaymentSchema schema

  static belongsTo = [paysheetProject:PaysheetProject]

  Date dateCreated
  Date lastUpdated
}
