package com.modulus.uno

class ClientLink {

  String type
  String clientRef
  String stpClabe

  static belongsTo = [company:Company]

  static constraints = {
    stpClabe nullable:true, blank:false,size:18..18
  }

}
