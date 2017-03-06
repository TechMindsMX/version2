package com.modulus.uno

enum PaymentMethod {
  NA("N/A"), EFECTIVO("EFECTIVO"), CHEQUE_NOMINATIVO("CHEQUE NOMINATIVO"), TRANSFERENCIA_ELECTRONICA("TRANSFERENCIA ELECTRONICA")

  PaymentMethod(String description) {
    this.description = description
  }

  private final String description
  private final String key = ordinal().toString().padLeft(2,'0')

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
