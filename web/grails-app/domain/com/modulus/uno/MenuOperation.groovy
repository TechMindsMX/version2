package com.modulus.uno

class MenuOperation {

  String name
  String internalUrl

  Date dateCreated
  Date lastUpdated

  static constraints = {
    name blank:false
    internalUrl blank:false
  }
}
