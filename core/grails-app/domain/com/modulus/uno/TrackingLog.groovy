package com.modulus.uno

class TrackingLog {

  State state
  
  Date dateCreated
  Date lastUpdated

  static belongsTo = [machineryLink:MachineryLink]

  static constraints = {
    state nullable:false
  }

}
