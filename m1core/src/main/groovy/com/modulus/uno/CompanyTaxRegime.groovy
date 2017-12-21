package com.modulus.uno

enum CompanyTaxRegime {
  MORAL("taxregime.moral", "601", "General de Ley Personas Morales"),
  FISICA_EMPRESARIAL("taxregime.fisica", "612", "Personas Físicas con Actividades Empresariales y Profesionales")

  private final String code
  private final String key
  private final String description 

  CompanyTaxRegime(String code, String key, String description){
    this.code = code
    this.key = key
    this.description = description
  }

  String getCode(){ return this.code }
  String getKey(){ return this.key }
  String getDescription(){ return this.description }
}
