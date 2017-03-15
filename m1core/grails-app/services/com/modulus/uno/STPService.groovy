package com.modulus.uno

class StpService {

  SignService signService
  GenerateXMLService generateXMLService
  RequestSOAPService requestSOAPService

  def sendPayOrder(Map data){
    String sign = signService.generateSign(data)
    String encryptedSign = signService.encodeSign(sign)
    data.firma = encryptedSign
    String xmlPayOrder = generateXMLService.xmlOrderSaleRequest(data)
    def result = requestSOAPService.doRequest("http://demo.stpmex.com:7004/speidemo/webservices/SpeiServices"){
      xml xmlPayOrder
    }.doit()
    result
  }
}
