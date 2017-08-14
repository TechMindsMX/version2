package com.modulus.uno.paysheet

import com.modulus.uno.Company

class PaysheetController {

  PaysheetService paysheetService

  def createFromPrePaysheet(PrePaysheet prePaysheet) {
    Paysheet paysheet = paysheetService.createPaysheetFromPrePaysheet(prePaysheet)
    redirect action:"show", id:paysheet.id
  }

  def show(Paysheet paysheet) {
    respond paysheet
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
      setResponseHeaders(response, "nomina-${paysheet.company}.xlsx")
      save(response.outputStream)
    }
  }

}
