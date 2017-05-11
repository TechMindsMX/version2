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

  def getTransactionsForCompanyInPeriod(Company company, Period period) {
    Map data = createDataMapForConciliation(company, period)
    String xmlSignedConciliation = generateXMLService.xmlSignedConciliationRequest(data)
    def result = requestSOAPService.doRequest(grailsApplication.config.stp.urls.payOrder){
      xml xmlSignedConciliation
    }.doit()
    log.info "Result envelope: ${result.envelope}"
    result
  }

  private Map createDataMapForConciliation(Company company, Period period) {
    Map data = [
      initDate:"${period.init.format('yyyy-MM-dd HH:mm:ss')}",
      endDate:"${period.end.format('yyyy-MM-dd HH:mm:ss')}",
      account:company.accounts.first().stpClabe,
      costsCenter:company.accounts.first().aliasStp,
      sign:signService.encodeSign(company.accounts.first().aliasStp)
    ]
  }
}
