package com.modulus.uno.paysheet

enum RateTax {
  R1(0.01, 496.07, 0, 1.92),
  R2(496.08, 4210.41, 9.52, 6.40),
  R3(4210.42, 7399.42, 247.23, 10.88),
  R4(7399.43, 8601.50, 594.24, 16),
  R5(8601.51, 10298.35, 786.55, 17.92),
  R6(10298.36, 20770.29, 1090.62, 21.36),
  R7(20770.30, 32736.83, 3327.42, 23.52),
  R8(32736.84, 62499.99, 6141.95, 30),
  R9(62500, 83333.33, 15070.90, 32),
  R10(83333.34, 250000, 21737.56, 34),
  R11(250000.01, 999999999999.99, 78404.23, 35)

  private final BigDecimal lowerLimit
  private final BigDecimal upperLimit
  private final BigDecimal fixedQuota
  private final BigDecimal rate

  RateTax(BigDecimal lowLim, BigDecimal upLim, BigDecimal quota, BigDecimal rate) {
    this.lowerLimit = lowLim
    this.upperLimit = upLim
    this.fixedQuota = quota
    this.rate = rate
  }

  BigDecimal getLowerLimit() {
    lowerLimit
  }

  BigDecimal getUpperLimit() {
    upperLimit
  }

  BigDecimal getFixedQuota() {
    fixedQuota
  }

  BigDecimal getRate() {
    rate
  }

}
