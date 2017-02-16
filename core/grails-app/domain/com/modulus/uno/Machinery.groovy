package com.modulus.uno

class Machinery {

  State initialState

  Date dateCreated
  Date lastUpdated

  static hasMany = [states:State]

  static constraints = {
    initialState nullable:false
  }

}
