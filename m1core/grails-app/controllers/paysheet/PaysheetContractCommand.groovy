package com.modulus.uno.paysheet

import grails.validation.Validateable
import com.modulus.uno.BusinessEntity
import com.modulus.uno.User
import com.modulus.uno.Company

class PaysheetContractCommand implements Validateable {
  
  String id
  String clientId
  String initDate
  String executiveId
  String companyId
  String employerRegistration
  String serie
  String folio

  static constraints = {
    id nullable:true
    clientId nullable:false
    initDate nullable:false
    executiveId nullable:false
    companyId nullable:false
    employerRegistration nullable:false, blank:false
    serie nullable:true, blank:true
    folio nullable:false
  }

  PaysheetContract createPaysheetContract() {
    new PaysheetContract(
      client:BusinessEntity.get(this.clientId),
      initDate:Date.parse("dd/MM/yyyy", this.initDate),
      executive:User.get(this.executiveId),
      company:Company.get(this.companyId),
      employerRegistration:this.employerRegistration,
      serie:this.serie,
      folio:this.folio.toInteger()
    )
  }

  PaysheetContract updatePaysheetContract() {
    PaysheetContract paysheetContract = PaysheetContract.get(this.id)
    paysheetContract.client = BusinessEntity.get(this.clientId)
    paysheetContract.initDate = Date.parse("dd/MM/yyyy", this.initDate)
    paysheetContract.executive = User.get(this.executiveId)
    paysheetContract.employerRegistration = this.employerRegistration
    paysheetContract.serie = this.serie
    paysheetContract.folio = this.folio.toInteger()
    paysheetContract
  }

}
