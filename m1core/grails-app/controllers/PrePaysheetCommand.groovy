package com.modulus.uno

import grails.validation.Validateable

class PrePaysheetCommand implements Validateable {

  String companyId
  String paysheetProject
  String paymentPeriod
  Date initDatePeriod
  Date endDatePeriod
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
      initPeriod:this.initDatePeriod,
      endPeriod:this.endDatePeriod,
      accountExecutive:this.accountExecutive,
      company:Company.get(this.companyId)
    )
  }

}
