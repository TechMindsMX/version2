package com.modulus.uno

import wslite.rest.*
import static groovyx.net.http.ContentType.*
import groovyx.net.http.HTTPBuilder
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.content.InputStreamBody
import org.apache.http.entity.mime.content.StringBody
import static groovyx.net.http.Method.*

class RestService {

  def grailsApplication
  def restClientBean
  WsliteConnectionService wsliteConnectionService
  WsliteRequestService wsliteRequestService

  def getOnModulus(MessageCommand message, String template) {
    log.info "Calling Service : ${template}"
    def response = wsliteConnectionService.get(grailsApplication.config.modulus.url,
                                           "${template}/${message.uuid}")
    response ?: new RestException("Error")
  }

  def getInvoiceData(def invoice) {
    def response = wsliteConnectionService.post("http://api.makingdevs.com",
                                                "/InvoiceDetail.groovy",[:],{
        type ContentType.BINARY
        bytes invoice
      })
    response ?: new RestException("Error!!!")
  }

  def getBalancesIntegrator(String type, String template) {
    log.info "Calling Service : ${template}"
    def response = wsliteConnectionService.get(grailsApplication.config.modulus.url,
                                           "${template}/${type}")
    response ?: new RestException("Error!!!")
  }

  def sendEmailToEmailer(def message){
    log.info "Calling Emailer Service"
    def response = wsliteConnectionService.post(grailsApplication.config.emailer.urlEmailer, "" ,[:] , { json message })
  }

  def sendCommandWithAuth(MessageCommand message, String template){
    try{
      log.info "CALLING Modulusuno service: ${template}"
      String token = obtainingTokenFromModulusUno()
      def modulusResponse = callingModulusUno(message,template,token)
      log.info "Return Information of modulus uno account to service"
      modulusResponse
    } catch(BusinessException ex) {
      log.warn "Error: ${ex.message}"
      throw new RestException(ex.message)
    }
  }

  def getAuthMap(){
    [
      username:grailsApplication.config.modulus.username,
      password:grailsApplication.config.modulus.password,
      client_id:grailsApplication.config.modulus.clientId,
      client_secret:grailsApplication.config.modulus.secret,
      grant_type:grailsApplication.config.modulus.grant
    ]
  }

  def obtainingTokenFromModulusUno() {
    log.info "Creating RestTemplate Object for obtain token"
    restClientBean.uri = grailsApplication.config.modulus.url
    log.info "Calling Modulus Uno service"
    def tokenResponse = restClientBean.post(
      path: grailsApplication.config.modulus.token,
      body: getAuthMap(),
      requestContentType: URLENC
      )
    log.info "Return token obtained ${tokenResponse.dump()}"
    tokenResponse.responseData.access_token
  }

  private def callingModulusUno(MessageCommand message,String template,String token) {
    log.info "Creating RestTemplate object for create Modulus Uno Account"
    restClientBean.uri = grailsApplication.config.modulus.url
    log.info "Calling service for create account of Modulus Uno"
    def modulusAccountResponse = restClientBean.post(
      path: template,
      headers: [Authorization:"Bearer ${token}"],
      body:message,
      requestContentType: 'application/json')
    log.info "Return of account and properties of Modulus Uno"
    modulusAccountResponse.responseData
  }

  //Metodos de consulta de facturacion
  def sendFacturaCommandWithAuth(MessageCommand message, String template){
    try{
      log.info "CALLING Modulusuno facturacion service: ${template}"
      String token = obtainingFacturaToken()
      def modulusResponse = callingFacturaService(message,template,token)
      log.info "Return Information of modulus uno"
      modulusResponse
    } catch(BusinessException ex) {
      log.warn "Error: ${ex.message}"
      throw new RestException(ex.message)
    }
  }

  private def obtainingFacturaToken() {
    log.info "Calling Facturación service for token"
    def tokenResponse = wsliteConnectionService.post(grailsApplication.config.modulus.facturacionUrl,
                                                     grailsApplication.config.modulus.token, [:], { urlenc getAuthMap() })
    log.info "Return token obtained ${tokenResponse.dump()}"
    tokenResponse.access_token
  }

  private def callingFacturaService(MessageCommand message,String template,String token) {
    log.info "Calling Facturación service for creating factura"
    String baseUrl = grailsApplication.config.modulus.facturacionUrl
    String endpoint = template
    def response = wsliteRequestService.doRequest{
      baseUrl baseUrl
      endpointUrl endpoint
      headers Authorization: "Bearer ${token}"
      callback { urlenc message}
    }.json()
    response
    /*
    def modulusAccountResponse = restClientBean.post(
      body:message,
      requestContentType: 'application/json')
    */
  }

  def getTransactionsAccount(MessageCommand command){
    log.info "Calling Service : services/integra/tx/getTransactions"
    wsliteConnectionService.get(grailsApplication.config.modulus.url,
                                "services/integra/tx/getTransactions/${command.uuid}/${command.begin}/${command.end}")
  }

  //TODO Metodo que no se usa, pero que se usara, pero se tendra que ajustar, 
  //     falta agregar cuenta concentradora (PD no se si jala por cambio de peticion)
  def getTransactionsIntegrator(MessageCommand command, String template){
    log.info "Calling Service : ${template}"
    def response = wsliteConnectionService.get("grailsApplication.config.modulus.url",
                                               "${template}/${command.type}/${command.begin}/${command.end}")
    response ?: new RestException("Error aqui")
  }

  def sendFilesForInvoiceM1(def bodyMap, def token) {
    try {
      log.info "Calling Service : Send Files for Create invoice"
      log.info "Path: ${grailsApplication.config.modulus.facturacionUrl}${grailsApplication.config.modulus.invoice}"
      def http = new HTTPBuilder("${grailsApplication.config.modulus.facturacionUrl}${grailsApplication.config.modulus.invoice}")
      http.request(POST) { req ->
        requestContentType: "multipart/form-data"
        MultipartEntity multiPartContent = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE)
        multiPartContent.addPart("cer", new InputStreamBody(bodyMap.cer.inputStream, bodyMap.cer.contentType, bodyMap.cer.originalFilename))
        multiPartContent.addPart("key", new InputStreamBody(bodyMap.key.inputStream, bodyMap.key.contentType, bodyMap.key.originalFilename))
        multiPartContent.addPart("logo", new InputStreamBody(bodyMap.logo.inputStream, bodyMap.logo.contentType, bodyMap.logo.originalFilename))
        multiPartContent.addPart("password", new StringBody(bodyMap.password))
        multiPartContent.addPart("rfc", new StringBody(bodyMap.rfc))
        multiPartContent.addPart("certNumber", new StringBody(bodyMap.certNumber))

        req.setEntity(multiPartContent)

      }
    } catch(Exception ex) {
      log.info "error completado"
      log.error "Error ${ex.message}"
      return "500"
    }
  }

  def existEmisorForGenerateInvoice(String rfc) {
    log.info "CALLING Service: Verify if exist emisor"
    String facturacionUrl = grailsApplication.config.modulus.facturacionUrl
    String endpoint = "${grailsApplication.config.modulus.invoice}/${rfc}"
    def response = wsliteRequestService.doRequest{
      baseUrl facturacionUrl
      endpointUrl endpoint
    }.json()
    response ?: [error:false]
  }

}
