package com.modulus.uno.paysheet

enum RegimeType {

  SALARIES ("02", "Sueldos"),
  RETIREES ("03", "Jubilados"),
  PENSIONERS ("04", "Pensionados"),
  COOPERATIVES_ASSIMILATED ("05","Asimilados Miembros Sociedades Cooperativas Produccion"),
  CIVIL_SOCIETIES_ASSIMILATED ("06","Asimilados Integrantes Sociedades Asociaciones Civiles"),
  MEMBERS_COUNCIL_ASSIMILATED ("07", "Asimilados Miembros consejos"),
  COMMISSION_AGENTS_ASSIMILATED ("08", "Asimilados comisionistas"),
  FEES_ASSIMILATED ("09", "Asimilados Honorarios"),
  ACTIONS_ASSIMILATED ("10", "Asimilados acciones"),
  OTHERS_ASSIMILATED ("11", "Asimilados otros"),
  RETIREES_OR_PENSIONERS ("12", "Jubilados o Pensionados"),
  OTHER ("99", "Otro Regimen")

  private final String key
  private final String description

  RegimeType(String key, String description) {
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
