package com.modulus.uno

import grails.util.Holders as H
import wslite.rest.*

class RestService {

  def grailsApplication
  WsliteRequestService wsliteRequestService

  String facturacionUrl = H.grailsApplication.config.modulus.facturacionUrl
  String modulusunoUrl = H.grailsApplication.config.modulus.url

  def getOnModulus(MessageCommand message, String template) {
    log.info "Calling Service : ${template}"
    wsliteRequestService.doRequest(modulusunoUrl){
      endpointUrl "${template}/${message.uuid}"
    }.doit()?.json
  }

  def getInvoiceData(def invoice) {
    wsliteRequestService.doRequest("http://api.makingdevs.com"){
      endpointUrl "/InvoiceDetail.groovy"
      callback {
        type ContentType.BINARY
        bytes invoice
      }
    }.doit()?.json
  }

  def getBalancesIntegrator(String type, String template) {
    log.info "Calling Service : ${template}"
    wsliteRequestService.doRequest(modulusunoUrl){
      endpointUrl "${template}/${type}"
    }.doit()?.json
  }

  def sendEmailToEmailer(def message){
    log.info "Calling Emailer Service"
    wsliteRequestService.doRequest(grailsApplication.config.emailer.urlEmailer){
      method HTTPMethod.POST
      callback { json message }
    }.doit()?.json
  }

  def sendCommandWithAuth(MessageCommand message, String template){
    log.info "CALLING Modulusuno service: ${template}"
    log.debug "*"*30
    def data = [
        institucionContraparte: message.bankCode,
        empresa: "TECHMINDS", alias empresa
        fechaDeOperacion: new Date().format("yyyyMMdd"),  
        folioOrigen: "",
        claveDeRastreo: new Date().timestap(),
        institucionOperante: "90646",
        montoDelPago: message.amount,
        tipoDelPago: "1",
        tipoDeLaCuentaDelOrdenante: "",
        nombreDelOrdenante: "TECHMINDS",
        cuentaDelOrdenante: "",
        rfcCurpDelOrdenante: "",
        tipoDeCuentaDelBeneficiario: "40",
        nombreDelBeneficiario: "Provider Soft Temoc Uno",
        cuentaDelBeneficiario: message.clabe,
        rfcCurpDelBeneficiario: "NA",
        emailDelBeneficiario: "mailBeneficiary@mail.com",
        tipoDeCuentaDelBeneficiario2: "",
        nombreDelBeneficiario2: "",
        cuentaDelBeneficiario2: "",
        rfcCurpDelBeneficiario2: "",
        conceptoDelPago: message.concept,
        conceptoDelPago2: "",
        claveDelCatalogoDeUsuario1: "",
        claveDelCatalogoDeUsuario2: "",
        claveDelPago: "",
        referenciaDeCobranza: "",
        referenciaNumerica: "1170306",
        tipoDeOperación: "",
        topologia: "",
        usuario: "",
        medioDeEntrega: "",
        prioridad: "",
        iva: "",
      ]    
    log.debug message.properties.toString()
    String token = obtainingTokenFromModulusUno()
    callingModulusUno(message,template,token)
  }

  def obtainingTokenFromModulusUno() {
    log.info "Calling Modulusuno service for token"
    def endpoint = grailsApplication.config.modulus.token
    def data = getAuthMap()
    def response = wsliteRequestService.doRequest(modulusunoUrl){
      endpointUrl endpoint
      callback { urlenc data }
      method HTTPMethod.POST
    }.doit()
    log.info "Return token obtained ${response?.json?.access_token}"
    response?.json?.access_token
  }

  def sendFacturaCommandWithAuth(MessageCommand message, String template){
    log.info "CALLING Modulusuno facturacion service: ${template}"
    String token = obtainingFacturaToken()
    callingFacturaService(message,template,token)
  }

  def getTransactionsAccount(MessageCommand command){
    log.info "Calling Service : services/integra/tx/getTransactions"
    wsliteRequestService.doRequest(modulusunoUrl){
      endpointUrl "services/integra/tx/getTransactions/${command.uuid}/${command.begin}/${command.end}"
    }.doit()?.json
  }

  //TODO Metodo que no se usa, pero que se usara, pero se tendra que ajustar,
  //     falta agregar cuenta concentradora (PD no se si jala por cambio de peticion)
  def getTransactionsIntegrator(MessageCommand command, String template){
    log.info "Calling Service : ${template}"
    wsliteRequestService.doRequest(modulusunoUrl){
      endpointUrl "${template}/${command.type}/${command.begin}/${command.end}"
      method HTTPMethod.POST
    }.doit()?.json
  }

  def sendFilesForInvoiceM1(def bodyMap, def token) {
    log.info "Calling Service : Send Files for Create invoice"
    log.info "Path: ${grailsApplication.config.modulus.facturacionUrl}${grailsApplication.config.modulus.invoice}"
    def endpoint = grailsApplication.config.modulus.invoice
    def response = wsliteRequestService.doRequest(facturacionUrl){
      endpointUrl endpoint
      method HTTPMethod.POST
      callback {
        multipart "cer", bodyMap.cer.bytes, bodyMap.cer.contentType, bodyMap.cer.originalFilename
        multipart "key", bodyMap.key.bytes, bodyMap.key.contentType, bodyMap.key.originalFilename
        multipart "logo", bodyMap.cer.bytes, bodyMap.logo.contentType, bodyMap.logo.originalFilename
        multipart "password", bodyMap.password.bytes
        multipart "rfc", bodyMap.rfc.bytes
        multipart "certNumber", bodyMap.certNumber.bytes
      }
    }.doit()
    response
  }

  def existEmisorForGenerateInvoice(String rfc) {
    log.info "CALLING Service: Verify if exist emisor"
    String endpoint = "${grailsApplication.config.modulus.invoice}/${rfc}"
    def response = wsliteRequestService.doRequest(facturacionUrl){
      endpointUrl endpoint
    }.doit()
    response ? response.json : [error:false]
  }

  private def getAuthMap(){
    [
      username:grailsApplication.config.modulus.username,
      password:grailsApplication.config.modulus.password,
      client_id:grailsApplication.config.modulus.clientId,
      client_secret:grailsApplication.config.modulus.secret,
      grant_type:grailsApplication.config.modulus.grant
    ]
  }

  private def obtainingFacturaToken() {
    log.info "Calling Facturación service for token"
    def endpoint = grailsApplication.config.modulus.token
    def data = getAuthMap()
    def response = wsliteRequestService.doRequest(facturacionUrl){
      endpointUrl endpoint
      callback { urlenc data }
      method HTTPMethod.POST
    }.doit()
    log.info "Return token obtained ${response?.json?.access_token}"
    response?.json?.access_token
  }

  private def callingFacturaService(MessageCommand message,String template,String token) {
    log.info "Calling Facturación service for creating factura"
    def response = wsliteRequestService.doRequest(facturacionUrl){
      endpointUrl template
      headers Authorization: "Bearer ${token}"
      method HTTPMethod.POST
      callback {
        type ContentType.JSON
        text groovy.json.JsonOutput.toJson(message)
      }
    }.doit()
    response
  }

  private def callingModulusUno(MessageCommand message,String template,String token) {
    log.info "Calling Modulusuno service: ${template}"
    def response = wsliteRequestService.doRequest(modulusunoUrl){
      endpointUrl "${template}"
      headers Authorization: "Bearer ${token}"
      method HTTPMethod.POST
      callback {
        type ContentType.JSON
        text groovy.json.JsonOutput.toJson(message)
      }
    }.doit()
    response
  }

}
