package com.modulus.uno

class LogRecord {
  
  State currentState

  Date dateCreated
  Date lastUpdated

  static belongsTo = [trackingLogLink:TrackingLogLink]

  static constraints = {
    currentState nullable:false
  }
}
