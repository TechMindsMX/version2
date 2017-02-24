package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class TrackingService {

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

    Long maxId = criteria.get{
      projections{
        max "id"
      }
    }

    LogRecord.get(maxId)
  }

}
