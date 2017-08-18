package com.modulus.uno.paysheet

enum PaymentSchema {
  IMSS("IMSS"), ASSIMILABLE("Asimilable")

  private final String description

  PaymentSchema(String descr) {
    this.description = descr
  }

  public String toString() {
    this.description
  }
}
