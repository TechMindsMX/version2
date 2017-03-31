package com.modulus.uno

class CombinationLink {

  String type
  Long instanceRef

  Date dateCreated
  Date lastUpdated

  static hasMany = [combinations:Combination]

  static constraints = {
    type nullable:false, blank:false
    instanceRef nullable:false, blank:false
  }

}
