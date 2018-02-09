package com.modulus.uno

enum PaymentPeriod {
  WEEKLY(7, "SEMANAL", "S"), FOURTEEN(14, "CATORCENAL", "C"), BIWEEKLY(15, "QUINCENAL", "Q"), MONTHLY(30, "MENSUAL", "M")

  PaymentPeriod(int days, String showName, String shortName) {
    this.days = days
    this.showName = showName
    this.shortName = shortName
  }

  private final int days
  private final String showName
  private final String shortName

  int getDays() {
    days
  }

  String getShortName() {
    shortName
  }

  public String toString() {
    return showName
  }

}
