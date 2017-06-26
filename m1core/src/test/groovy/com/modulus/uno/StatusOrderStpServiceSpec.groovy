package com.modulus.uno

import spock.lang.Specification
import grails.test.mixin.TestFor
import grails.test.mixin.Mock

@TestFor(StatusOrderStpService)
@Mock([StatusOrderStp])
class StatusOrderStpServiceSpec extends Specification {

  void "Should save a status order stp"() {
    given:"A status order stp"
      StatusOrderStp statusOrderStp = new StatusOrderStp(keyTransaction:"IdTransaccion", company:"AliasStp", trackingKey:"Rastreo", status:"RECHAZADO", causeRefund:CauseRefundStp.ACCOUNT_NOT_EXISTS)
    when:
      def result = service.saveStatusOrderStp(statusOrderStp)
    then:
      result.id > 0
  }

}
