package com.modulus.uno.paysheet

import com.modulus.uno.DataImssEmployeeService
import com.modulus.uno.DataImssEmployee
import com.modulus.uno.EmployeeLink
import com.modulus.uno.BusinessException
import java.math.RoundingMode
import grails.transaction.Transactional

class BreakdownPaymentEmployeeService {

  def grailsApplication
  DataImssEmployeeService dataImssEmployeeService
  PaysheetProjectService paysheetProjectService

  @Transactional
  BreakdownPaymentEmployee generateBreakdownPaymentEmployee(PaysheetEmployee paysheetEmployee) {
    EmployeeLink employee = EmployeeLink.findByEmployeeRef(paysheetEmployee.prePaysheetEmployee.rfc)
    if (!employee) {
      throw new BusinessException("El empleado de la prenómina con RFC ${paysheetEmployee.prePaysheetEmployee.rfc} ya no fue encontrado en los registros de la empresa")
    }
    DataImssEmployee dataImssEmployee = dataImssEmployeeService.getDataImssForEmployee(employee)
    if (!dataImssEmployee) {
      log.error "Data Imss for employee not found: ${employeeLink.dump()}"
      throw new BusinessException("El empleado con RFC ${employeeLink.employeeRef} no tiene los datos del IMSS requeridos")
    }

    BreakdownPaymentEmployee breakdownPayment = new BreakdownPaymentEmployee()
    if (dataImssEmployee.baseImssMonthlySalary > 0) {
      BigDecimal integratedDailySalary = getIntegratedDailySalaryForEmployee(dataImssEmployee, paysheetEmployee.paysheet)
      BigDecimal baseQuotation = getBaseQuotation(integratedDailySalary)
      BigDecimal diseaseAndMaternityBase = getDiseaseAndMaternityBase(integratedDailySalary)
      breakdownPayment = new BreakdownPaymentEmployee(
        integratedDailySalary: integratedDailySalary,
        baseQuotation: baseQuotation,
        fixedFee: getFixedFee(),
        diseaseAndMaternityBase: diseaseAndMaternityBase,
        diseaseAndMaternityEmployer: getDiseaseAndMaternityEmployer(diseaseAndMaternityBase),
        diseaseAndMaternity: getDiseaseAndMaternityEmployee(diseaseAndMaternityBase),
        pension: getPensionEmployee(baseQuotation),
        pensionEmployer: getPensionEmployer(baseQuotation),
        loan: getLoanEmployee(baseQuotation),
        loanEmployer: getLoanEmployer(baseQuotation),
        disabilityAndLife: getDisabilityAndLifeEmployee(integratedDailySalary),
        disabilityAndLifeEmployer: getDisabilityAndLifeEmployer(integratedDailySalary),
        kindergarten: getKindergarten(baseQuotation),
        occupationalRisk: getOccupationalRisk(baseQuotation, paysheetEmployee.paysheet),
        retirementSaving: getRetirementSaving(baseQuotation),
        unemploymentAndEld: getUnemploymentAndEldEmployee(baseQuotation),
        unemploymentAndEldEmployer: getUnemploymentAndEldEmployer(baseQuotation),
        infonavit: getInfonavit(baseQuotation)
      )
    }

    breakdownPayment.save()
    breakdownPayment
  }

