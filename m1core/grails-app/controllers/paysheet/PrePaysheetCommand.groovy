package com.modulus.uno.paysheet

import grails.validation.Validateable
import com.modulus.uno.PaymentPeriod

class PrePaysheetCommand implements Validateable {

  String contractId
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
      paysheetContract:PaysheetContract.get(this.contractId)
    )
  }

}
