package com.modulus.uno

import spock.lang.Specification
import grails.test.mixin.TestFor
import grails.test.mixin.Mock

@TestFor(BankService)
@Mock([Bank])
class BankServiceSpec extends Specification {

  void "should compute clabe and get bank entity"() {
    given:"A clabe"
      String clabe = '072180002095154312'
    and:"A bank"
      Bank bank = new Bank(name:'BANORTE', bankingCode:'40072')
    when:"We compute clabe"
      Bank.metaClass.static.findByBankingCodeLike = { bank }
      Bank result = service.computeClabeAndGetBank(clabe)
    then:"We get id"
     result.name == 'BANORTE'
     result.bankingCode == '40072'
  }

}
