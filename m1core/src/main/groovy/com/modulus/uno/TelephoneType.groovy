package com.modulus.uno

enum TelephoneType {
  CASA ("Casa"),
  TRABAJO ("Trabajo"),
  CELULAR ("Celular")

  final String value

  TelephoneType(String value){
    this.value = value
  }

  String getValue(){
    value
  }
}
