package com.modulus.uno

class Machinery {

  State initialState 
  Company company

  Date dateCreated
  Date lastUpdated

  static hasMany = [states:State]

}
