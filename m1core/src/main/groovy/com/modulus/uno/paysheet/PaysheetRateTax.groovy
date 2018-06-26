package com.modulus.uno.paysheet

enum RateTax {
  R1(0.01, 578.52, 0, 1.92),
  R2(578.53, 4910.18, 11.11, 6.40),
  R3(4910.19, 8629.20, 288.33, 10.88),
  R4(8629.21, 10031.07, 692.96, 16),
  R5(10031.08, 12009.94, 917.26, 17.92),
  R6(12009.95, 24222.31, 1271.87, 21.36),
  R7(24222.32, 38177.69, 3880.44, 23.52),
  R8(38177.70, 72887.50, 7162.74, 30),
  R9(72887.51, 97183.33, 17575.69, 32),
  R10(97183.34, 291550, 25350.35, 34),
  R11(291550.01, 999999999999.99, 91435.02, 35)

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
