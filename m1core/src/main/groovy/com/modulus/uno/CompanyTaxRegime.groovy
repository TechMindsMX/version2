package com.modulus.uno

enum CompanyTaxRegime {
  MORAL("taxregime.moral"),
  FISICA_EMPRESARIAL("taxregime.fisica")

  private final String code

  CompanyTaxRegime(String code){
    this.code = code
  }

  String getCode(){ return this.code }
}
