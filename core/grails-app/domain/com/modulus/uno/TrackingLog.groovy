package com.modulus.uno

class TrackingLog {

  State state

  Date dateCreated
  Date lastUpdated

  static constraints = {
    state nullable:false
  }

}
