package com.modulus.uno

class TrackingLog{
  
  State currentState

  Date dateCreated
  Date lastUpdated

  static constraints = {
    currentState nullable:false
  }
}
