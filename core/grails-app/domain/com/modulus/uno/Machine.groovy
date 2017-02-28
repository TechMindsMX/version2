package com.modulus.uno

class Machine {

  State initialState

  Date dateCreated
  Date lastUpdated

  static hasMany = [states:State,
                    transitions:Transition]

  static constraints = {
    initialState nullable:true
  }

}
