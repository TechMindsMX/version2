package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(ConciliationCommissionsInvoiceService)
@Mock([ConciliationCommissionsInvoice, PaymentM1Emitter, CommissionsInvoice, CommissionTransaction])
class ConciliationCommissionsInvoiceServiceSpec extends Specification {

  void "Should return null when payment hasn't conciliations to apply"() {
    given:"A payment m1 emitter"
      PaymentM1Emitter payment =  new PaymentM1Emitter(amount:1000).save(validate:false)
    when:
      def result = service.getConciliationsToApplyForPayment(payment)
    then:
      !result
  }

  void "Should get two elements when payment has two conciliations to apply"() {
    given:"A payment m1 emitter"
      PaymentM1Emitter payment =  new PaymentM1Emitter(amount:1000).save(validate:false)
    and:"The conciliations for payment"
      ConciliationCommissionsInvoice conciliation1 = new ConciliationCommissionsInvoice(payment:payment, amount:100).save(validate:false)
      ConciliationCommissionsInvoice conciliation2 = new ConciliationCommissionsInvoice(payment:payment, amount:200).save(validate:false)
    when:
      def result = service.getConciliationsToApplyForPayment(payment)
    then:
      result.size() == 2
  }

}
