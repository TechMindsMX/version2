package com.modulus.uno

import java.text.*
import grails.validation.Validateable
import com.modulus.uno.paysheet.ContractType
import com.modulus.uno.paysheet.RegimeType
import com.modulus.uno.paysheet.WorkDayType
import com.modulus.uno.paysheet.JobRisk
import com.modulus.uno.paysheet.PaysheetSchema

class DataImssEmployeeCommand implements Validateable {

  String idEmployee
  String nss
  String registrationDate
  String dischargeDate
  String baseImssMonthlySalary
  String monthlyNetAssimilableSalary
  String totalCrudeMonthlySalary
  String holidayBonusRate
  String annualBonusDays
  String paymentPeriod
  String contractType
  String regimeType
  String workDayType
  String jobRisk
  String department
  String job
  String paysheetSchema

  static constraints = {
    idEmployee nullable:false
    nss nullable:true
    registrationDate nullable:true
    dischargeDate nullable:true
    baseImssMonthlySalary nullable:true
    monthlyNetAssimilableSalary nullable:true
    totalCrudeMonthlySalary nullable:false
    holidayBonusRate nullable:true
    annualBonusDays nullable:true
    paymentPeriod nullable:false
    contractType nullable:false
    regimeType nullable:false
    workDayType nullable:true
    jobRisk nullable:true
    department nullable:true
    job nullable:true
    paysheetSchema nullable:false
  }

  DataImssEmployee createDataImssEmployee() {
    EmployeeLink employee = EmployeeLink.get(this.idEmployee)
    new DataImssEmployee(
      employee:employee,
      registrationDate:this.registrationDate ? Date.parse("dd/MM/yyyy", this.registrationDate) : null,
      dischargeDate:this.dischargeDate ? Date.parse("dd/MM/yyyy", this.dischargeDate) : null,
      nss:this.nss,
      baseImssMonthlySalary:getValueInBigDecimal(this.baseImssMonthlySalary ?: "0"),
      monthlyNetAssimilableSalary:getValueInBigDecimal(this.monthlyNetAssimilableSalary ?: "0"),
      totalMonthlySalary:getValueInBigDecimal(this.totalCrudeMonthlySalary ?: "0"),
      holidayBonusRate:getValueInBigDecimal(this.holidayBonusRate ?: "0"),
      annualBonusDays:this.annualBonusDays?.toInteger(),
      paymentPeriod:PaymentPeriod.find { it.toString() == this.paymentPeriod },
      contractType:ContractType.find { it.toString() == this.contractType },
      regimeType:RegimeType.find { it.toString() == this.regimeType },
      workDayType:this.workDayType ? WorkDayType.find { it.toString() == this.workDayType } : null,
      jobRisk:this.jobRisk ? JobRisk.find { it.toString() == this.jobRisk } : null,
      department:this.department,
      job:this.job,
      paysheetSchema:PaysheetSchema."${this.paysheetSchema}"
    )
  }

  private def getValueInBigDecimal(String value) {
    Locale.setDefault(new Locale("es","MX"));
    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
    df.setParseBigDecimal(true);
    BigDecimal bd = (BigDecimal) df.parse(value);
    bd
  }

}
