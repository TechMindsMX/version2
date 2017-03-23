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
  def xml = ""

  RequestSOAP(String url){
    this.baseUrl = url
  }

  def baseUrl(String url) { this.baseUrl = url; this }
  def xml(c) { this.xml = c; this }

  def doit(){
    try{
      def client = new SOAPClient(this.baseUrl)
      def response = client.send(this.xml)
      response
    }catch (HTTPClientException e) {
      log.debug this.baseUrl
      log.error e.message
    }
  }

}