package com.modulus.uno.paysheet

enum ExtraHourType {

  DOUBLE ("01", "Dobles"),
  TRIPLE ("02", "Triples"),
  SIMPLE ("03", "Simples")

  private final String key
  private final String description

  ExtraHourType (String key, String description) {
    this.key = key
    this.description = description
  }

  String toString() {
    this.description
  }

}
