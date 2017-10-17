package com.modulus.uno

import wslite.soap.*

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
    log.info "Request conciliation service stp for company ${company}"
    Map data = createDataMapForConciliation(company, period)
    String xmlSignedConciliation = generateXMLService.xmlSignedConciliationRequest(data)
    def result = requestSOAPService.doRequest(grailsApplication.config.stp.urls.payOrder){
      xml xmlSignedConciliation
    }.doit()
    Map transactions = processResponseConciliationService(result)
    transactions.period = period
    transactions
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

  private Map processResponseConciliationService(def response) {
    log.info "Processing response from conciliation stp"
    String str = new String(response.httpResponse.data, "UTF-8").trim().replace("\n", "")
    def ns = new groovy.xml.Namespace("http://h2h.integration.spei.enlacefi.lgec.com/", 'ns')
    def root = new XmlParser().parseText(str)

    Map result = [balance:[:], transactions:[]]
    Map resultResponse = resultResponse(root)
    if (resultResponse.code != 0) {
      log.error "Error consultando conciliacionServiceFirma: ${resultResponse.code} - ${resultResponse.description}"
    } else {
      log.info "Result is OK, obtaining balance and transactions"
      List transactions = obtainTransactionsFromResponse(root)
      Map balance = obtainBalanceFromResponse(root)
      result.balance = balance
      result.transactions = transactions
    }
    result
  }

  private Map resultResponse(def root) {
    def result = root.'**'.find {
         it.name()=='resultado'
    }

    Map resultResponse = [:]
    resultResponse.code = new Integer(result.codigo.text())
    resultResponse.description = result.descripcion.text()
    resultResponse
  }

  private List obtainTransactionsFromResponse(def root) {
    def movimientos = root.'**'.findAll{
      it.name()=='movimiento'
    }

    def listMovs = []
    movimientos.each { mov ->
      def mapMovs = [:]
      mapMovs.id = mov.id.text()
      mapMovs.credit = new BigDecimal(mov.abono.text())
      mapMovs.debit = new BigDecimal(mov.cargo.text())
      mapMovs.clabe = mov.clabe.text()
      mapMovs.bankCode = mov.contraparte.text()
      mapMovs.settlementDate = Date.parse("yyyy-MM-dd HH:mm:ss",mov.fechaLiquidacion.text())
      mapMovs.bankName = mov.institucionNombre.text()
      mapMovs.tracing = mov.rastreo.text()
      mapMovs.reference = mov.referenciaNumerica.text()
      listMovs << mapMovs
    }

    listMovs.sort({m1, m2 -> m1.settlementDate <=> m2.fecha})
  }

  private Map obtainBalanceFromResponse(def root) {
    def cuentaWS = root.'**'.find { it.name()=='cuentaWS' }
    def saldo = cuentaWS.saldo

    Map balance = [:]
    balance.totalCredits = new BigDecimal(saldo.abonos.text())
    balance.totalDebits = new BigDecimal(saldo.cargos.text())
    balance.account = saldo.cuenta.text()
    balance.countCredits = new Integer(saldo.nAbonos.text())
    balance.countDebits = new Integer(saldo.nCargos.text())
    balance.balance = new BigDecimal(saldo.saldo.text())
    balance.periodBalance = new BigDecimal(saldo.saldoPeriodo.text())

    balance
  }

}
