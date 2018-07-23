package com.modulus.uno.paysheet

import grails.transaction.Transactional

@Transactional(readOnly = true)
class PaysheetEmployeeController {

  PaysheetEmployeeService paysheetEmployeeService

  def showPaysheetReceipts(PaysheetEmployee employee) {
    [employee:employee] 
  }

  @Transactional
  def reloadData(PaysheetEmployee paysheetEmployee) {
    paysheetEmployeeService.reloadDataEmployee(paysheetEmployee)
    redirect controller:"paysheet", action:"show", id:paysheetEmployee.paysheet.id
  }
}


