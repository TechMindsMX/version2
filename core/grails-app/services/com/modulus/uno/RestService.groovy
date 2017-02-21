package com.modulus.uno

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

  def getOnModulus(MessageCommand message, String template) {
    try {
      log.info "Calling Service : ${template}"
      restClientBean.uri = grailsApplication.config.modulus.url
      def resultGet = restClientBean.get(
        path: "${template}/${message.uuid}"
      )
      resultGet
    } catch(Exception ex) {
      log.warn "Error ${ex.message}"
      throw new RestException(ex.message)
    }
  }

  def getInvoiceData(def invoice) {
    try {
      restClientBean.uri = "http://api.makingdevs.com"
      restClientBean.post(
        path: "/InvoiceDetail.groovy",
        body:invoice,
        requestContentType: 'application/octet-stream')
    } catch(BusinessException ex){
      log.warm "Error ${ex.message}"
      throw new RestException(ex.message)
    }
  }

  def getBalancesIntegrator(String type, String template) {
    try {
      log.info "Calling Service : ${template}"
      restClientBean.uri = grailsApplication.config.modulus.url
      def resultGet = restClientBean.get(
        path: "${template}/${type}"
      )
      resultGet
    } catch(BusinessException ex) {
      log.warn "Error ${ex.message}"
      throw new RestException(ex.message)
    }
  }

//Send arguments to Emailer Modulus Uno
  def sendArgumentsToEmailer(def message){
    try{
      log.info message.dump()
      restClientBean.uri = grailsApplication.config.emailer.urlEmailer
      restClientBean.post(
        body: message,
        requestContentType: 'application/json' )
    } catch(BusinessException ex) {
      log.warn "Error: ${ex.message}"
      throw new RestException(ex.message)
    }
  }

  def sendCommand(MessageCommand message, String template){
    try{
      log.info "CALLING Email service: ${template}"
      restClientBean.uri = grailsApplication.config.emailer.url
      restClientBean.post(
        path: template,
        body: message,
        requestContentType: 'application/json' )
    } catch(BusinessException ex) {
      log.warn "Error: ${ex.message}"
      throw new RestException(ex.message)
    }
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
    log.info "Creating RestTemplate Object for obtain token"
    restClientBean.uri = grailsApplication.config.modulus.facturacionUrl
    log.info "Calling Modulus Uno service for token"
    def tokenResponse = restClientBean.post(
      path: grailsApplication.config.modulus.token,
      body: getAuthMap(),
      requestContentType: URLENC
      )
    log.info "Return token obtained ${tokenResponse.dump()}"
    tokenResponse.responseData.access_token
  }

  private def callingFacturaService(MessageCommand message,String template,String token) {
    log.info "Creating RestTemplate object for create Modulus Uno facturacion"
    restClientBean.uri = grailsApplication.config.modulus.facturacionUrl
    log.info "Calling service for creating factura"
    def modulusAccountResponse = restClientBean.post(
      path: template,
      headers: [Authorization:"Bearer ${token}"],
      body:message,
      requestContentType: 'application/json')
    log.info "Returning factura properties of Modulus Uno"
    modulusAccountResponse.responseData
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
    def result = wsliteConnectionService.get("${grailsApplication.config.modulus.facturacionUrl}",
                                           "${grailsApplication.config.modulus.invoice}/${rfc}")
    result ?: [error:false]
  }

}
