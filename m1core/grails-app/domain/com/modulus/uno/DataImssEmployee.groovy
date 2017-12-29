package com.modulus.uno

class DataImssEmployee {

  EmployeeLink employee
  String nss
  Date registrationDate
  BigDecimal baseImssMonthlySalary
  BigDecimal crudeMonthlySalary
  BigDecimal holidayBonusRate
  Integer annualBonusDays
  PaymentPeriod paymentPeriod = PaymentPeriod.WEEKLY

  static constraints = {
    employee nullable:false
    nss nullable:false, unique: true
    registrationDate nullable:false
    baseImssMonthlySalary nullable:false, min:0.0
    crudeMonthlySalary nullable:false, min:0.0
    holidayBonusRate nullable:false, min:0.0, max:100.0
    annualBonusDays nullable:false, min:15
    paymentPeriod nullable:false
  }

  BigDecimal getCrudeMonthlyAssimilableSalary() {
    this.crudeMonthlySalary - this.baseImssMonthlySalary
  }

  /*
  Integer getAntiquityInWeeks() {
  }

  Integer getAntiquityInDays() {
  }

  String getAntiquityInYearsMonthsDays() {
  }
  */
}
