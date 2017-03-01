package com.modulus.uno

enum LeadType {
  USER(""),
  CLIENTE(""),
  PROVEEDOR(""),
  CLIENTE_PROVEEDOR(""),
  EMPLEADO("")

  private final String code

  LeadType(String code){
    this.code = code
  }

  String getCode(){ return this.code }
}
