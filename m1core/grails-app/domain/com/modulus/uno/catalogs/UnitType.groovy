package com.modulus.uno.catalogs

import com.modulus.uno.Company

class UnitType {
  
  Company company
  String unitKey
  String name
  String symbol

  static constraints = {
    unitKey unique:"company"
    symbol nullable:true
  }
}
