package com.modulus.uno

import grails.validation.Validateable

class PrePaysheetCommand implements Validateable {

  String companyId
  String paysheetProject
  String paymentPeriod
  String accountExecutive

  static constraints = {
    paysheetProject nullable:false, blank:false
    paymentPeriod nullable:false, blank:false
    accountExecutive nullable:false, blank:false
  }

  PrePaysheet createPrePaysheet() {
    new PrePaysheet(
      paysheetProject:this.paysheetProject,
      paymentPeriod:this.paymentPeriod,
      accountExecutive:this.accountExecutive,
      company:Company.get(this.companyId)
    )
  }

}
