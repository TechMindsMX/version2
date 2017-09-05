package com.modulus.uno

import grails.transaction.Transactional

@Transactional(readOnly = true)
class PrePaysheetController {

  PrePaysheetService prePaysheetService

  def create() {
    Company company = Company.get(session.company)
    respond new PrePaysheet(), model:[company:company]
  }

  @Transactional
  def save(PrePaysheetCommand command) {
    log.info "Saving prePaysheet: ${command.dump()}"
    Company company = Company.get(session.company)
    if (!command) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    if (command.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond command.errors, view:"create", model:[company:company]
      return
    }

    PrePaysheet prePaysheet = command.createPrePaysheet()
    prePaysheetService.savePrePaysheet(prePaysheet)

    if (prePaysheet.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond prePaysheet.errors, view:"create", model:[company:company]
      return
    }

    redirect action:"addEmployees", id:prePaysheet.id
  }

  def addEmployees(PrePaysheet prePaysheet) {
    List employeesAvailableToAdd = prePaysheetService.getEmployeesAvailableToAdd(prePaysheet)
    List dataImssEmployees = prePaysheetService.getDataImssForEmployees(employeesAvailableToAdd)
    List netPaymentEmployees = prePaysheetService.getNetPaymentForEmployees(employeesAvailableToAdd, prePaysheet)
    respond prePaysheet, model:[employeesAvailableToAdd:employeesAvailableToAdd, dataImssEmployees:dataImssEmployees, netPaymentEmployees:netPaymentEmployees]
  }

  def show(PrePaysheet prePaysheet) {
    respond prePaysheet
  }

  def list() {
    params.max = 25
    Company company = Company.get(session.company)
    Map prePaysheets = prePaysheetService.getListAndCountPrePaysheetsForCompany(company, params)
    [prePaysheetList:prePaysheets.list, prePaysheetCount:prePaysheets.total]
  }

  @Transactional
  def saveEmployees(PrePaysheet prePaysheet) {
    log.info "PrePaysheet id: ${prePaysheet.id}"
    log.info "Employees to save: ${params.entities}"

    if (!params.entities) {
      flash.message = "No seleccionó empleados"
      redirect action:"addEmployees", id:prePaysheet.id
      return
    }

    prePaysheetService.addEmployeesToPrePaysheet(prePaysheet, params)
    redirect action:"addEmployees", id:prePaysheet.id
  }

  @Transactional
  def sendToProcess(PrePaysheet prePaysheet) {
    prePaysheetService.sendPrePaysheetToProcess(prePaysheet)
    redirect action:"list"
  }

  def exportToXls(PrePaysheet prePaysheet) {
    log.info "Exporting to Xls the prePaysheet: ${prePaysheet.dump()}"
    def xls = prePaysheetService.exportPrePaysheetToXls(prePaysheet)
    xls.with {
      setResponseHeaders(response, "prenomina-${prePaysheet.company}.xlsx")
      save(response.outputStream)
    }
  }

}
