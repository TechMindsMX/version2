package com.modulus.uno.paysheet

enum ContractType {
  INDEFINED("01", "Contrato de trabajo por tiempo indeterminado"),
  DEFINED_WORK("02", "Contrato de trabajo para obra determinada"),
  DEFINED_TIME("03", "Contrato de trabajo por tiempo determinado"),
  SEASON_WORK("04","Contrato de trabajo por temporada"),
  TRIAL_WORK("05","Contrato de trabajo sujeto a prueba"),
  WORK_WITH_TRAINING("06","Contrato de trabajo con capacitación inicial"),
  HOURLY_WORK("07","Modalidad de contratación por pago de hora laborada"),
  LABOR_COMMISSION("08","Modalidad de trabajo por comisión laboral"),
  WORK_WITHOUT_RELATION("09","Modalidades de contratación donde no existe relación de trabajo"),
  RETIREMENT("10","Jubilación, pensión, retiro"),
  OTHER("99","Otro contrato")

  private final String key
  private final String description

  ContractType(String key, String description) {
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
