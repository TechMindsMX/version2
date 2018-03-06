package com.modulus.uno

import grails.transaction.Transactional
import com.modulus.uno.paysheet.ContractType
import com.modulus.uno.paysheet.RegimeType
import com.modulus.uno.paysheet.WorkDayType

class DataImssEmployeeService {

  @Transactional
  DataImssEmployee saveDataImss(DataImssEmployee dataImss) {
    if (existsNssInCompanyAlready(dataImss.employee.company, dataImss)) {
      transactionStatus.setRollbackOnly()
      throw new BusinessException("El NSS indicado ya se encuentra registrado en la empresa")
    }
    
    dataImss.save()
    log.info "Data Imss saving: ${dataImss?.dump()}"
    dataImss
  }

  DataImssEmployee createDataImssForRowEmployee(Map rowEmployee, EmployeeLink employee) {
		PaymentPeriod paymentPeriod = PaymentPeriod.find { it.toString() == rowEmployee.PERIODO_PAGO.toUpperCase() }
    ContractType contractType = ContractType.find { it.key == rowEmployee.TIPO_CONTRATO }
    RegimeType regimeType = RegimeType.find { it.key == rowEmployee.TIPO_REGIMEN }
    WorkDayType workDayType = WorkDayType.find { it.key == rowEmployee.TIPO_JORNADA }
    DataImssEmployee dataImssEmployee = new DataImssEmployee(
      employee:employee,
      nss:rowEmployee.NSS,
      registrationDate:Date.parse("dd-MM-yyyy", parseRegistrationDateFromRowEmployeeToString(rowEmployee.FECHA_ALTA)),
      baseImssMonthlySalary:new BigDecimal(rowEmployee.SA_BRUTO),
      totalMonthlySalary:new BigDecimal(rowEmployee.NETO),
      holidayBonusRate:new BigDecimal(rowEmployee.PRIMA_VAC),
      annualBonusDays:new Double(rowEmployee.DIAS_AGUINALDO.toString()).intValue(),
      paymentPeriod:paymentPeriod,
      contractType:contractType,
      regimeType:regimeType,
      workDayType:workDayType,
      department:rowEmployee.DEPARTAMENTO,
      job:rowEmployee.PUESTO
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

  def existsNssInCompanyAlready(Company company, DataImssEmployee dataImssToSave) {
    def criteria = DataImssEmployee.createCriteria()
    def exists = criteria.get {
      employee {
        eq ("company", company)
      }
      eq("nss", dataImssToSave.nss)
    }
    dataImssToSave.id && exists ? dataImssToSave.id != exists.id : !dataImssToSave.id && exists
  }

}
