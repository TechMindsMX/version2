package com.modulus.uno

import java.text.*
import grails.validation.Validateable

class DataImssEmployeeCommand implements Validateable {

  String idEmployee
  String nss
  Date registrationDate
  String baseImssMonthlySalary
  String crudeMonthlySalary
  String holidayBonusRate
  String annualBonusDays
  String paymentPeriod

  static constraints = {
    idEmployee nullable:false
    nss nullable:false
    registrationDate nullable:false
    baseImssMonthlySalary nullable:false
    crudeMonthlySalary nullable:false
    holidayBonusRate nullable:false
    annualBonusDays nullable:false
    paymentPeriod nullable:false
  }

  DataImssEmployee createDataImssEmployee() {
    EmployeeLink employee = EmployeeLink.get(this.idEmployee)
    new DataImssEmployee(
      employee:employee,
      registrationDate:this.registrationDate,
      nss:this.nss,
      baseImssMonthlySalary:getValueInBigDecimal(this.baseImssMonthlySalary),
      crudeMonthlySalary:getValueInBigDecimal(this.crudeMonthlySalary),
      holidayBonusRate:getValueInBigDecimal(this.holidayBonusRate),
      annualBonusDays:this.annualBonusDays.toInteger(),
      paymentPeriod:PaymentPeriod.find { it.toString() == this.paymentPeriod }
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
