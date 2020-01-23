package com.modulus.uno.credit

enum ProductType {
  ADVANCE("Adelanto"),
  SIMPLE_CREDIT("Crédito Simple")

  final String value

  ProductType(String value){
    this.value = value
  }

  String getValue(){
    value
  }
}
