package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(TransactionHelperService)
class TransactionHelperServiceSpec extends Specification {

  void "should generate command from loan order when is percentage"() {
    given:"A Loan order"
      LoanOrder loanOrder = Mock(LoanOrder)
    and:"A commission"
      Commission commission = new Commission(percentage:10, type: CommissionType.PRESTAMO)
    and:"A creditor and debtor"
      Company creditor = Mock(Company)
      Company debtor = Mock(Company)
      ModulusUnoAccount creditorAccount = Mock(ModulusUnoAccount)
      ModulusUnoAccount debtorAccount = Mock(ModulusUnoAccount)
    when:"We generate command from loan order"
      loanOrder.amount >> 100
      creditorAccount.timoneUuid >> 'creditorUuid'
      debtorAccount.timoneUuid >> 'debtorUuid'
      loanOrder.company >> creditor
      loanOrder.creditor >> debtor
      creditor.accounts >> [creditorAccount]
      debtor.accounts >> [debtorAccount]
      TransferFundsCommand result = service.generateCommandFromLoanOrder(loanOrder, commission)
    then:"We expect amount and commission computed"
      result.amount == 100
      result.fee == 10
  }

  void "should generate command from loan order when is amount"() {
    given:"A Loan order"
      LoanOrder loanOrder = Mock(LoanOrder)
    and:"A commission"
      Commission commission = new Commission(fee:20, type: CommissionType.PRESTAMO)
    and:"A creditor and debtor"
      Company creditor = Mock(Company)
      Company debtor = Mock(Company)
      ModulusUnoAccount creditorAccount = Mock(ModulusUnoAccount)
      ModulusUnoAccount debtorAccount = Mock(ModulusUnoAccount)
    when:"We generate command from loan order"
      loanOrder.amount >> 100
      creditorAccount.timoneUuid >> 'creditorUuid'
      debtorAccount.timoneUuid >> 'debtorUuid'
      loanOrder.company >> creditor
      loanOrder.creditor >> debtor
      creditor.accounts >> [creditorAccount]
      debtor.accounts >> [debtorAccount]
      TransferFundsCommand result = service.generateCommandFromLoanOrder(loanOrder, commission)
    then:"We expect amount and commission computed"
      result.amount == 100
      result.fee == 20
  }

}
