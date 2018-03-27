package com.modulus.uno

import java.text.*
import grails.validation.Validateable
import com.modulus.uno.paysheet.ContractType
import com.modulus.uno.paysheet.RegimeType
import com.modulus.uno.paysheet.WorkDayType

class DataImssEmployeeCommand implements Validateable {

  String idEmployee
  String nss
  String registrationDate
  String dischargeDate
  String baseImssMonthlySalary
  String totalMonthlySalary
  String holidayBonusRate
  String annualBonusDays
  String paymentPeriod
  String contractType
  String regimeType
  String workDayType
  String department
  String job

  static constraints = {
    idEmployee nullable:false
    nss nullable:false
    registrationDate nullable:false
    dischargeDate nullable:true
    baseImssMonthlySalary nullable:false
    totalMonthlySalary nullable:false
    holidayBonusRate nullable:false
    annualBonusDays nullable:false
    paymentPeriod nullable:false
    contractType nullable:false
    regimeType nullable:false
    workDayType nullable:false
    department nullable:false
    job nullable:false
  }

  DataImssEmployee createDataImssEmployee() {
    EmployeeLink employee = EmployeeLink.get(this.idEmployee)
    new DataImssEmployee(
      employee:employee,
      registrationDate:Date.parse("dd/MM/yyyy", this.registrationDate),
      dischargeDate:this.dischargeDate ? Date.parse("dd/MM/yyyy", this.dischargeDate) : null,
      nss:this.nss,
      baseImssMonthlySalary:getValueInBigDecimal(this.baseImssMonthlySalary),
      totalMonthlySalary:getValueInBigDecimal(this.totalMonthlySalary),
      holidayBonusRate:getValueInBigDecimal(this.holidayBonusRate),
      annualBonusDays:this.annualBonusDays.toInteger(),
      paymentPeriod:PaymentPeriod.find { it.toString() == this.paymentPeriod },
      contractType:ContractType.find { it.toString() == this.contractType },
      regimeType:RegimeType.find { it.toString() == this.regimeType },
      workDayType:WorkDayType.find { it.toString() == this.workDayType },
      department:this.department,
      job:this.job
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
