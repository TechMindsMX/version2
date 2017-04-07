package com.modulus.uno.machine

class State {

  Boolean finalState = false
  String name

  static belongsTo = [machine:Machine]

  Date dateCreated
  Date lastUpdated

  static constraints = {
    finalState nullable:false
  }

}
