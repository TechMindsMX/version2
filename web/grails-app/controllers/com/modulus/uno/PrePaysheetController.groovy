package com.modulus.uno

import grails.transaction.Transactional

@Transactional(readOnly = true)
class PrePaysheetController {

  PrePaysheetService prePaysheetService

  def create() {
    Company company = Company.get(session.company)
    respond new PrePaysheet(), model:[company:company]
  }

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
    respond prePaysheet, model:[employeesAvailableToAdd:employeesAvailableToAdd]
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

    prePaysheetService.addEmployeesToPrePaysheet(prePaysheet, params.entities)
    redirect action:"addEmployees", id:prePaysheet.id
  }
}
