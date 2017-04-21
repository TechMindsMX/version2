package com.modulus.uno

class CommissionsInvoiceTagLib {

  static namespace = "modulusuno"
  static defaultEncodeAs = [taglib:'html']

  def commissionsInvoiceUrl = { attrs, body ->
    out << "${grailsApplication.config.modulus.facturacionUrl}${createUrlToShowFile(attrs)}"
  }

  private def createUrlToShowFile(def attrs) {
    String nameFile = "${attrs.invoice.folioSat}"
    def file = "${nameFile}.${attrs.format}"
    def rfc = "${grailsApplication.config.m1emitter.rfc}"
    def url = grailsApplication.config.modulus.showFactura
    url.replace('#rfc',rfc).replace('#file',file)
  }

}
