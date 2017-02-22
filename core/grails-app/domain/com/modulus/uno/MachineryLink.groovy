package com.modulus.uno

class MachineryLink {

  Long companyRef
  String type
  Machine machine

  Date dateCreated
  Date lastUpdated

  static constraints = {
    companyRef min:0L
    type blank:false
  }

}
