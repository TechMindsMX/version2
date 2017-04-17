package com.modulus.uno

class Combination {

  String classLeft
  String classRight
  Long rightInstanceId

  Date dateCreated
  Date lastUpdated

  static constraints = {
    classLeft nullable:false, blank:false
    classRight nullable:false, blank:false
    rightInstanceId nullable:false, blank:false
  }

}
