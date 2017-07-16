package com.modulus.uno

import grails.transaction.Transactional

class DataImssEmployeeService {

  @Transactional
  DataImssEmployee saveDataImss(DataImssEmployee dataImss) {
    dataImss.save()
    log.info "Data Imss saving: ${dataImss?.dump()}"
    dataImss
  }

  DataImssEmployee createDataImssForRowEmployee(Map rowEmployee, EmployeeLink employee) {
    PaymentPeriod paymentPeriod = PaymentPeriod.find { it.toString() == rowEmployee.PERIODO_PAGO.toUpperCase() }
    DataImssEmployee dataImssEmployee = new DataImssEmployee(
      employee:employee,
      nss:rowEmployee.NSS,
      registrationDate:Date.parse("dd-MM-yyyy", rowEmployee.FECHA_ALTA),
      baseImssMonthlySalary:new BigDecimal(rowEmployee.BASE_COTIZA),
      netMonthlySalary:new BigDecimal(rowEmployee.NETO),
      holidayBonusRate:new BigDecimal(rowEmployee.PRIMA_VAC),
      annualBonusDays:rowEmployee.DIAS_AGUINALDO.intValue(),
      paymentPeriod:paymentPeriod
    )
    dataImssEmployee.save()
    log.info "DataImssEmployee: ${dataImssEmployee.dump()}"
    dataImssEmployee
  }

}
