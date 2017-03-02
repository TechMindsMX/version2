package com.modulus.uno

class SaleOrderTagLib {

  def restService

  static namespace = "modulusuno"
  static defaultEncodeAs = [taglib:'html']
  //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

  def formatPrice = { attrs, body ->
    out << g.formatNumber(number:attrs.number, type:"currency", maxFractionDigits:attrs.decimals?:"2", locale:"es_MX")
  }

  def invoiceUrl = { attrs, body ->
    out << "${grailsApplication.config.modulus.facturacionUrl}${createUrlToShowFile(attrs)}"
  }

  def invoiceAccuseUrl = { attrs, body ->
    out << "${grailsApplication.config.modulus.facturacionUrl}${createUrlToShowAccuse(attrs)}"
  }

  private def createUrlToShowFile(def attrs) {
    def file = "${attrs.saleOrder.folio}_${attrs.saleOrder.id}.${attrs.format}"
    def rfc = "${attrs.saleOrder.company.rfc}"
    def url = grailsApplication.config.modulus.showFactura
    url.replace('#rfc',rfc).replace('#file',file)
  }

  private def createUrlToShowAccuse(def attrs) {
    def file = "${attrs.saleOrder.folio}.xml"
    def rfc = "${attrs.saleOrder.company.rfc}"
    def url = grailsApplication.config.modulus.showAccuse
    url.replace('#rfc',rfc).replace('#file',file)
  }

}
