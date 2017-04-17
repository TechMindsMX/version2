package com.modulus.uno.machine

class TrackingLog {

  String state
  
  Date dateCreated
  Date lastUpdated

  static belongsTo = [machineryLink:MachineryLink]

  static constraints = {
    state nullable:false
  }

}
