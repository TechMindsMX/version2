package com.modulus.uno.paysheet

import grails.transaction.Transactional

import com.modulus.uno.Company

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

  def deleteEmployee(PrePaysheetEmployee prePaysheetEmployee) {
    log.info "Deleting employee from prepaysheet: ${prePaysheetEmployee}"
    PrePaysheet prePaysheet = prePaysheetEmployee.prePaysheet
    prePaysheetService.deleteEmployeeFromPrePaysheet(prePaysheetEmployee)
    redirect action:"show", id:prePaysheet.id
  }

  def incidencesFromEmployee(PrePaysheetEmployee prePaysheetEmployee) {
    respond prePaysheetEmployee
  }

  @Transactional
  def addIncidence(PrePaysheetEmployeeIncidenceCommand incidenceCommand) {
    log.info "Incidence command: ${incidenceCommand.dump()}"
    if (incidenceCommand.hasErrors()) {
      transactionStatus.setRollbackOnly()
      redirect action:"incidencesFromEmployee", id:incidenceCommand.prePaysheetEmployeeId
      return
    }

    PrePaysheetEmployeeIncidence incidence = incidenceCommand.createPrePaysheetEmployeeIncidence()
    log.info "Incidence to save: ${incidence.dump()}"
    prePaysheetService.saveIncidence(incidence)

    if (incidence.hasErrors()) {
      log.info "Error saving incidence: ${incidence.dump()}"
      transactionStatus.setRollbackOnly()
      redirect action:"incidencesFromEmployee", id:incidenceCommand.prePaysheetEmployeeId
      return
    }

    redirect action:"incidencesFromEmployee", id:incidence.prePaysheetEmployee.id
  }

  def deleteIncidence(PrePaysheetEmployeeIncidence incidence) {
    log.info "Deleting incidence from employee: ${incidence.dump()}"
    PrePaysheetEmployee prePaysheetEmployee = incidence.prePaysheetEmployee
    prePaysheetService.deleteIncidenceFromPrePaysheetEmployee(incidence)
    redirect action:"incidencesFromEmployee", id:prePaysheetEmployee.id
  }

	def importPrePaysheet() {
		Company company = Company.get(session.company)
		render view:"import", model:[company:company]
	}

}
