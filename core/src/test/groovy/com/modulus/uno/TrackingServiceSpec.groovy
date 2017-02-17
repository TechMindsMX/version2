package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import java.lang.Void as Should
import spock.lang.FailsWith

@TestFor(TrackingService)
@Mock([PurchaseOrder,Bank,TrackingLogLink,TrackingLog])
class TrackingServiceSpec extends Specification {

  Should "create a tracking link for an instance"(){
    given:"the instance that implements the interface"
      PurchaseOrder purchaseOrder = new PurchaseOrder()
      purchaseOrder.save(validate:false)
    when:
      TrackingLogLink trackingLogLink = service.createTrackingLogForThisInstance(purchaseOrder)
    then:
      trackingLogLink.id
      trackingLogLink.type == purchaseOrder.class.simpleName
  }

  @FailsWith(RuntimeException)
  Should "fail while trying to create the tracking for an instance without implements"(){
    given:
      Bank instance = new Bank()
    when:
      TrackingLogLink trackingLogLink = service.createTrackinForThisInstance(bank) 
    then:
      !trackingLogLink.id
  }

}
