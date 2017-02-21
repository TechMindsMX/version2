package com.modulus.uno

class State {

  Boolean finalState = false

  static belongsTo = [machine:Machine]
  static hasMany = [transitions:Transition]

  Date dateCreated
  Date lastUpdated

  static constraints = {
    finalState nullable:false
  }

}
