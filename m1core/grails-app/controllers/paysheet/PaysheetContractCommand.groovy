package com.modulus.uno.paysheet

import grails.validation.Validateable
import com.modulus.uno.BusinessEntity
import com.modulus.uno.User

class PaysheetContractCommand implements Validateable {
  
  String clientId
  String initDate
  String executiveId

  static constraints = {
    clientId nullable:false
    initDate nullable:false
    executiveId nullable:false
  }

  PaysheetContract createPaysheetContract() {
    new PaysheetContract(
      client:BusinessEntity.get(this.clientId),
      initDate:Date.parse("dd/MM/yyyy", this.initDate),
      executive:User.get(this.executiveId)
    )
  }
}