  BigDecimal getIntegratedDailySalaryForEmployee(DataImssEmployee dataImssEmployee, Paysheet paysheet) {
    PaysheetProject project = paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(paysheet.paysheetContract, paysheet.prePaysheet.paysheetProject)
    (dataImssEmployee.baseImssMonthlySalary / 30 * project.integrationFactor).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getBaseQuotation(BigDecimal integratedDailySalary) {
    integratedDailySalary * new BigDecimal(grailsApplication.config.paysheet.quotationDays)
  }

  BigDecimal getFixedFee() {
    new BigDecimal(grailsApplication.config.paysheet.uma) * new BigDecimal(grailsApplication.config.paysheet.quotationDays) * (new BigDecimal(grailsApplication.config.paysheet.fixedFee)/100)
  }

  BigDecimal getDiseaseAndMaternityBase(BigDecimal integratedDailySalary) {
    BigDecimal limit = 3 * new BigDecimal(grailsApplication.config.paysheet.uma)
    BigDecimal diseaseAndMaternityBase = new BigDecimal(0)
    if (integratedDailySalary > limit) {
      diseaseAndMaternityBase = (integratedDailySalary - limit) * new BigDecimal(grailsApplication.config.paysheet.quotationDays)
    }
    diseaseAndMaternityBase.setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getDiseaseAndMaternityEmployer(BigDecimal diseaseAndMaternityBase) {
    (diseaseAndMaternityBase * (new BigDecimal(grailsApplication.config.paysheet.diseaseAndMaternityEmployer)/100)).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getDiseaseAndMaternityEmployee(BigDecimal diseaseAndMaternityBase) {
    (diseaseAndMaternityBase * (new BigDecimal(grailsApplication.config.paysheet.diseaseAndMaternityEmployee)/100)).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getPensionEmployer(BigDecimal baseQuotation) {
    (baseQuotation * (new BigDecimal(grailsApplication.config.paysheet.pensionEmployer)/100)).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getPensionEmployee(BigDecimal baseQuotation) {
    (baseQuotation * (new BigDecimal(grailsApplication.config.paysheet.pensionEmployee)/100)).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getLoanEmployer(BigDecimal baseQuotation) {
    (baseQuotation * (new BigDecimal(grailsApplication.config.paysheet.loanEmployer)/100)).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getLoanEmployee(BigDecimal baseQuotation) {
    (baseQuotation * (new BigDecimal(grailsApplication.config.paysheet.loanEmployee)/100)).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getDisabilityAndLifeEmployer(BigDecimal integratedDailySalary) {
    BigDecimal limit = new BigDecimal(grailsApplication.config.paysheet.uma) * 25
    BigDecimal disabilityAndLifeEmployer = integratedDailySalary * new BigDecimal(grailsApplication.config.paysheet.quotationDays) * (new BigDecimal(grailsApplication.config.paysheet.disabilityAndLifeEmployer)/100)
    if (integratedDailySalary > limit) {
      disabilityAndLifeEmployer = limit * new BigDecimal(grailsApplication.config.paysheet.quotationDays) * (new BigDecimal(grailsApplication.config.paysheet.disabilityAndLifeEmployer)/100)
    }
    disabilityAndLifeEmployer.setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getDisabilityAndLifeEmployee(BigDecimal integratedDailySalary) {
    BigDecimal limit = new BigDecimal(grailsApplication.config.paysheet.uma) * 25
    BigDecimal disabilityAndLifeEmployee = integratedDailySalary * new BigDecimal(grailsApplication.config.paysheet.quotationDays) * (new BigDecimal(grailsApplication.config.paysheet.disabilityAndLifeEmployee)/100)
    if (integratedDailySalary > limit) {
      disabilityAndLifeEmployee = limit * new BigDecimal(grailsApplication.config.paysheet.quotationDays) * (new BigDecimal(grailsApplication.config.paysheet.disabilityAndLifeEmployee)/100)
    }
    disabilityAndLifeEmployee.setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getKindergarten(BigDecimal baseQuotation) {
    (baseQuotation * (new BigDecimal(grailsApplication.config.paysheet.kindergarten)/100)).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getOccupationalRisk(BigDecimal baseQuotation, Paysheet paysheet) {
    PaysheetProject project = paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(paysheet.paysheetContract, paysheet.prePaysheet.paysheetProject)
    (baseQuotation * (project.occupationalRiskRate/100)).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getRetirementSaving(BigDecimal baseQuotation) {
    (baseQuotation * (new BigDecimal(grailsApplication.config.paysheet.retirementSaving)/100)).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getUnemploymentAndEldEmployer(BigDecimal baseQuotation) {
    (baseQuotation * (new BigDecimal(grailsApplication.config.paysheet.unemploymentAndEldEmployer)/100)).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getUnemploymentAndEldEmployee(BigDecimal baseQuotation) {
    (baseQuotation * (new BigDecimal(grailsApplication.config.paysheet.unemploymentAndEldEmployee)/100)).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getInfonavit(BigDecimal baseQuotation) {
    (baseQuotation * (new BigDecimal(grailsApplication.config.paysheet.infonavit)/100)).setScale(2, RoundingMode.HALF_UP)
  }

}
