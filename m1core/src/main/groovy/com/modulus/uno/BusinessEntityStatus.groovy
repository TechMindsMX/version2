package com.modulus.uno

enum BusinessEntityStatus {
  ACTIVE("ACTIVO"), INACTIVE("INACTIVO"), TO_AUTHORIZE("POR AUTORIZAR"), REJECTED("RECHAZADO")

  BusinessEntityStatus(String description) {
    this.description = description
  }

  private final String description

  String getDescription() {
    this.description
  }
}
