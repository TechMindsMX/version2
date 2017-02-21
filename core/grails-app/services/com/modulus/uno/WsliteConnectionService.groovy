package com.modulus.uno

import wslite.http.HTTPClientException
import wslite.rest.*

class WsliteConnectionService {

  def get(String baseUrl, String endpoint, Map query = [:]){
    try{
      def client = new RESTClient(baseUrl)
      def response = client.get(path:endpoint, query:query,
      headers:["Accept":"application/json; charset=utf-8"])
      response.json
    }catch (HTTPClientException e) {
      handleError(
        e:e, method:"GET", baseUrl:baseUrl, endpoint:endpoint, query:query)
    }
  }

  def post(String baseUrl, String endpoint, Map query = [:], Closure callback = {}){
    try{
      def client = new RESTClient(baseUrl)
      def response = client.post([path:endpoint,query:query, headers:["Accept":"application/json; charset=utf-8"]], callback)
      response.json
    }catch (HTTPClientException e) {
      handleError(
        e:e, method:"POST", baseUrl:baseUrl, endpoint:endpoint, query:query)
    }
  }

  def put(String baseUrl, String endpoint, Map query = [:], String body = ""){
    try{
      def client = new RESTClient(baseUrl)
      def response = client.put(path:endpoint, query:query)
      {
        text body
      }
      response.json
    }catch (HTTPClientException e) {
      handleError(
        e:e, method:"PUT", baseUrl:baseUrl, endpoint:endpoint, query:query)
    }
  }

  def delete(String baseUrl, String endpoint, Map query = [:]){
    try{
      def client = new RESTClient(baseUrl)
      def response = client.delete(path:endpoint, query:query)
      response.json
    }catch (HTTPClientException e) {
      handleError(
        e:e, method:"DELETE", baseUrl:baseUrl, endpoint:endpoint, query:query)
    }
  }

  private def handleError(Map params) {
      log.error "${params?.e} -- ${params?.e?.message} por ${params?.method}"
      log.error "Base URl ${params?.baseUrl}"
      log.error "Endpoint: ${params?.endpoint}"
      log.error "Query: ${params?.query ?: 'Sin query'}"
  }

}
