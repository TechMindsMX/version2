package com.modulus.uno

import com.modulus.uno.catalogs.UnitType

class Product {

  String sku
  String name
  BigDecimal price = new BigDecimal(1)
  BigDecimal ieps = new BigDecimal(0)
  BigDecimal iva = new BigDecimal(16)

  UnitType unitType
  CurrencyType currencyType
  String satKey

  static belongsTo = [company:Company]

  static constraints = {
    sku blank:false,size:4..50,matches:/^[A-Za-z0-9-]*$/, unique: 'company'
    name blank:false,size:1..500
    price min:0.0,max:250000000.00
    ieps min:0.0,max:100.00
    iva min:0.0,max:100.00
    satKey blank:false, size:8..8
    unitType nullable:true
  }

  BigDecimal getAppliedIEPS(){
    price * (ieps/100)
  }

  BigDecimal getAppliedIVA(){
    price * (iva/100)
  }

}
