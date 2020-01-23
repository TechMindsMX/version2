package com.modulus.uno.credit

enum CreditLineType {
  ONE_LINE("Solo 1 crédito")

  final String value

  CreditLineType(String value){
    this.value = value
  }

  String getValue(){
    value
  }
}
