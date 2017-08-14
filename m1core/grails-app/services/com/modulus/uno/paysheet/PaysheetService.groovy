package com.modulus.uno.paysheet

import grails.transaction.Transactional

class PaysheetService {

  PaysheetEmployeeService paysheetEmployeeService
  PrePaysheetService prePaysheetService

  @Transactional
  Paysheet createPaysheetFromPrePaysheet(PrePaysheet prePaysheet) {
    Paysheet paysheet = new Paysheet(
      prePaysheet:prePaysheet,
      company:prePaysheet.company
    )
    paysheet.save()
    loadEmployeesToPaysheetFromPrePaysheet(paysheet, prePaysheet)
    prePaysheetService.changeStatusToProcessed(prePaysheet)
    paysheet
  }

  Paysheet loadEmployeesToPaysheetFromPrePaysheet(Paysheet paysheet, PrePaysheet prePaysheet) {
    prePaysheet.employees.each { prePaysheetEmployee ->
      PaysheetEmployee paysheetEmployee = paysheetEmployeeService.createPaysheetEmployeeFromPrePaysheetEmployee(paysheet, prePaysheetEmployee)
      paysheet.addToEmployees(paysheetEmployee)
    }
    paysheet.save()
    paysheet
  }

  @Transactional
  Paysheet sendToAuthorize(Paysheet paysheet) {
    paysheet.status = PaysheetStatus.TO_AUTHORIZE
    paysheet.save()
    //TODO: enviar notificación a usuario autorizador de nómina
    paysheet
  }

}
