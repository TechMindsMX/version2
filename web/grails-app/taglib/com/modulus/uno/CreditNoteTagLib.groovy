package com.modulus.uno

import grails.util.Environment

class CreditNoteTagLib {

  static namespace = "modulusuno"
  static defaultEncodeAs = [taglib:'html']
  //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

  def creditNoteUrl = { attrs, body ->
    out << "${grailsApplication.config.modulus.facturacionUrl}${createUrlToShowFile(attrs)}"
  }

  private def createUrlToShowFile(def attrs) {
    def file = "${attrs.creditNote.folio}.${attrs.format}"
    def rfc = "AAA010101AAA/${attrs.creditNote.saleOrder.company.id}"
    if (Environment.current == Environment.PRODUCTION) {
      rfc = "${attrs.creditNote.saleOrder.company.rfc}/${attrs.creditNote.saleOrder.company.id}"
    }
    def url = grailsApplication.config.modulus.showFactura
    url.replace('#rfc',rfc).replace('#file',file)
  }

  def cancelAccuseUrl = { attrs, body ->
    out << "${grailsApplication.config.modulus.facturacionUrl}${createUrlToShowAccuse(attrs)}"
  }

  private def createUrlToShowAccuse(def attrs) {
    def file = "${attrs.creditNote.folio.substring(0,36)}.${attrs.format}"
    def rfc = "AAA010101AAA/${attrs.creditNote.saleOrder.company.id}"
    if (Environment.current == Environment.PRODUCTION) {
      rfc = "${attrs.creditNote.saleOrder.company.rfc}/${attrs.creditNote.saleOrder.company.id}"
    }
    def url = grailsApplication.config.modulus.showAccuse
    url.replace('#rfc',rfc).replace('#file',file)
  }


}
