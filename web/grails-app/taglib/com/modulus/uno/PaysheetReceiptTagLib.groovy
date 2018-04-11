package com.modulus.uno

import grails.util.Environment
import com.modulus.uno.paysheet.PaysheetProjectService
import com.modulus.uno.paysheet.PayerPaysheetProject
import com.modulus.uno.paysheet.PaysheetProject
import com.modulus.uno.paysheet.PaymentSchema

class PaysheetReceiptTagLib {

  PaysheetProjectService paysheetProjectService

  static namespace = "modulusuno"
  static defaultEncodeAs = [taglib:'html']
  //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

  def paysheetReceiptUrl = { attrs, body ->
    out << "${grailsApplication.config.modulus.facturacionUrl}${createUrlToShowFile(attrs)}"
  }

  private def createUrlToShowFile(def attrs) {
    String uuid = attrs.schema == PaymentSchema.IMSS ? attrs.employee.paysheetReceiptUuidSA : attrs.employee.paysheetReceiptUuidIAS
    def file = "${uuid}.${attrs.format}"
    PaysheetProject paysheetProject = paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(attrs.employee.paysheet.paysheetContract, attrs.employee.paysheet.prePaysheet.paysheetProject)
    PayerPaysheetProject payer = paysheetProject.payers.find { payer -> payer.paymentSchema == attrs.schema } 
    def rfc = "AAA010101AAA/${payer.company.id}"
    if (Environment.current == Environment.PRODUCTION) {
      rfc = "${payer.company.rfc}/${payer.company.id}"
    }
    def url = grailsApplication.config.modulus.showPaysheetReceipt
    url.replace('#rfc',rfc).replace('#file',file)
  }

}
