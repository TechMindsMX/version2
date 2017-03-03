package com.modulus.uno

import wslite.http.HTTPClientException
import groovy.util.logging.Slf4j
import wslite.soap.*

class RequestSOAPService {

  def doRequest(String url, @DelegatesTo(RequestSOAP) Closure cl){
    def request = new RequestSOAP(url)
    def code = cl.rehydrate(request, this, this)
    code.resolveStrategy = Closure.DELEGATE_ONLY
    code()
  }

}

@Slf4j
class RequestSOAP {
  String baseUrl = ""
  String endpointUrl = ""
  Map query = [:]
  Map headers = ["Accept":"application/json; charset=utf-8"]
  String xml = ""

  RequestSOAP(String url){
    this.baseUrl = url
  }

  def baseUrl(String url) { this.baseUrl = url; this }
  def endpointUrl(String e) { this.endpointUrl = e; this }
  def query(q) { this.query = q; this }
  def headers(h) { this.headers = h; this }
  def xml(c) { this.xml = c; this }

  def doit(){
    try{
      def client = new SOAPClient(this.baseUrl)
      def response = client.send(this.xml)
      response
    }catch (HTTPClientException e) {
      log.error e.message
      //handleError(
      //  e:e, method:this.method, baseUrl:this.baseUrl, endpoint:this.endpointUrl, query:this.query)
    }
  }

  //private def handleError(Map params) {
  //  log.error "${params?.e} -- ${params?.e?.message} por ${params?.method}"
  //  log.error "Base URl ${params?.baseUrl}"
  //  log.error "Endpoint: ${params?.endpoint}"
  //  log.error "Query: ${params?.query ?: 'Sin query'}"
  //}
}