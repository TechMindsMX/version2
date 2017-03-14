package com.modulus.uno

class STPService {

  SignService signService
  GenerateXMLService generateXMLService
  RequestSOAPService requestSOAPService

  def sendPayOrder(def algo){
    String sign = signService.generateSign(algo)
    String encryptedSign = signService.encodeSign(sign)
    algo.firma = encryptedSign
    String xmlPayOrder = generateXMLService.xmlOrderSaleRequest(algo)
    def result = requestSOAPService.doRequest("http://demo.stpmex.com:7004/speidemo/webservices/SpeiServices"){
      xml xmlPayOrder
    }.doit()
    log.debug result.dump()
  }
}
