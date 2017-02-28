package com.modulus.uno.machine

class TrackingLog {

  State state
  
  Date dateCreated
  Date lastUpdated

  static belongsTo = [machineryLink:MachineryLink]

  static constraints = {
    state nullable:false
  }

}
