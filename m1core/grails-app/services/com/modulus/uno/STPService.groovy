package com.modulus.uno

class StpService {

  SignService signService
  GenerateXMLService generateXMLService
  RequestSOAPService requestSOAPService
  def grailsApplication

  def sendPayOrder(Map data){
    String sign = signService.generateSign(data)
    log.info "Data to sign: ${sign}"
    String encryptedSign = signService.encodeSign(sign)
    log.info "Encrypted sign: ${encryptedSign}"
    data.firma = encryptedSign
    String xmlPayOrder = generateXMLService.xmlPayOrderRequest(data)
    def result = requestSOAPService.doRequest(grailsApplication.config.stp.urls.payOrder){
      xml xmlPayOrder
    }.doit()
    result.envelope.text()
  }
}
