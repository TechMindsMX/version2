package com.modulus.uno

import grails.util.Environment

class SaleOrderTagLib {

  def restService

  static namespace = "modulusuno"
  static defaultEncodeAs = [taglib:'html']
  //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

  def invoiceUrl = { attrs, body ->
    out << "${grailsApplication.config.modulus.facturacionUrl}${createUrlToShowFile(attrs)}"
  }

  def invoiceAccuseUrl = { attrs, body ->
    out << "${grailsApplication.config.modulus.facturacionUrl}${createUrlToShowAccuse(attrs)}"
  }

  def paymentComplementUrl = { attrs, body ->
    out << "${grailsApplication.config.modulus.facturacionUrl}${createUrlToShowPaymentComplement(attrs)}"
  }

  private def createUrlToShowFile(def attrs) {
    int lengthFolio = attrs.saleOrder.folio.length()
    String nameFile = lengthFolio > 36 ? "${attrs.saleOrder.folio}" : "${attrs.saleOrder.folio}_${attrs.saleOrder.id}"
    def file = "${nameFile}.${attrs.format}"
    def rfc = "AAA010101AAA/${attrs.saleOrder.company.id}"
    if (Environment.current == Environment.PRODUCTION) {
      rfc = "${attrs.saleOrder.company.rfc}/${attrs.saleOrder.company.id}"
    }
    def url = grailsApplication.config.modulus.showFactura
    url.replace('#rfc',rfc).replace('#file',file)
  }

  private def createUrlToShowAccuse(def attrs) {
    int lengthFolio = attrs.saleOrder.folio.length()
    String nameFile = lengthFolio > 36 ? "${attrs.saleOrder.folio.substring(0,36)}" : "${attrs.saleOrder.folio}"
    def file = "${nameFile}.${attrs.format}"
    def rfc = "AAA010101AAA/${attrs.saleOrder.company.id}"
    if (Environment.current == Environment.PRODUCTION) {
      rfc = "${attrs.saleOrder.company.rfc}/${attrs.saleOrder.company.id}"
    }
    def url = grailsApplication.config.modulus.showAccuse
    url.replace('#rfc',rfc).replace('#file',file)
  }

  private def createUrlToShowPaymentComplement(def attrs) {
    String nameFile = attrs.bankingTransaction.paymentComplementUuid
    def file = "${nameFile}.${attrs.format}"
    def rfc = "AAA010101AAA/${attrs.company.id}"
    if (Environment.current == Environment.PRODUCTION) {
      rfc = "${attrs.company.rfc}/${attrs.company.id}"
    }
    def url = grailsApplication.config.modulus.showPaymentComplement
    url.replace('#rfc',rfc).replace('#file',file)
  }

}
