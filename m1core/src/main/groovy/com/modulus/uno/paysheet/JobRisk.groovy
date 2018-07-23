package com.modulus.uno.paysheet

enum JobRisk {
  CLASS_01 ("1", "Clase I"),
  CLASS_02 ("2", "Clase II"),
  CLASS_03 ("3", "Clase III"),
  CLASS_04 ("4", "Clase IV"),
  CLASS_05 ("5", "Clase V"),
  NOT_APPLY ("99", "No Aplica")

  private final String key
  private final String description

  JobRisk (String key, String description) {
    this.key = key
    this.description = description
  }

  String getKey() {
    this.key
  }

  String toString() {
    this.description
  }
}
