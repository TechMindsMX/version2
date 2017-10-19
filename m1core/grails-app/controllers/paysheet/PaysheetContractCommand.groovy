package com.modulus.uno.paysheet

import grails.validation.Validateable
import com.modulus.uno.BusinessEntity

class PaysheetContractCommand implements Validateable {
  
  String clientId
  String initDate

  static constraints = {
    clientId nullable:false
    initDate nullable:false
  }

  PaysheetContract createPaysheetContract() {
    new PaysheetContract(
      client:BusinessEntity.get(this.clientId),
      initDate:Date.parse("dd/MM/yyyy", this.initDate)
    )
  }
}
