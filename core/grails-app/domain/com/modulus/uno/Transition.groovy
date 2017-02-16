package com.modulus.uno

class Transition {
  
  Action action
  State stateTo

  Date dateCreated
  Date lastUpdated
  
  static belongsTo = [state: State]

  static constraints = {
    action nullable:false
    stateTo nullable:false
  }

}
