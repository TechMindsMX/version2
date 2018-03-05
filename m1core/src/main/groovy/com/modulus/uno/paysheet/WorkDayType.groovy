package com.modulus.uno.paysheet

enum WorkDayType {

  DIURNAL ("01", "Diurna"),
  NIGHTLY ("02", "Nocturna"),
  MIXED ("03", "Mixta"),
  PER_HOUR ("04", "Por hora"),
  REDUCED ("05", "Reducida"),
  CONTINUED ("06", "Continuada"),
  FRACTIONATED ("07", "Partida"),
  BY_TURNS ("08", "Por turnos"),
  OTHER ("99", "Otra Jornada")

  private final String key
  private final String description

  WorkDayType(String key, String description) {
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
