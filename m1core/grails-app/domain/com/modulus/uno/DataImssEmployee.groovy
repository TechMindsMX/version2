package com.modulus.uno

import com.modulus.uno.paysheet.ContractType
import com.modulus.uno.paysheet.RegimeType
import com.modulus.uno.paysheet.WorkDayType
import com.modulus.uno.paysheet.JobRisk
import com.modulus.uno.paysheet.PaysheetSchema

class DataImssEmployee {

  EmployeeLink employee
  String nss
  Date registrationDate
  Date dischargeDate
  BigDecimal baseImssMonthlySalary
  BigDecimal monthlyNetAssimilableSalary
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
  PaysheetSchema paysheetSchema = PaysheetSchema.SA_IAS

  static constraints = {
    employee nullable:false
    nss nullable:true
    registrationDate nullable:true
    dischargeDate nullable:true
    baseImssMonthlySalary nullable:false, min:0.0
    monthlyNetAssimilableSalary nullable:false, min:0.0
    totalMonthlySalary nullable:false, min:0.0
    holidayBonusRate nullable:true, min:0.0, max:100.0
    annualBonusDays nullable:true, min:15
    paymentPeriod nullable:false
    jobRisk nullable:true
    paysheetSchema nullable:true
    workDayType nullable:true
    department nullable:true
    job nullable:true
  }

  BigDecimal getMonthlyAssimilableSalary() {
    this.totalMonthlySalary - this.baseImssMonthlySalary
  }

}
