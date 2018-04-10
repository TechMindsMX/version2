package com.modulus.uno

enum PaymentPeriod {
  WEEKLY(7, "SEMANAL", "S", "02"), FOURTEEN(14, "CATORCENAL", "C", "03"), BIWEEKLY(15, "QUINCENAL", "Q", "04"), MONTHLY(30, "MENSUAL", "M", "05")

  PaymentPeriod(int days, String showName, String shortName, String key) {
    this.days = days
    this.showName = showName
    this.shortName = shortName
    this.key = key
  }

  private final int days
  private final String showName
  private final String shortName
  private final String key

  int getDays() {
    days
  }

  String getShortName() {
    shortName
  }

  String getKey() {
    this.key
  }

  public String toString() {
    return showName
  }

}
