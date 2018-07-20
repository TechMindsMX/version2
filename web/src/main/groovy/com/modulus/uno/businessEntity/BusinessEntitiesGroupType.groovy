package com.modulus.uno.businessEntity

enum BusinessEntitiesGroupType {
  CLIENTS("CLIENTES"), PROVIDERS("PROVEEDORES"), EMPLOYEES("EMPLEADOS")

  private final String description

  BusinessEntitiesGroupType(String description) {
    this.description = description
  }

  String getDescription() {
    this.description
  }

  String toString() {
    this.description
  }
}
