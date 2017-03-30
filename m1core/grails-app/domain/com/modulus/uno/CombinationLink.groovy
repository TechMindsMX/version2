package com.modulus.uno

class CombinationLink {

  String type
  Long instanceRef

  Combination combination

  Date dateCreated
  Date lastUpdated

  static constraints = {
    type nullable:false, blank:false
    instanceRef nullable:false, blank:false
  }

}
