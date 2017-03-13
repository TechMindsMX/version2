package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(LoanOrderService)
class LoanOrderServiceSpec extends Specification {

  RestService restService = Mock(RestService)
  TransactionHelperService transactionHelperService = Mock(TransactionHelperService)
  EmailSenderService emailSenderService = Mock(EmailSenderService)
  def grailsApplication = new GrailsApplicationMock()

  def setup(){
    service.restService = restService
    service.emailSenderService = emailSenderService
    service.transactionHelperService = transactionHelperService
    service.grailsApplication = grailsApplication
  }

  void "should execute a loan order"() {
    given:"A commission"
      Commission commission = new Commission(percentage:10, type: CommissionType.PRESTAMO)
    and:"A loan order and command"
      LoanOrder loanOrder = Mock(LoanOrder)
      TransferFundsCommand command = Mock(TransferFundsCommand)
      Company company = Mock(Company)
    when:"We send a loan order"
      loanOrder.creditor >> company
      company.commissions >> [commission]
      transactionHelperService.generateCommandFromLoanOrder(loanOrder, commission) >> command
      LoanOrder result = service.executeLoanOrder(loanOrder)
    then:"We expect to call modulus uno service"
    1 * restService.sendCommandWithAuth(command, grailsApplication.config.modulus.loanCreate)
    1 * emailSenderService.notifyLoanOrderChangeStatus(loanOrder)
    result == loanOrder
  }

  void "should not execute a loan order since there is no commission"() {
    given:"A loan order and command"
      LoanOrder loanOrder = Mock(LoanOrder)
      TransferFundsCommand command = Mock(TransferFundsCommand)
      Company company = Mock(Company)
    when:"We send a loan order"
      loanOrder.creditor >> company
      LoanOrder result = service.executeLoanOrder(loanOrder)
    then:"We expect to call modulus uno service"
    thrown CommissionException
  }

}
