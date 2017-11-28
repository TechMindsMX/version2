package com.modulus.uno.catalogs

class UnitType {
  
  String unitKey
  String name
  String symbol

  static constraints = {
    symbol nullable:true
  }
}
