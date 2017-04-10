package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock

@TestFor(CommissionTransactionService)
@Mock([CommissionTransaction, Company, Transaction, SaleOrder, Commission])
class CommissionTransactionServiceSpec extends Specification {

  void "Should save a commission transaction"() {
    given:"A fee command"
      FeeCommand feeCommand = new FeeCommand(companyId:"1", amount:new BigDecimal(10), type:"PAGO", transactionId:"1")
    and:
      Company company = new Company().save(validate:false)
      Transaction transaction = new Transaction().save(validate:false)
    when:"We save the commission"
      def commission = service.saveCommissionTransaction(feeCommand)
    then:
      commission.id
  }

  void "Should obtain the commissions pending balance for company"() {
    given:"A company"
      Company company = new Company().save(validate:false)
      Commission commission1 = new Commission(fee:5, percentage:0, type:CommissionType.DEPOSITO).save(validate:false)
      Commission commission2 = new Commission(fee:10, percentage:0, type:CommissionType.PAGO).save(validate:false)
      company.addToCommissions(commission1)
      company.addToCommissions(commission2)
      company.save(validate:false)
    when:
      def balance = service.getCommissionsPendingBalanceForCompany(company)
    then:
      balance.size() == company.commissions.size()
  }

}

