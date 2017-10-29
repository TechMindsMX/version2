package com.modulus.uno.paysheet

import com.modulus.uno.Company

class PaysheetController {

  PaysheetService paysheetService
  PaysheetEmployeeService paysheetEmployeeService
  PaysheetContractService paysheetContractService

  def createFromPrePaysheet(PrePaysheet prePaysheet) {
    Paysheet paysheet = paysheetService.createPaysheetFromPrePaysheet(prePaysheet)
    redirect controller:"prePaysheet", action:"list"
  }

  def show(Paysheet paysheet) {
    respond paysheet, model:[chargeBanksAccounts: paysheetService.getBanksAccountsToPaymentDispersion(paysheet), baseUrlDocuments:grailsApplication.config.grails.url.base.images]
  }

  def list() {
    Company company = Company.get(session.company)
    List<PaysheetContract> paysheetContracts = paysheetContractService.getPaysheetContractsWithProjectsOfCompany(company)
    [paysheetContracts:paysheetContracts]
  }

  def listPaysheetsForPaysheetContract() {
    params.max = 25
    PaysheetContract paysheetContract = PaysheetContract.get(params.paysheetContractId)
    List<Paysheet> paysheetList = Paysheet.findAllByPaysheetContract(paysheetContract, params)
    Integer paysheetCount = Paysheet.countByPaysheetContract(paysheetContract)
    render view:"list", model:[client:paysheetContract.client, paysheetList:paysheetList, paysheetCount:paysheetCount]
  }

  def sendToAuthorize(Paysheet paysheet) {
    paysheetService.sendToAuthorize(paysheet)
    redirect action:"list"
  }

  def exportToXls(Paysheet paysheet) {
    log.info "Exporting to Xls the paysheet: ${paysheet.dump()}"
    def xls = paysheetService.exportPaysheetToXls(paysheet)
    xls.with {
      setResponseHeaders(response, "nomina-${paysheet.paysheetContract.client}-${paysheet.prePaysheet.paysheetProject}.xlsx")
      save(response.outputStream)
    }
  }

  def authorize(Paysheet paysheet) {
    paysheetService.authorize(paysheet)
    redirect action:"list"
  }

  def reject(Paysheet paysheet) {
    paysheetService.reject(paysheet)
    redirect action:"list"
  }

  def exportToXlsImss(Paysheet paysheet) {
    log.info "Exporting to Xls only Imss the paysheet: ${paysheet.dump()}"
    def xls = paysheetService.exportPaysheetToXlsImss(paysheet)
    xls.with {
      setResponseHeaders(response, "nominaIMSS-${paysheet.paysheetContract.client}-${paysheet.prePaysheet.paysheetProject}.xlsx")
      save(response.outputStream)
    }
  }

  def exportToXlsAssimilable(Paysheet paysheet) {
    log.info "Exporting to Xls only assimilable the paysheet: ${paysheet.dump()}"
    def xls = paysheetService.exportPaysheetToXlsAssimilable(paysheet)
    xls.with {
      setResponseHeaders(response, "nominaAsimilables-${paysheet.paysheetContract.client}-${paysheet.prePaysheet.paysheetProject}.xlsx")
      save(response.outputStream)
    }
  }

  def generatePaymentDispersion(Paysheet paysheet) {
    log.info "Generating txt payments dispersion charge bank account ${params.chargeBankAccountsIds} from paysheet ${paysheet.id}"
    paysheetService.generateDispersionFilesFromPaysheet(paysheet, params)
		redirect action:"show", id:paysheet.id
  }

  def exportToXlsCash(Paysheet paysheet) {
    log.info "Exporting to Xls only Cash the paysheet: ${paysheet.dump()}"
    def xls = paysheetService.exportPaysheetToXlsCash(paysheet)
    xls.with {
      setResponseHeaders(response, "nominaEfectivo-${paysheet.paysheetContract.client}-${paysheet.prePaysheet.paysheetProject}.xlsx")
      save(response.outputStream)
    }
  }

	def changePaymentWayFromEmployee(PaysheetEmployee employee) {
		paysheetEmployeeService.changePaymentWayFromEmployee(employee)
		redirect action:"show", id:employee.paysheet.id
	}
}
