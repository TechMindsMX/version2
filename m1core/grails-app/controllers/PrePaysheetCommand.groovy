package com.modulus.uno

import grails.validation.Validateable

class PrePaysheetCommand implements Validateable {

  String companyId
  String paysheetProject
  String paymentPeriod
  String initDatePeriod
  String endDatePeriod
  String accountExecutive

  static constraints = {
    paysheetProject nullable:false, blank:false
    paymentPeriod nullable:false, blank:false
    accountExecutive nullable:false, blank:false
    initDatePeriod nullable:false
    endDatePeriod nullable:false
  }

  PrePaysheet createPrePaysheet() {
    new PrePaysheet(
      paysheetProject:this.paysheetProject,
      paymentPeriod:PaymentPeriod.find { it.toString() == this.paymentPeriod },
      initPeriod:Date.parse("dd-MM-yyyy", this.initDatePeriod),
      endPeriod:Date.parse("dd-MM-yyyy",this.endDatePeriod),
      accountExecutive:this.accountExecutive,
      company:Company.get(this.companyId)
    )
  }

}
