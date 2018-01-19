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
      registrationDate:Date.parse("dd-MM-yyyy", parseRegistrationDateFromRowEmployeeToString(rowEmployee.FECHA_ALTA)),
      baseImssMonthlySalary:new BigDecimal(rowEmployee.SA_BRUTO),
      totalMonthlySalary:new BigDecimal(rowEmployee.SA_BRUTO)+new BigDecimal(rowEmployee.IAS),
      holidayBonusRate:new BigDecimal(rowEmployee.PRIMA_VAC),
      annualBonusDays:new Double(rowEmployee.DIAS_AGUINALDO.toString()).intValue(),
      paymentPeriod:paymentPeriod
    )
    dataImssEmployee.save()
    log.info "DataImssEmployee: ${dataImssEmployee.dump()}"
    dataImssEmployee
	}

	String parseRegistrationDateFromRowEmployeeToString(def registrationDate) {
		String stringDate = ""
		if (registrationDate.class.simpleName == "LocalDate") {
			stringDate = registrationDate.toDate().format("dd-MM-yyyy")
		} else {
			stringDate = registrationDate
		}
		stringDate		
	}

  DataImssEmployee getDataImssForEmployee(EmployeeLink employee) {
    DataImssEmployee.findByEmployee(employee)
  }

}
