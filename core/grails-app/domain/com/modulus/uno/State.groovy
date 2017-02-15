package com.modulus.uno

class State {

  Boolean isFinalState = false

  static belongsTo = [machinery:Machinery]
  static hasMany = [transitions:Transition]

  Date dateCreated
  Date lastUpdated

  static constraints = {
    isFinalState nullable:false
  }

}
