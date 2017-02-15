package com.modulus.uno

class Action {
  String name

  Date dateCreated
  Date lastUpdated

  static constraints = {
    name nullable:false,blank:false
  } 
}
