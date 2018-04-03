package com.modulus.uno.paysheet

import com.modulus.uno.BusinessEntity
import com.modulus.uno.FederalEntity

class PaysheetProject {

  String name
  String description
  BigDecimal integrationFactor
  BigDecimal occupationalRiskRate
  BigDecimal commission
  FederalEntity federalEntity = FederalEntity.DIF

  static belongsTo = [paysheetContract:PaysheetContract]
  static hasMany = [payers:PayerPaysheetProject, employees:BusinessEntity, billers:BillerPaysheetProject]

  static constraints = {
    name nullable:false, blank:false
    description nullable:true, blank:true
    integrationFactor nullable:false, min:0.0
    occupationalRiskRate nullable:false, min:0.0, max:100.0
    commission nullable:false, min:0.0, max:100.0
  }

}
