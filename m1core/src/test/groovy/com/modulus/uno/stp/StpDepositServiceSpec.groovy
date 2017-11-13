package com.modulus.uno.stp

import spock.lang.Specification
import grails.test.mixin.TestFor
import grails.test.mixin.Mock

import com.modulus.uno.Payment
import com.modulus.uno.Company
import com.modulus.uno.Commission
import com.modulus.uno.CommissionType
import com.modulus.uno.Transaction
import com.modulus.uno.ModulusUnoAccount
import com.modulus.uno.ClientLink
import com.modulus.uno.BusinessException

import com.modulus.uno.TransactionService
import com.modulus.uno.CommissionTransactionService
import com.modulus.uno.EmailSenderService

@TestFor(StpDepositService)
@Mock([StpDeposit, Payment, Company, Commission, Transaction, ModulusUnoAccount, ClientLink])
class StpDepositServiceSpec extends Specification {

  TransactionService transactionService = Mock(TransactionService)
  CommissionTransactionService commissionTransactionService = Mock(CommissionTransactionService)
  GrailsApplicationMock grailsApplication = new GrailsApplicationMock()
  EmailSenderService emailSenderService = Mock(EmailSenderService)

  def setup() {
    service.transactionService = transactionService
    service.commissionTransactionService = commissionTransactionService
    service.grailsApplication = grailsApplication
    service.emailSenderService = emailSenderService
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
      Company company = new Company().save(validate:false)
      Commission commission = new Commission(fee:10, percentage:0, type:CommissionType.DEPOSITO)
      company.addToCommissions(commission)
      company.save(validate:false)
    and:
      ModulusUnoAccount m1Account = new ModulusUnoAccount()
      m1Account.company = company
      m1Account.save(validate:false)
      ModulusUnoAccount.metaClass.static.findByStpClabe = { m1Account }
      ModulusUnoAccount.metaClass.static.findByStpClabeLike = { [stpClabe:"646180132400800007"] }
    and:
      ClientLink.metaClass.static.findByStpClabe = { null }
    and:
      transactionService.saveTransaction(_) >> new Transaction().save(validate:false)
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
      ModulusUnoAccount.metaClass.static.findByStpClabeLike = { [stpClabe:"646180132400800007"] }
    and:
      Company company = new Company().save(validate:false)
      Commission commission = new Commission(fee:10, percentage:0, type:CommissionType.DEPOSITO)
      company.addToCommissions(commission)
      company.save(validate:false)
    and:
      ClientLink client = new ClientLink()
      client.company = company
      client.save(validate:false)
      ClientLink.metaClass.static.findByStpClabe = { client }
    and:
      transactionService.saveTransaction(_) >> new Transaction().save(validate:false)
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
      ModulusUnoAccount.metaClass.static.findByStpClabeLike = { [stpClabe:"646180132400800007"] }
    and:
      ClientLink.metaClass.static.findByStpClabe = { null }
    when:"We process the notification"
      def result = service.notificationDepositFromStp(notification)
    then:"We validate"
      result.estatus == StpDepositStatus.RECHAZADO
  }

  void "Should thrown business exception when company hasn't commission for deposit"(){
    given:"A string notification with xml"
      String clave = "1101"
      String rastreo = "ABC001"
      String clabe = "646180132400800007"
      StpDeposit notification = createNotificationWithData(clave, rastreo, clabe)
    and:
      StpDeposit.metaClass.static.findAllByOperationNumberAndTracingKeyAndIdNotEqualAndStatusNotEqual = { [] }
    and:
      Company company = new Company().save(validate:false)
      Commission commission = new Commission(fee:10, percentage:0, type:CommissionType.PAGO)
      company.addToCommissions(commission)
      company.save(validate:false)
    and:
      ModulusUnoAccount m1Account = new ModulusUnoAccount()
      m1Account.company = company
      m1Account.save(validate:false)
      ModulusUnoAccount.metaClass.static.findByStpClabe = { m1Account }
      ModulusUnoAccount.metaClass.static.findByStpClabeLike = { [stpClabe:"646180132400800007"] }
    and:
      ClientLink.metaClass.static.findByStpClabe = { null }
    and:
      transactionService.saveTransaction(_) >> new Transaction().save(validate:false)
    when:"We process the notification"
      def result = service.notificationDepositFromStp(notification)
    then:"We validate"
      thrown BusinessException
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
      accountBeneficiary: "${clabe}",
      rfcCurpBeneficiary: "RFC121212ABC",
      paymentConcept: "prueba concepto",
      numericalReference: "2".toLong(),
      companyNameStp: "STP"
    )
  }

}
