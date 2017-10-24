package com.modulus.uno.paysheet

class PaysheetProject {

  String name
  String description
  BigDecimal integrationFactor
  BigDecimal occupationalRiskRate
  BigDecimal commission

  static belongsTo = [paysheetContract:PaysheetContract]

  static constrains = {
    name nullable:false, blank:false
    description nullable:true, blank:true
    integrationFactor nullable:false, min:0.0
    occupationalRiskRate nullable:false, min:0.0, max:100.0
    commission nullable:false, min:0.0, max:100.0
  }

}
