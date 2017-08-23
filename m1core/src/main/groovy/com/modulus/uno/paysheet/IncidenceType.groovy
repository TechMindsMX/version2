package com.modulus.uno.paysheet

enum IncidenceType {
  PERCEPTION(1), DEDUCTION(-1)

  private final int factor

  IncidenceType(int factor) {
    this.factor = factor
  }

  public int getFactor() {
    this.factor
  }
  
}
