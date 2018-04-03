package com.modulus.uno

import com.modulus.uno.paysheet.ContractType
import com.modulus.uno.paysheet.RegimeType
import com.modulus.uno.paysheet.WorkDayType
import com.modulus.uno.paysheet.JobRisk

class DataImssEmployee {

  EmployeeLink employee
  String nss
  Date registrationDate
  Date dischargeDate
  BigDecimal baseImssMonthlySalary
  BigDecimal totalMonthlySalary
  BigDecimal holidayBonusRate
  Integer annualBonusDays
  PaymentPeriod paymentPeriod = PaymentPeriod.WEEKLY
  ContractType contractType = ContractType.UNDEFINED
  RegimeType regimeType = RegimeType.OTHER
  WorkDayType workDayType = WorkDayType.DIURNAL
  JobRisk jobRisk = JobRisk.CLASS_01
  String department
  String job


  static constraints = {
    employee nullable:false
    nss nullable:false
    registrationDate nullable:false
    dischargeDate nullable:true
    baseImssMonthlySalary nullable:false, min:0.0
    totalMonthlySalary nullable:false, min:0.0
    holidayBonusRate nullable:false, min:0.0, max:100.0
    annualBonusDays nullable:false, min:15
    paymentPeriod nullable:false
    jobRisk nullable:false
  }

  BigDecimal getMonthlyAssimilableSalary() {
    this.totalMonthlySalary - this.baseImssMonthlySalary
  }

}
