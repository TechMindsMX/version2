package com.modulus.uno.paysheet

enum IncidenceType {
  PERCEPTION(1, "Percepción"), DEDUCTION(-1, "Deducción"), OTHER_PERCEPTION(1, "Otros pagos")

  private final int factor
  private final String description

  IncidenceType(int factor, String descr) {
    this.factor = factor
    this.description = descr
  }

  public int getFactor() {
    this.factor
  }

  public String toString() {
    this.description
  }

}
