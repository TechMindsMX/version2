package com.modulus.uno

class MachineryLink {

  Long machineryRef
  String type
  Machine machine

  Date dateCreated
  Date lastUpdated

  static hasMany = [trackingLogs:TrackingLog]

  static constraints = {
    machineryRef min:0L
    type blank:false
  }

}
