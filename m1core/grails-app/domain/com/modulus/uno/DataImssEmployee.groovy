package com.modulus.uno

import com.modulus.uno.paysheet.ContractType
import com.modulus.uno.paysheet.RegimeType
import com.modulus.uno.paysheet.WorkDayType

class DataImssEmployee {

  EmployeeLink employee
  String nss
  Date registrationDate
  BigDecimal baseImssMonthlySalary
  BigDecimal totalMonthlySalary
  BigDecimal holidayBonusRate
  Integer annualBonusDays
  PaymentPeriod paymentPeriod = PaymentPeriod.WEEKLY
  ContractType contractType = ContractType.INDEFINED
  RegimeType regimeType = RegimeType.OTHER
  WorkDayType workDayType = WorkDayType.DIURNAL
  String department
  String job


  static constraints = {
    employee nullable:false
    nss nullable:false
    registrationDate nullable:false
    baseImssMonthlySalary nullable:false, min:0.0
    totalMonthlySalary nullable:false, min:0.0
    holidayBonusRate nullable:false, min:0.0, max:100.0
    annualBonusDays nullable:false, min:15
    paymentPeriod nullable:false
  }

  BigDecimal getMonthlyAssimilableSalary() {
    this.totalMonthlySalary - this.baseImssMonthlySalary
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
