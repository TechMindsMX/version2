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

}
