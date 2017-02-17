package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class TrackingService {

  TrackingLogLink createTrackingLogForThisInstance(def instance){
    if(!Tracking.class.isAssignableFrom(instance.class)){
      throw new RuntimeException("Tracking is not assignable from ${instance.class.simpleName}")
    }
    
    TrackingLogLink trackingLogLink = TrackingLogLink.findByTrackingRefAndType(instance.id,instance.class.simpleName) ?: new TrackingLogLink(trackingRef:instance.id,
                                                                                                                                             type:instance.class.simpleName)
    
    trackingLogLink.save()
    trackingLogLink
  }

  
  TrackingLog addRecordToInstanceLog(def instance,Long stateId){
    TrackingLogLink trackingLink = TrackingLogLink.findByTrackingRefAndType(trackingRef:instance.id,type:instance.class.simpleName) 
    State currentState = State.get(stateId)
    TrackingLog record = new TrackingLog(currentState:currentState)
    trackingLink.addToRecords(record)
    record.save()
    record
  }

}
