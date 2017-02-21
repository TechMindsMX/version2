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

  TrackingLogLink getTrackingLogLinkOfInstance(def instance){
    TrackingLogLink.findByTrackingRefAndType(instance.id,instance.class.simpleName)
  }

  LogRecord addRecordToInstanceLog(def instance,Long stateId){
    TrackingLogLink trackingLogLink = getTrackingLogLinkOfInstance(instance)
    State currentState = State.get(stateId)
    LogRecord record = new LogRecord(currentState:currentState)
    trackingLogLink.addToRecords(record)
    trackingLogLink.save()
    record
  }

  ArrayList<LogRecord> findTrackingLogOfInstance(def instance){
    TrackingLogLink trackingLink  = getTrackingLogLinkOfInstance(instance)
    trackingLink.records
  }

  LogRecord findLastTrackingLogRecord(def instance){
    TrackingLogLink trackingLink  = getTrackingLogLinkOfInstance(instance)
    def criteria = LogRecord.createCriteria()
    Date maxDate = criteria.get{
      projections{
        max "dateCreated"
      }
    }

    LogRecord.findByDateCreated(maxDate)
  }

}
