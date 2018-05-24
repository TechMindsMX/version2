package com.modulus.uno.status

enum CreditNoteStatus {
  CREATED ("CREADA"),
  TO_AUTHORIZE ("POR AUTORIZAR"),
  AUTHORIZED ("AUTORIZADA"),
  REJECTED ("RECHAZADA"),
  APPLIED ("APLICADA"),
  CANCELED ("CANCELADA"),
  CANCEL_TO_AUTHORIZE ("CANCELACIÓN POR AUTORIZAR"),
  CANCEL_AUTHORIZED ("CANCELACIÓN AUTORIZADA"),
  CANCEL_APPLIED ("CANCELACIÓN APLICADA")

  private final String description

  CreditNoteStatus (String descrip) {
    this.description = descrip
  }

  String toString() {
    this.description
  }

}
