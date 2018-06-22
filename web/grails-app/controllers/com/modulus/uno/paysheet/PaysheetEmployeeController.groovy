package com.modulus.uno.paysheet

import grails.transaction.Transactional
import com.modulus.uno.User

@Transactional(readOnly = true)
class PaysheetEmployeeController {

  PaysheetEmployeeService paysheetEmployeeService
  PaysheetService paysheetService
  def springSecurityService

  def showPaysheetReceipts(PaysheetEmployee employee) {
    [employee:employee] 
  }

  @Transactional
  def reloadData(PaysheetEmployee paysheetEmployee) {
    paysheetEmployeeService.reloadDataEmployee(paysheetEmployee)
    redirect controller:"paysheet", action:"show", id:paysheetEmployee.paysheet.id
  }

  def choosePaysheetToShowReceiptsForCurrentUser() {
    User user = springSecurityService.currentUser
    List<Paysheet> payedPaysheetsOfUser = paysheetService.getPaysheetsStampedForEmployee(user.username)
    render view:"listPaysheetReceipts", model:[user:user, payedPaysheetsOfUser:payedPaysheetsOfUser]
  }

  def showPaysheetReceiptsOfUserForPaysheet() {
    User user = springSecurityService.currentUser
    Paysheet paysheet = Paysheet.get(params.paysheetId)
    List<Paysheet> payedPaysheetsOfUser = paysheetService.getPaysheetsStampedForEmployee(user.username)
    PaysheetEmployee employee = paysheetEmployeeService.findEmployeeForRfcAndPaysheet(user.username, paysheet)
    log.info "Paysheet employee: ${employee}"
    render view:"listPaysheetReceipts", model:[user:user, payedPaysheetsOfUser:payedPaysheetsOfUser, paysheet:paysheet, employee:employee]
  }

  @Transactional
  def generatePdfForSAPaysheetReceiptEmployee(PaysheetEmployee paysheetEmployee) {
    paysheetEmployeeService.generatePaysheetReceiptPdfIMSS(paysheetEmployee)
    redirect action:"showPaysheetReceipts", id:paysheetEmployee.id
  }
}


