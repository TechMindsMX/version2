package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(LoanOrderHelperService)
@Mock([LoanPaymentOrder,User,Authorization])
class LoanOrderHelperServiceSpec extends Specification {

  def grailsApplication = new GrailsApplicationMock()
  EmailSenderService emailSenderService = Mock(EmailSenderService)
  ModulusUnoService modulusUnoService = Mock(ModulusUnoService)

  def setup(){
    service.grailsApplication = grailsApplication
    service.emailSenderService = emailSenderService
    service.modulusUnoService = modulusUnoService
  }

  void "should compute first payment"() {
    given:"A loan order"
      LoanOrder loanOrder = Mock(LoanOrder)
    and:"This expectations"
      loanOrder.loanDate >> new Date() - 30
      loanOrder.term >> 12
      loanOrder.amount >> 50000
      loanOrder.rate >> 10
      service.getCurrenteBalance >> 50000
    when:"When we compute payment"
      LoanPaymentOrder result = service.computeMinimumPayment(loanOrder)
    then:"We expect this result"
      result.daysPeriod == 30
      result.amountInterest == 410.96
      result.amountIvaInterest == 65.75
  }

  void "should compute payment commission"(){
    given:"A loan payment order"
      Company company = Mock(Company)
      LoanOrder loanOrder = Mock(LoanOrder)
      Commission commission = Mock(Commission)
      commission.percentage >> 2
      commission.type >> CommissionType.DEPOSITO
      company.commissions >> [commission]
      loanOrder.company >> company
      LoanPaymentOrder loanPaymentOrder = new LoanPaymentOrder(amountInterest:new BigDecimal(410.96), amountIvaInterest:new BigDecimal(65.75), loanOrder:loanOrder)
      loanPaymentOrder.save(validate:false)
    when:"When we compute payment"
      BigDecimal result = service.computeCommissionPayment(loanPaymentOrder)
    then:"We expect this result"
      result == 9.53
  }

  void "should send to authorize a loan payment order"(){
    given:"A loan payment order"
      Company company = Mock(Company)
      LoanOrder loanOrder = Mock(LoanOrder)
      LoanPaymentOrder loanPaymentOrder = new LoanPaymentOrder (amountInterest : new BigDecimal(410.96), amountIvaInterest : new BigDecimal(65.75), amountToCapital : new BigDecimal(4500), datePayment : new Date(), daysPeriod : 30, loanOrder : loanOrder, status : LoanPaymentOrderStatus.CREATED, company : company)
      loanPaymentOrder.save(validate:false)
    when:"When we send to authorize"
      LoanPaymentOrder result = service.sendToAuthorize(loanPaymentOrder)
    then:"We expect this"
      1 * emailSenderService.notifyLoanPaymentOrderChangeStatus(loanPaymentOrder)
      result.status == LoanPaymentOrderStatus.VALIDATE
  }

  void "should add authorization to loan payment order"(){
    given:"A loan payment order"
      LoanPaymentOrder loanPaymentOrder = new LoanPaymentOrder(amountToCapital:new BigDecimal(4500))
      loanPaymentOrder.save(validate:false)
    and:"A User"
      User user = new User(username:'usuario')
      user.save(validate:false)
    when:"When we send to authorize"
      loanPaymentOrder = service.addAuthorizationToLoanPaymentOrder(loanPaymentOrder, user)
    then:"We expect this"
      loanPaymentOrder.authorizations.size() == 1
  }

  void "should set authorized a loan payment order"(){
    given:"A loan payment order"
      LoanPaymentOrder loanPaymentOrder = new LoanPaymentOrder(amountToCapital:new BigDecimal(4500))
      loanPaymentOrder.save(validate:false)
    when:"When we send to authorize"
      loanPaymentOrder = service.authorizeLoanPaymentOrder(loanPaymentOrder)
    then:"We expect this"
      loanPaymentOrder.status == LoanPaymentOrderStatus.AUTHORIZED
      1 * emailSenderService.notifyLoanPaymentOrderChangeStatus(loanPaymentOrder)
  }

  void "should set executed a loan payment order"(){
    given:"A loan payment order"
      Company debtor = Mock(Company)
      ModulusUnoAccount accountDebtor = Mock(ModulusUnoAccount)
      accountDebtor.timoneUuid >> "UuidDebtor"
      debtor.accounts >> [accountDebtor]
      Company creditor = Mock(Company)
      ModulusUnoAccount accountCreditor = Mock(ModulusUnoAccount)
      accountCreditor.timoneUuid >> "UuidCreditor"
      creditor.accounts >> [accountCreditor]
      LoanOrder loanOrder = Mock(LoanOrder)
      Commission commission = Mock(Commission)
      commission.percentage >> 2
      commission.type >> CommissionType.DEPOSITO
      creditor.commissions >> [commission]
      loanOrder.company >> creditor
      LoanPaymentOrder loanPaymentOrder = new LoanPaymentOrder(amountToCapital:new BigDecimal(4500), amountInterest:new BigDecimal(10), amountIvaInterest:new BigDecimal(1.6), company:debtor, loanOrder:loanOrder)
      loanPaymentOrder.save(validate:false)
    when:"When we send to executed"
      loanPaymentOrder = service.executeLoanPaymentOrder(loanPaymentOrder)
    then:"We expect this"
      loanPaymentOrder.status == LoanPaymentOrderStatus.EXECUTED
      1 * emailSenderService.notifyLoanPaymentOrderChangeStatus(loanPaymentOrder)
  }

}
