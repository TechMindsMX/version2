package com.modulus.uno

class FirstAccessToLegalRepresentatives {

  String urlVerification
  User user
  Company company
  boolean enabled = true
  String token

  static constraints = {
    urlVerification nullable:false
  }
}
