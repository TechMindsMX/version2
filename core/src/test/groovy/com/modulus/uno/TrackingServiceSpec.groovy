package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import java.lang.Void as Should
import spock.lang.FailsWith

@TestFor(TrackingService)
@Mock([PurchaseOrder,Bank,TrackingLogLink,LogRecord,State])
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
      TrackingLogLink trackingLogLink = service.createTrackingLogForThisInstance(instance) 
    then:
      !trackingLogLink.id
  }

  Should "create the log register for instance"(){
    given:"the instance"
      PurchaseOrder purchaseOrder = new PurchaseOrder()
      purchaseOrder.save(validate:false)
    and:"its tracking log link"
      TrackingLogLink trackingLogLink = service.createTrackingLogForThisInstance(purchaseOrder) 
    and:"the current state of the instance in the machine"
      State state = new State()
      state.save(validate:false)
    when:
      LogRecord record = service.addRecordToInstanceLog(purchaseOrder,state.id)
    then:
      record.id
      record.currentState.id == state.id
  }

  Should "get the records of the log"(){
    given:"the instance"
      PurchaseOrder purchaseOrder = new PurchaseOrder()
      service.createTrackingLogForThisInstance(purchaseOrder)
    and:"the states"
      ArrayList<State> states = [new State(),new State(),new State(isFinalState:true)]
      states.each{ state ->
        state.save(validate:false)
        service.addRecordToInstanceLog(purchaseOrder,state.id)
      }
    when:
      ArrayList<LogRecord> logRecords = service.findTrackingLogOfInstance(purchaseOrder)
    then:
      logRecords.size() == 3
  }

}
