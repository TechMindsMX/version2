package com.modulus.uno.machine

class Transition {

  State stateFrom
  State stateTo  
  Action action

  Date dateCreated
  Date lastUpdated

  static belongsTo = [machine:Machine]

  static constraints = {
    action nullable:false
    stateTo nullable:false
  }

}
