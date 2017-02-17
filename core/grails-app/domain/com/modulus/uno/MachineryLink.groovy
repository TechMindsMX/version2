package com.modulus.uno

class MachineryLink {

  Long machineryRef
  String type
  Machinery machinery

  Date dateCreated
  Date lastUpdated

  static constraints = {
    machineryRef min:0L
    type blank:false
  }

}
