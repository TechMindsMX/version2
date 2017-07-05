package com.modulus.uno

class PaysheetProject {

  String name
  String description
  BigDecimal integrationFactor
  BigDecimal occupationalRiskRate

  static belongsTo = [company:Company]

  static constrains = {
    name nullable:false, blank:false
    description nullable:true, blank:true
    integrationFactor nullable:false, min:0.0
    occupationalRiskRate nullable:false, min:0.0, max:100.0
  }

}
