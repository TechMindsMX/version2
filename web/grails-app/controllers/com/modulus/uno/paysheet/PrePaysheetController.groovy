package com.modulus.uno.paysheet

import grails.transaction.Transactional

import com.modulus.uno.Company

@Transactional(readOnly = true)
class PrePaysheetController {

  PrePaysheetService prePaysheetService
  PaysheetContractService paysheetContractService

  def create() {
    Company company = Company.get(session.company)
    List<PaysheetContract> paysheetContracts = paysheetContractService.getPaysheetContractsWithProjectsOfCompany(company)
    respond new PrePaysheet(), model:[paysheetContracts:paysheetContracts]
  }

  def choosePaysheetContract(){
    PaysheetContract paysheetContract = PaysheetContract.get(params.paysheetContractId)
    render view:"create", model:[prePaysheet:new PrePaysheet(paysheetContract:paysheetContract)]
  }

  @Transactional
  def save(PrePaysheetCommand command) {
    log.info "Saving prePaysheet: ${command.dump()}"

    if (!command) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    PaysheetContract paysheetContract = PaysheetContract.get(command.contractId)
    if (command.hasErrors()) {
      transactionStatus.setRollbackOnly()
      log.info "Contract ${paysheetContract.dump()}"
      log.info "Executive: ${paysheetContract.executive.name}"
      log.info "Employees: ${paysheetContract.projects.employees}"
      log.info "Payers: ${paysheetContract.projects.payers}"
      log.info "Error: ${command.errors}"
      render view:"create", model:[prePaysheet:new PrePaysheet(paysheetContract:paysheetContract), prePaysheetCommand: command]
      return
    }

    PrePaysheet prePaysheet = command.createPrePaysheet()
    prePaysheetService.savePrePaysheet(prePaysheet)

    if (prePaysheet.hasErrors()) {
      log.info "Error prepaysheet"
      transactionStatus.setRollbackOnly()
      respond prePaysheet.errors, view:"create", model:[prePaysheet:new PrePaysheet(paysheetContract:paysheetContract)]
      return
    }

    redirect action:"addEmployees", id:prePaysheet.id
  }

  def addEmployees(PrePaysheet prePaysheet) {
    List employeesAvailableToAdd = prePaysheetService.getEmployeesAvailableToAdd(prePaysheet)
    List dataImssEmployees = prePaysheetService.getDataImssForEmployees(employeesAvailableToAdd, prePaysheet.paysheetContract.company)
    List netPaymentEmployees = prePaysheetService.getNetPaymentForEmployees(employeesAvailableToAdd, prePaysheet)
    respond prePaysheet, model:[employeesAvailableToAdd:employeesAvailableToAdd, dataImssEmployees:dataImssEmployees, netPaymentEmployees:netPaymentEmployees]
  }

  def show(PrePaysheet prePaysheet) {
    respond prePaysheet
  }

  def list() {
    Company company = Company.get(session.company)
    List<PaysheetContract> paysheetContracts = paysheetContractService.getPaysheetContractsWithProjectsOfCompany(company)
    [paysheetContracts:paysheetContracts]
  }

  def listPrePaysheetsForPaysheetContract() {
    params.max = 25
    PaysheetContract paysheetContract = PaysheetContract.get(params.paysheetContractId)
    Map prePaysheets = prePaysheetService.getListAndCountPrePaysheetsForPaysheetContract(paysheetContract, params)
    render view:"list", model:[paysheetContract:paysheetContract, prePaysheetList:prePaysheets.list, prePaysheetCount:prePaysheets.total]
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
      setResponseHeaders(response, "prenomina-${prePaysheet.paysheetContract.client}.xlsx")
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

	def importXlsPrePaysheet(PrePaysheet prePaysheet) {
		log.info "Importing data from xls prepaysheet"
    def file = request.getFile('prePaysheetXlsFile')
		Map importResults = prePaysheetService.processXlsPrePaysheet(file, prePaysheet)
		log.info "Import results: ${importResults}"
		render view:"show", model:[prePaysheet:prePaysheet, importResults:importResults]
	}
	
  def downloadLayout() {
    log.info "Downloading layout for import pre-paysheet"
    def layout = prePaysheetService.createLayoutForPrePaysheet()
    layout.with {
      setResponseHeaders(response, "layoutPrenomina.xlsx")
      save(response.outputStream)
    }
  }

  def delete(PrePaysheet prePaysheet) {
    log.info "Deleting prePaysheet: ${prePaysheet.id}"
    prePaysheetService.deletePrePaysheet(prePaysheet)
    redirect action:"list"
  }

}
