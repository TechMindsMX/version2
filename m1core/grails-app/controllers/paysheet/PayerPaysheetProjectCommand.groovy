package com.modulus.uno.paysheet

import grails.validation.Validateable
import com.modulus.uno.Company

class PayerPaysheetProjectCommand implements Validateable {
  String company
  String schema
  String paysheetProject

  PayerPaysheetProject createPayerPaysheetProject() {
    new PayerPaysheetProject(
      company:Company.get(this.company),
      paymentSchema:PaymentSchema.values().find { it.toString()==this.schema },
      paysheetProject:PaysheetProject.get(this.paysheetProject)
    )
  }
}
