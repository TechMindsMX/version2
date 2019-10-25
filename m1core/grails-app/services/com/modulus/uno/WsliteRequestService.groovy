package com.modulus.uno

import wslite.http.HTTPClientException
import groovy.util.logging.Slf4j
import wslite.rest.*

class WsliteRequestService {

  def doRequest(String url, @DelegatesTo(Request) Closure cl){
    def request = new Request(url)
    def code = cl.rehydrate(request, this, this)
    code.resolveStrategy = Closure.DELEGATE_ONLY
    code()
  }

}

@Slf4j
class Request {
  String baseUrl = ""
  String endpointUrl = ""
  Map query = [:]
  Map headers = ["Accept":"application/json; charset=utf-8"]
  HTTPMethod method = HTTPMethod.GET
  Closure callback = {}

  Request(String url){
    this.baseUrl = url
  }

  def baseUrl(String url) { this.baseUrl = url; this }
  def endpointUrl(String e) { this.endpointUrl = e; this }
  def query(q) { this.query = q; this }
  def headers(h) { this.headers = h; this }
  def method(m) { this.method = m; this }
  def callback(c) { this.callback = c; this }

  def doit(){
    try{
      def client = new RESTClient(this.baseUrl)
      def response = client."${this.method.code}"([path:this.endpointUrl, query:this.query,
        headers:this.headers], this.callback)
      response
    }catch (HTTPClientException e) {
      handleError(
        e:e, method:this.method, baseUrl:this.baseUrl, endpoint:this.endpointUrl, query:this.query)
      throw new RestException(e.response.contentAsString)
    }
  }

  private def handleError(Map params) {
    log.error "${params?.e} -- ${params?.e?.message} por ${params?.method}"
    log.error "Base URl ${params?.baseUrl}"
    log.error "Endpoint: ${params?.endpoint}"
    log.error "Query: ${params?.query ?: 'Sin query'}"
  }
}

enum HTTPMethod{
  GET("get"),
  POST("post"),
  PUT("put"),
  DELETE("delete")

  private final String code

  HTTPMethod(String code){ this.code = code }

  String getCode(){ return this.code }
}
