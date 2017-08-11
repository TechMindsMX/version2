package com.modulus.uno.paysheet

class PaysheetController {

  PaysheetService paysheetService

  def createFromPrePaysheet(PrePaysheet prePaysheet) {
    Paysheet paysheet = paysheetService.createPaysheetFromPrePaysheet(prePaysheet)
    redirect action:"show", id:paysheet.id
  }

  def show(Paysheet paysheet) {
    respond paysheet
  }

}
