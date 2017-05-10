package com.modulus.uno

class StpService {

  SignService signService
  GenerateXMLService generateXMLService
  RequestSOAPService requestSOAPService
  def grailsApplication

  def sendPayOrder(Map data){
    log.info "Data map: ${data}"
    String sign = signService.generateSign(data)
    log.info "Data to sign: ${sign}"
    String encryptedSign = signService.encodeSign(sign)
    log.info "Encrypted sign: ${encryptedSign}"
    data.firma = encryptedSign
    String xmlPayOrder = generateXMLService.xmlPayOrderRequest(data)
    def result = requestSOAPService.doRequest(grailsApplication.config.stp.urls.payOrder){
      xml xmlPayOrder
    }.doit()
    validateResult(result.envelope.text())
    result.envelope.text()
  }

  private void validateResult(String idResult) {
    if (!idResult.isNumber()) {
      log.error "Error al registrar el pago en STP: ${idResult}"
      throw new RestException("No fue posible ejecutar el pago, intente más tarde")
    }
  }

  def getTransactionsForCompanyInPeriod(String account, Period period) {
    Map data = createDataMap(account, period)
    String xmlSignedConciliation = generateXMLService.xmlSignedConciliationRequest(data)
    def result = requestSOAPService.doRequest(grailsApplication.config.stp.urls.signedConciliation){
      xml xmlSignedConciliation
    }.doit()

  }

}
