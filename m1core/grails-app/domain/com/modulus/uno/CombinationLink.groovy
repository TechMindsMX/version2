package com.modulus.uno

class CombinationLink {

  String type
  Long instanceRef

  Date dateCreated
  Date lastUpdated

  Combination combination

  static constraints = {
    type nullable:false, blank:false
    instanceRef nullable:false, blank:false
  }

}
