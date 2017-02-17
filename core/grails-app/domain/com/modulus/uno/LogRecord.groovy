package com.modulus.uno

class LogRecord{
  
  State currentState

  Date dateCreated
  Date lastUpdated

  static constraints = {
    currentState nullable:false
  }
}
