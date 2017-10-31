package com.modulus.uno

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

  private def createUrlToShowFile(def attrs) {
    int lengthFolio = attrs.saleOrder.folio.length()
    String nameFile = lengthFolio > 36 ? "${attrs.saleOrder.folio}" : "${attrs.saleOrder.folio}_${attrs.saleOrder.id}"
    def file = "${nameFile}.${attrs.format}"
    def rfc = "${attrs.saleOrder.company.rfc}"
    def url = grailsApplication.config.modulus.showFactura
    url.replace('#rfc',rfc).replace('#file',file)
  }

  private def createUrlToShowAccuse(def attrs) {
    int lengthFolio = attrs.saleOrder.folio.length()
    String nameFile = lengthFolio > 36 ? "${attrs.saleOrder.folio.substring(0,36)}" : "${attrs.saleOrder.folio}"
    def file = "${nameFile}.${attrs.format}"
    def rfc = "${attrs.saleOrder.company.rfc}"
    def url = grailsApplication.config.modulus.showAccuse
    url.replace('#rfc',rfc).replace('#file',file)
  }

}
