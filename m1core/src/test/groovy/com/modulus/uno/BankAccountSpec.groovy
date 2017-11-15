package com.modulus.uno

import spock.lang.Specification
import spock.lang.Unroll
import grails.test.mixin.Mock
import java.lang.Void as Should

@Mock([BankAccount,Bank])
class BankAccountSpec extends Specification{

  Should "create a bank account and validate the clabe"(){
    given:
      BankAccount bankAccount = new BankAccount()
    when:
      bankAccount.banco = new Bank(bankingCode:"90902",name:"INDEVAL").save()
      bankAccount.accountNumber = _accountNumber
      bankAccount.branchNumber = _branchNumber
      bankAccount.clabe = _clabe
    then:
      bankAccount.validate() == _result
    where:
     _accountNumber   | _branchNumber | _clabe               | _result
     "12345678901"   | "12345"         | "002115016003269411" | true
     "1368067893"    | "56789"         | "033333333377844923" | false
     "789654"        | "98865"         | "032180000118359719" | true
  }

}
