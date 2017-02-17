package com.modulus.uno

class TrackingLogLink {

  Long trackingRef
  String type

  Date dateCreated
  Date lastUpdated

  static hasMany = [trackingLogs:TrackingLog]

  static constraints = {
    trackingRef min:0L
    type blank:false
  }

}
