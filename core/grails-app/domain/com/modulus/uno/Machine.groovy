package com.modulus.uno

class Machine {

  State initialState

  Date dateCreated
  Date lastUpdated

  static hasMany = [states:State]

  static constraints = {
    initialState nullable:false
  }

}
