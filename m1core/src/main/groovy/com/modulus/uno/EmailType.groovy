package com.modulus.uno

enum EmailType {
  CASA ("Casa"),
  TRABAJO ("Trabajo"),
  OTRO ("Otro")

  final String value

  EmailType(String value){
    this.value = value
  }

  String getValue(){
    value
  }
}
