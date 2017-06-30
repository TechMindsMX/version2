package com.modulus.uno

enum PaymentPeriod {
  WEEKLY(7), FOURTEEN(14), BIWEEKLY(15), MONTHLY(30)

  PaymentPeriod(int days) {
    this.days = days
  }

  private final int days

  int getDays() {
    days
  }

  public String toString() {
    return name()
  }

}
