package com.modulus.uno.paysheet

import com.modulus.uno.DataImssEmployeeService
import com.modulus.uno.DataImssEmployee
import com.modulus.uno.EmployeeLink
import com.modulus.uno.PaymentPeriod
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
      paysheet:paysheet,
      breakdownPayment: new BreakdownPaymentEmployee(),
      ivaRate: new BigDecimal(grailsApplication.config.iva).setScale(2, RoundingMode.HALF_UP),
			paymentWay: prePaysheetEmployee.bank ? PaymentWay.BANKING : PaymentWay.CASH
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
    paysheetEmployee
  }

  BigDecimal calculateImssSalary(PaysheetEmployee paysheetEmployee) {
    calculateProportionalAmountFromPaymentPeriod(getBaseMonthlyImssSalary(paysheetEmployee), paysheetEmployee.paysheet.prePaysheet.paymentPeriod)
  }

  BigDecimal getBaseMonthlyImssSalary(PaysheetEmployee paysheetEmployee) {
    EmployeeLink employeeLink = EmployeeLink.findByEmployeeRef(paysheetEmployee.prePaysheetEmployee.rfc)
    DataImssEmployee dataImssEmployee = dataImssEmployeeService.getDataImssForEmployee(employeeLink)
    dataImssEmployee.baseImssMonthlySalary
  }

  BigDecimal calculateSocialQuota(PaysheetEmployee paysheetEmployee) {
    calculateProportionalAmountFromPaymentPeriod(paysheetEmployee.breakdownPayment.socialQuotaEmployeeTotal, paysheetEmployee.paysheet.prePaysheet.paymentPeriod)
  }

  BigDecimal calculateSubsidySalary(PaysheetEmployee paysheetEmployee) {
    BigDecimal baseImssMonthlySalary = getBaseMonthlyImssSalary(paysheetEmployee)
    EmploymentSubsidy employmentSubsidy = EmploymentSubsidy.values().find { sb ->
      baseImssMonthlySalary >= sb.lowerLimit && baseImssMonthlySalary <= sb.upperLimit
    }
    employmentSubsidy ? calculateProportionalAmountFromPaymentPeriod(employmentSubsidy.getSubsidy(), paysheetEmployee.paysheet.prePaysheet.paymentPeriod) : new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
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
    calculateProportionalAmountFromPaymentPeriod(marginalTax + rateTax.fixedQuota, paysheetEmployee.paysheet.prePaysheet.paymentPeriod)
  }

  BigDecimal calculateSalaryAssimilable(PaysheetEmployee paysheetEmployee) {
    (paysheetEmployee.prePaysheetEmployee.netPayment - paysheetEmployee.imssSalaryNet).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculateSocialQuotaEmployer(PaysheetEmployee paysheetEmployee) {
    (paysheetEmployee.breakdownPayment.socialQuotaEmployer / 30 * paysheetEmployee.paysheet.prePaysheet.paymentPeriod.getDays()).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculatePaysheetTax(PaysheetEmployee paysheetEmployee) {
    BigDecimal baseImssMonthlySalary = getBaseMonthlyImssSalary(paysheetEmployee)
    calculateProportionalAmountFromPaymentPeriod(baseImssMonthlySalary * (new BigDecimal(grailsApplication.config.paysheet.paysheetTax)/100), paysheetEmployee.paysheet.prePaysheet.paymentPeriod)    
  }

  BigDecimal calculateCommission(PaysheetEmployee paysheetEmployee) {
    PaysheetProject project = paysheetProjectService.getPaysheetProjectByCompanyAndName(paysheetEmployee.paysheet.company, paysheetEmployee.paysheet.prePaysheet.paysheetProject)
    (paysheetEmployee.paysheetCost * (project.commission/100)).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculateProportionalAmountFromPaymentPeriod(BigDecimal amountMonthly, PaymentPeriod paymentPeriod) {
    (amountMonthly / 30 * paymentPeriod.getDays()).setScale(2, RoundingMode.HALF_UP)
  }

	@Transactional
	void changePaymentWayFromEmployee(PaysheetEmployee employee){
		employee.paymentWay = employee.paymentWay == PaymentWay.BANKING ? PaymentWay.CASH : PaymentWay.BANKING
		employee.save()
	}
}
