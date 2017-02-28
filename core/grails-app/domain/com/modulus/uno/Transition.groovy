package com.modulus.uno

class Transition {

  State stateFrom
  State stateTo  
  Action action

  Date dateCreated
  Date lastUpdated

  static constraints = {
    action nullable:false
    stateTo nullable:false
  }

}
