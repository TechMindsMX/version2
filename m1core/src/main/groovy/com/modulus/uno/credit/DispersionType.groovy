package com.modulus.uno.credit

enum DispersionType {
  INTERBANK_TRANSFER("CLABE Bancaria")

  final String value

  DispersionType(String value){
    this.value = value
  }

  String getValue(){
    value
  }
}
