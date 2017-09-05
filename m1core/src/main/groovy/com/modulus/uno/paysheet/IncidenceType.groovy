package com.modulus.uno.paysheet

enum IncidenceType {
  PERCEPTION(1, "Percepción"), DEDUCTION(-1, "Deducción")

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
