package com.modulus.uno.paysheet

enum DispersionResultFileDetailStatus {
  FOUND("ENCONTRADO"), NOT_FOUND("NO ENCONTRADO")

  private final String description

  DispersionResultFileDetailStatus(String descr) {
    this.description = descr
  }

  public String toString() {
    this.description
  }

}
