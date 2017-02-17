package com.modulus.uno

class TrackingLogLink {

  Long trackingRef
  String type

  Date dateCreated
  Date lastUpdated

  static hasMany = [records:LogRecord]

  static constraints = {
    trackingRef min:0L
    type blank:false
  }

}
