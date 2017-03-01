package com.modulus.uno.machine

class Transition {

  State stateFrom
  State stateTo  

  Date dateCreated
  Date lastUpdated

  static hasMany = [actions:String]

  static belongsTo = [machine:Machine]

  static constraints = {
    stateTo nullable:false
  }

}
