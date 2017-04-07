package com.modulus.uno

import spock.lang.Specification
import grails.test.mixin.TestFor
import grails.test.mixin.Mock

@TestFor(StpDepositService)
@Mock([StpDeposit, Payment])
class StpDepositServiceSpec extends Specification {

  TransactionService transactionService = Mock(TransactionService)

  def setup() {
    service.transactionService = transactionService
  }

  void "Should obtain a accepted status for a stp deposit which account is from company"(){
    given:"A string notification with xml"
      String clave = "1101"
      String rastreo = "ABC001"
      String clabe = "646180132400800007"
      StpDeposit notification = createNotificationWithData(clave, rastreo, clabe)
    and:
      StpDeposit.metaClass.static.findAllByOperationNumberAndTracingKeyAndIdNotEqualAndStatusNotEqual = { [] }
    and:
      ModulusUnoAccount m1Account = Mock(ModulusUnoAccount)
      m1Account.save(validate:false)
      ModulusUnoAccount.metaClass.static.findByStpClabe = { m1Account }
    and:
      ClientLink.metaClass.static.findByStpClabe = { null }
    when:"We process the notification"
      def result = service.notificationDepositFromStp(notification)
    then:"We validate"
      result.estatus == StpDepositStatus.ACEPTADO
  }

  void "Should obtain a accepted status for a stp deposit which account is from client"(){
    given:"A string notification with xml"
      String clave = "1101"
      String rastreo = "ABC001"
      String clabe = "646180132400800007"
      StpDeposit notification = createNotificationWithData(clave, rastreo, clabe)
    and:
      StpDeposit.metaClass.static.findAllByOperationNumberAndTracingKeyAndIdNotEqualAndStatusNotEqual = { [] }
    and:
      ModulusUnoAccount.metaClass.static.findByStpClabe = { null }
    and:
      ClientLink client = Mock(ClientLink)
      client.save(validate:false)
      ClientLink.metaClass.static.findByStpClabe = { client }
    when:"We process the notification"
      def result = service.notificationDepositFromStp(notification)
    then:"We validate"
      result.estatus == StpDepositStatus.ACEPTADO
  }

  void "Should obtain a rejected status for a stp deposit which account is not from client and neither company"(){
    given:"A string notification with xml"
      String clave = "1101"
      String rastreo = "ABC001"
      String clabe = "646180132400800007"
      StpDeposit notification = createNotificationWithData(clave, rastreo, clabe)
    and:
      StpDeposit.metaClass.static.findAllByOperationNumberAndTracingKeyAndIdNotEqualAndStatusNotEqual = { [] }
    and:
      ModulusUnoAccount.metaClass.static.findByStpClabe = { null }
    and:
      ClientLink.metaClass.static.findByStpClabe = { null }
    when:"We process the notification"
      def result = service.notificationDepositFromStp(notification)
    then:"We validate"
      result.estatus == StpDepositStatus.RECHAZADO
  }

  private StpDeposit createNotificationWithData(String clave, String rastreo, String clabe) {
    new StpDeposit(
      operationNumber: clave.toLong(),
      operationDate: new Date().parse("yyyyMMdd", "20100323"),
      payerKey: "846",
      beneficiaryKey: "90646",
      tracingKey: rastreo,
      amount: new BigDecimal("200"),
      payerName: "Makingdevs",
      beneficiaryName: "Techminds",
      typeAccountBeneficiary: "40".toLong(),
      accountBeneficiary: "clabe",
      rfcCurpBeneficiary: "RFC121212ABC",
      paymentConcept: "prueba concepto",
      numericalReference: "2".toLong(),
      companyNameStp: "STP"
    )
  }

}
