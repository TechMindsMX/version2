package com.modulus.uno

import java.text.*
import grails.validation.Validateable

class DataImssEmployeeCommand implements Validateable {

  String idEmployee
  String nss
  String registrationDate
  String baseImssMonthlySalary
  String netMonthlySalary
  String holidayBonusRate
  String annualBonusDays
  PaymentPeriod paymentPeriod

  static constraints = {
    idEmployee nullable:false
    nss nullable:false
    registrationDate nullable:false
    imssSalary nullable:false
    assimilableSalary nullable:false
    holidayBonusRate nullable:false
    annualBonusDays nullable:false
    paymentPeriod nullable:false
  }

  DataImssEmployee createDataImssEmployee() {
    EmployeeLink employee = EmployeeLink.get(this.idEmployee)
    new DataImssEmployee(
      employee: employee,
      registrationDate:Date.parse("dd/MM/yyyy", this.registrationDate),
      baseImssMonthlySalary:getValueInBigDecimal(this.baseImssMonthlySalary),
      netMonthlySalary:getValueInBigDecimal(this.netMonthlySalary),
      holidayBonusRate:getValueInBigDecimal(this.holidayBonusRate),
      annualBonusDays:this.annualBonusDays.toInteger(),
      paymentPeriod:this.paymentPeriod
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
