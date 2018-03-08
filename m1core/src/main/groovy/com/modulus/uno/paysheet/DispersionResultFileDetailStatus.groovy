package com.modulus.uno.paysheet

enum DispersionResultFileDetailStatus {
  APPLIED("APLICADO"), NOT_FOUND("NO ENCONTRADO"), AMOUNT_ERROR("ERROR EN MONTO"), REJECTED("RECHAZADO"), PREVIOUS_PROCESSED("PREVIAMENTE PROCESADO")

  private final String description

  DispersionResultFileDetailStatus(String descr) {
    this.description = descr
  }

  public String toString() {
    this.description
  }

}
