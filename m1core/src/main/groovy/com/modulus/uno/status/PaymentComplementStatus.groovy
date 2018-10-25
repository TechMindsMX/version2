package com.modulus.uno.status

enum PaymentComplementStatus {
  XML_GENERATED ("XML Generado"),
  FULL_STAMPED ("Timbrado Completo")

  private final String description

  PaymentComplementStatus (String description) {
    this.description = description
  }

  String toString() {
    this.description
  }
}
