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
    initDatePeriod nullable:false, validator: { val, obj ->
      (obj.endDatePeriod && val.before(obj.endDatePeriod)) ?: false
     }
    endDatePeriod nullable:false, validator: { val, obj ->
      (obj.initDatePeriod && val.after(obj.initDatePeriod)) ?: false
     }
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
