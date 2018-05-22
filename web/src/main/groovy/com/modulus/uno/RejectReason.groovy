package com.modulus.uno

enum RejectReason {
  DOCUMENTO_INVALIDO ("DOCUMENTO INVÁLIDO"), CANTIDAD_INCORRECTA ("CANTIDAD INCORRECTA"), OTRO ("OTRO")

  private final String description
  private final String name

  RejectReason(String descrip) {
    this.description = descrip
  }

  String getDescription() {
    this.description
  }

  String getName() {
    this.name()
  }
}
