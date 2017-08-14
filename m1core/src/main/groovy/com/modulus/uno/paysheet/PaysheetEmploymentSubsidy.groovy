package com.modulus.uno.paysheet

enum EmploymentSubsidy {
  S1(0.01, 1768.96, 407.02),
  S2(1768.97, 2653.38, 406.83),
  S3(2653.39, 3472.84, 406.62),
  S4(3472.85, 3537.87, 392.77),
  S5(3537.88, 4446.15, 382.46),
  S6(4446.16, 4717.18, 354.23),
  S7(4717.19, 5335.42, 324.87),
  S8(5335.43, 6224.67, 294.63),
  S9(6224.68, 7113.90, 253.54),
  S10(7113.91, 7382.33, 217.61),
  S11(7382.34, 999999999999.99, 0)

  private final BigDecimal lowerLimit
  private final BigDecimal upperLimit
  private final BigDecimal subsidy

  EmploymentSubsidy(BigDecimal lowLim, BigDecimal upLim, BigDecimal subsidy) {
    this.lowerLimit = lowLim
    this.upperLimit = upLim
    this.subsidy = subsidy
  }

  BigDecimal getLowerLimit() {
    lowerLimit
  }

  BigDecimal getUpperLimit() {
    upperLimit
  }

  BigDecimal getSubsidy() {
    subsidy
  }

}
