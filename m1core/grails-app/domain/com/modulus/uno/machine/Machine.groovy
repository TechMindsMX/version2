package com.modulus.uno.machine

class Machine {

  String uuid = UUID.randomUUID().toString().replace('-','')[0..15]
  State initialState

  Date dateCreated
  Date lastUpdated

  static hasMany = [states:State,
                    transitions:Transition]

  static constraints = {
    initialState nullable:true
  }

}
