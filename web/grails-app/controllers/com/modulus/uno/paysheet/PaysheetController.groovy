package com.modulus.uno.paysheet

import com.modulus.uno.Company

class PaysheetController {

  PaysheetService paysheetService

  def createFromPrePaysheet(PrePaysheet prePaysheet) {
    Paysheet paysheet = paysheetService.createPaysheetFromPrePaysheet(prePaysheet)
    redirect action:"show", id:paysheet.id
  }

  def show(Paysheet paysheet) {
    respond paysheet, model:[baseUrlDocuments:grailsApplication.config.grails.url.base.images]
  }

  def list() {
    params.max = 25
    Company company = Company.get(session.company)
    List<Paysheet> paysheetList = Paysheet.findAllByCompany(company, params)
    Integer paysheetCount = Paysheet.countByCompany(company)
    [paysheetList:paysheetList, paysheetCount:paysheetCount]
  }

  def sendToAuthorize(Paysheet paysheet) {
    paysheetService.sendToAuthorize(paysheet)
    redirect action:"list"
  }

  def exportToXls(Paysheet paysheet) {
    log.info "Exporting to Xls the paysheet: ${paysheet.dump()}"
    def xls = paysheetService.exportPaysheetToXls(paysheet)
    xls.with {
      setResponseHeaders(response, "nomina-${paysheet.company}-${paysheet.prePaysheet.paysheetProject}.xlsx")
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
      setResponseHeaders(response, "nominaIMSS-${paysheet.company}-${paysheet.prePaysheet.paysheetProject}.xlsx")
      save(response.outputStream)
    }
  }

  def exportToXlsAssimilable(Paysheet paysheet) {
    log.info "Exporting to Xls only assimilable the paysheet: ${paysheet.dump()}"
    def xls = paysheetService.exportPaysheetToXlsAssimilable(paysheet)
    xls.with {
      setResponseHeaders(response, "nominaAsimilables-${paysheet.company}-${paysheet.prePaysheet.paysheetProject}.xlsx")
      save(response.outputStream)
    }
  }

	def prepareDispersion(Paysheet paysheet){
		log.info "Preparing summary for dispersion from paysheet: ${paysheet.id}"
		List dispersionSummary = paysheetService.prepareSummaryDispersion(paysheet)
		render view:"show", model:[paysheet:paysheet, dispersionSummary:dispersionSummary]
	}

  def generatePaymentDispersion(Paysheet paysheet) {
    log.info "Generating txt payments dispersion charge bank account ${params.chargeBankAccountsIds} from paysheet ${paysheet.id}"
    paysheetService.generateDispersionFilesFromPaysheet(paysheet, params)
		redirect action:"show", id:paysheet.id
  }
}
