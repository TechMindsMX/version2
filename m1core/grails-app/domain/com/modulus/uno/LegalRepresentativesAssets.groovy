package com.modulus.uno

class LegalRepresentativesAssets {

  Long asset
  Long company

  static constraints = {
    asset nullable:false
    company nullable:false
  }
}
