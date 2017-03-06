package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(FeesReceipt)
@Mock([BusinessEntity])
class FeesReceiptSpec extends Specification {

  @Unroll
  void """given an amount: #amount, iva: #iva, isr: #isr and we validate we expect: #result"""() {
  given:"An receipt"
    FeesReceipt receipt = new FeesReceipt()
  and:"A company"
    def company = new Company()
  when:"We assign values"
    receipt.amount = amount
    receipt.iva = iva
    receipt.isr = isr
    receipt.ivaWithHolding = ivaWithHolding
    receipt.company = company
  then:"We validate"
    result == receipt.validate()
  where:"We expect following values"
    amount   | iva    | isr   | ivaWithHolding || result
    15000    | 2400   | 1500  | 1600           || true
    -1       | 2400   | 1500  | 1600           || false
    15000    | -1     | 1500  | 1600           || false
    15000    | null   | 1500  | 1600           || false
    15000    | 2400   | -1    | 1600           || false
    15000    | 2400   | null  | 1600           || false
    15000    | 2400   | 1500  | -1             || false
    15000    | 2400   | 1500  | null           || false
  }

  void "should compute iva, isr and iva withHolding"(){
  given:"An receipt"
    FeesReceipt receipt = new FeesReceipt()
  when:"we create object"
    receipt.amount = 15000
  then:"We expect has values"
    receipt.iva == 2400
    receipt.isr == 1500
    receipt.ivaWithHolding == 1600
  }

}
