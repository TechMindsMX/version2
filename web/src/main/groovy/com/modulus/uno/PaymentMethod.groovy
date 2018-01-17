package com.modulus.uno

enum PaymentMethod {
  PUE("PAGO EN UNA SOLA EXHIBICIÓN"), 
  PPD("PAGO EN PARCIALIDADES O DIFERIDO") 

  PaymentMethod(String description) {
    this.description = description
  }

  private final String description

  String getDescription() {
    this.description
  }

  public String toString() {
    return description
  }

}
