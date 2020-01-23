package com.modulus.uno.credit

enum FrequencyType {
  MONTHLY("Mensual"),
  BIWEEKLY("Quincenal"),
  WEEKLY("Semanal"),
  FOURTEEN("Catorcenal")

  final String value

  FrequencyType(String value){
    this.value = value
  }

  String getValue(){
    value
  }
}
