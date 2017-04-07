package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock

@TestFor(CommissionTransactionService)
@Mock([CommissionTransaction, Company, Transaction, SaleOrder])
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

}

