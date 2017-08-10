package com.modulus.uno.paysheet

import com.modulus.uno.DataImssEmployeeService
import com.modulus.uno.DataImssEmployee
import com.modulus.uno.EmployeeLink
import java.math.RoundingMode
import grails.transaction.Transactional

class PaysheetEmployeeService {

  BreakdownPaymentEmployeeService breakdownPaymentEmployeeService
  PaysheetProjectService paysheetProjectService
  DataImssEmployeeService dataImssEmployeeService
  def grailsApplication

  @Transactional
  PaysheetEmployee createPaysheetEmployeeFromPrePaysheetEmployee(Paysheet paysheet, PrePaysheetEmployee prePaysheetEmployee) {
    PaysheetEmployee paysheetEmployee = new PaysheetEmployee(
      prePaysheetEmployee:prePaysheetEmployee,
      paysheet:paysheet
    )

    if (prePaysheetEmployee.netPayment > 0) {
      paysheetEmployee.breakdownPayment = breakdownPaymentEmployeeService.generateBreakdownPaymentEmployee(paysheetEmployee)
      paysheetEmployee.salaryImss = calculateImssSalary(paysheetEmployee)
      paysheetEmployee.socialQuota = calculateSocialQuota(paysheetEmployee)
      paysheetEmployee.subsidySalary = calculateSubsidySalary(paysheetEmployee)
      paysheetEmployee.incomeTax = calculateIncomeTax(paysheetEmployee)
      paysheetEmployee.salaryAssimilable = calculateSalaryAssimilable(paysheetEmployee)
      paysheetEmployee.socialQuotaEmployer = calculateSocialQuotaEmployer(paysheetEmployee)
      paysheetEmployee.paysheetTax = calculatePaysheetTax(paysheetEmployee)
      paysheetEmployee.commission = calculateCommission(paysheetEmployee)
    }
    paysheetEmployee.save()
  }

  BigDecimal calculateImssSalary(PaysheetEmployee paysheetEmployee) {
    (getBaseMonthlyImssSalary(paysheetEmployee) / 30 * paysheetEmployee.paysheet.prePaysheet.paymentPeriod.getDays()).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getBaseMonthlyImssSalary(PaysheetEmployee paysheetEmployee) {
    EmployeeLink employeeLink = EmployeeLink.findByEmployeeRef(paysheetEmployee.prePaysheetEmployee.rfc)
    DataImssEmployee dataImssEmployee = dataImssEmployeeService.getDataImssForEmployee(employeeLink)
    dataImssEmployee.baseImssMonthlySalary
  }

  BigDecimal calculateSocialQuota(PaysheetEmployee paysheetEmployee) {
    (paysheetEmployee.breakdownPayment.socialQuotaEmployeeTotal / paysheetEmployee.paysheet.prePaysheet.paymentPeriod.getDays()).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculateSubsidySalary(PaysheetEmployee paysheetEmployee) {
    BigDecimal baseImssMonthlySalary = getBaseMonthlyImssSalary(paysheetEmployee)
    EmploymentSubsidy employmentSubsidy = EmploymentSubsidy.values().find { sb ->
      baseImssMonthlySalary >= sb.lowerLimit && baseImssMonthlySalary <= sb.upperLimit
    }
    employmentSubsidy ? employmentSubsidy.getSubsidy().setScale(2, RoundingMode.HALF_UP) : new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculateIncomeTax(PaysheetEmployee paysheetEmployee) {
    BigDecimal baseImssMonthlySalary = getBaseMonthlyImssSalary(paysheetEmployee)
    RateTax rateTax = RateTax.values().find { rt ->
      baseImssMonthlySalary >= rt.lowerLimit && baseImssMonthlySalary <= rt.upperLimit
    }
    if (!rateTax) {
      return new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
    }

    BigDecimal excess = baseImssMonthlySalary - rateTax.lowerLimit
    BigDecimal marginalTax = excess * (rateTax.rate/100)
    (marginalTax + rateTax.fixedQuota).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculateSalaryAssimilable(PaysheetEmployee paysheetEmployee) {
    (paysheetEmployee.prePaysheetEmployee.netPayment - paysheetEmployee.imssSalaryNet).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculateSocialQuotaEmployer(PaysheetEmployee paysheetEmployee) {
    (paysheetEmployee.breakdownPayment.socialQuotaEmployer / paysheetEmployee.paysheet.prePaysheet.paymentPeriod.getDays()).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculatePaysheetTax(PaysheetEmployee paysheetEmployee) {
    (paysheetEmployee.imssSalaryNet * (new BigDecimal(grailsApplication.config.paysheet.paysheetTax)/100)).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculateCommission(PaysheetEmployee paysheetEmployee) {
    PaysheetProject project = paysheetProjectService.getPaysheetProjectByCompanyAndName(paysheetEmployee.paysheet.company, paysheetEmployee.paysheet.prePaysheet.paysheetProject)
    (paysheetEmployee.paysheetCost * (project.commission/100)).setScale(2, RoundingMode.HALF_UP)
  }

}
