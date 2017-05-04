package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(PaymentM1EmitterService)
@Mock([PaymentM1Emitter])
class PaymentM1EmitterServiceSpec extends Specification {

  void "Should get the payments pending list"() {
    given:"The payments"
      PaymentM1Emitter payment1 = new PaymentM1Emitter(amount:1000, status:PaymentStatus.PENDING).save(validate:false)
      PaymentM1Emitter payment2 = new PaymentM1Emitter(amount:2000, status:PaymentStatus.PENDING).save(validate:false)
      PaymentM1Emitter payment3 = new PaymentM1Emitter(amount:1500, status:PaymentStatus.CONCILIATED).save(validate:false)
    when:
      def result = service.getPaymentsInStatus(PaymentStatus.PENDING)
    then:
      result.size() == 2

  }
}
