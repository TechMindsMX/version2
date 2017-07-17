package com.modulus.uno

enum PaymentPeriod {
  WEEKLY(7, "SEMANAL"), FOURTEEN(14, "CATORCENAL"), BIWEEKLY(15, "QUINCENAL"), MONTHLY(30, "MENSUAL")

  PaymentPeriod(int days, String showName) {
    this.days = days
    this.showName = showName
  }

  private final int days
  private final String showName

  int getDays() {
    days
  }

  public String toString() {
    return showName
  }

}
