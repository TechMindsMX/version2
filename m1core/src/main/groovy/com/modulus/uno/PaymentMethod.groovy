package com.modulus.uno

enum PaymentMethod {
  EFECTIVO("EFECTIVO","01"), 
  CHEQUE_NOMINATIVO("CHEQUE NOMINATIVO", "02"), 
  TRANSFERENCIA_ELECTRONICA("TRANSFERENCIA ELECTRONICA", "03"), 
  TARJETA_CREDITO("TARJETA DE CRÉDITO", "04"),
  TARJETA_DEBITO("TARJETA DE DÉBITO", "28"),
  POR_DEFINIR("POR DEFINIR", "99")

  PaymentMethod(String description, String key) {
    this.description = description
    this.key = key
  }

  private final String description
  private final String key

  String getDescription() {
    this.description
  }
  String getKey() {
    this.key
  }

  public String toString() {
    return key + " - " + description
  }

}
