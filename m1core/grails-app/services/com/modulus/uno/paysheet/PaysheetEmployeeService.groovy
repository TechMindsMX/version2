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
			paymentWay: prePaysheetEmployee.bank && prePaysheetEmployee.account ? PaymentWay.BANKING : PaymentWay.CASH
    )

    if (prePaysheetEmployee.netPayment > 0) {
      BigDecimal baseImssMonthlySalary = getBaseMonthlyImssSalary(paysheetEmployee)
      paysheetEmployee.breakdownPayment = breakdownPaymentEmployeeService.generateBreakdownPaymentEmployee(paysheetEmployee)
      paysheetEmployee.salaryImss = calculateImssSalary(paysheetEmployee)
      paysheetEmployee.socialQuota = calculateSocialQuota(paysheetEmployee)
      paysheetEmployee.subsidySalary = calculateSubsidySalary(paysheetEmployee)
      paysheetEmployee.incomeTax = calculateIncomeTax(baseImssMonthlySalary, paysheetEmployee.prePaysheetEmployee.prePaysheet.paymentPeriod)
      paysheetEmployee.netAssimilable = calculateNetAssimilableSalary(paysheetEmployee)
      paysheetEmployee.crudeAssimilable = calculateCrudeAssimilableSalary(paysheetEmployee)
      paysheetEmployee.incomeTaxAssimilable = paysheetEmployee.crudeAssimilable - paysheetEmployee.netAssimilable
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
    EmployeeLink employeeLink = EmployeeLink.findByEmployeeRefAndCompany(paysheetEmployee.prePaysheetEmployee.rfc, paysheetEmployee.paysheet.paysheetContract.company)
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

  BigDecimal calculateIncomeTax(BigDecimal monthlySalary, PaymentPeriod paymentPeriod) {
    RateTax rateTax = getRateTaxForMonthlySalary(monthlySalary)
    if (!rateTax) {
      return new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
    }

    BigDecimal excess = monthlySalary - rateTax.lowerLimit
    BigDecimal marginalTax = excess * (rateTax.rate/100)
    calculateProportionalAmountFromPaymentPeriod(marginalTax + rateTax.fixedQuota, paymentPeriod)
  }

  BigDecimal calculateNetAssimilableSalary(PaysheetEmployee paysheetEmployee) {
    if (getMonthlyAssimilableForEmployee(paysheetEmployee) > 0) {
    BigDecimal netAssimilable = (paysheetEmployee.prePaysheetEmployee.netPayment - paysheetEmployee.imssSalaryNet).setScale(2, RoundingMode.HALF_UP)
    netAssimilable > 0 ? netAssimilable : new BigDecimal(0)
    } else {
      new BigDecimal(0)
    }
  }

  BigDecimal getMonthlyAssimilableForEmployee(PaysheetEmployee paysheetEmployee) {
    EmployeeLink employeeLink = EmployeeLink.findByEmployeeRefAndCompany(paysheetEmployee.prePaysheetEmployee.rfc, paysheetEmployee.paysheet.paysheetContract.company)
    DataImssEmployee dataImssEmployee = dataImssEmployeeService.getDataImssForEmployee(employeeLink)
    dataImssEmployee.monthlyAssimilableSalary
  }

  BigDecimal calculateSocialQuotaEmployer(PaysheetEmployee paysheetEmployee) {
    (paysheetEmployee.breakdownPayment.socialQuotaEmployer / 30 * paysheetEmployee.paysheet.prePaysheet.paymentPeriod.getDays()).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculatePaysheetTax(PaysheetEmployee paysheetEmployee) {
    BigDecimal baseImssMonthlySalary = getBaseMonthlyImssSalary(paysheetEmployee)
    calculateProportionalAmountFromPaymentPeriod(baseImssMonthlySalary * (new BigDecimal(grailsApplication.config.paysheet.paysheetTax)/100), paysheetEmployee.paysheet.prePaysheet.paymentPeriod)    
  }

  BigDecimal calculateCommission(PaysheetEmployee paysheetEmployee) {
    PaysheetProject project = paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(paysheetEmployee.paysheet.paysheetContract, paysheetEmployee.paysheet.prePaysheet.paysheetProject)
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

  BigDecimal calculateCrudeAssimilableSalary(PaysheetEmployee paysheetEmployee) {
    if (paysheetEmployee.netAssimilable > 0) {
      BigDecimal monthlyNetAssimilable = ((paysheetEmployee.netAssimilable / paysheetEmployee.paysheet.prePaysheet.paymentPeriod.days) * 30).setScale(2, RoundingMode.HALF_UP)
      BigDecimal monthlyCrudeAssimilable = calculateCrudeIASFromNetIAS(monthlyNetAssimilable)
      calculateProportionalAmountFromPaymentPeriod(monthlyCrudeAssimilable, paysheetEmployee.paysheet.prePaysheet.paymentPeriod)
    } else {
      new BigDecimal(0)
    }
  }

  BigDecimal calculateCrudeIASFromNetIAS(BigDecimal netIAS) {
    RateTax temporalRateTax = getRateTaxForMonthlySalary(netIAS)
    BigDecimal approximateSalary = (netIAS * (1 + (temporalRateTax.rate / 100))).setScale(2, RoundingMode.HALF_UP)
    RateTax realRateTax = getRateTaxForMonthlySalary(approximateSalary)
    BigDecimal crudeIAS = (netIAS + realRateTax.fixedQuota -(realRateTax.lowerLimit * (realRateTax.rate/100))) / (1 - (realRateTax.rate/100))
    crudeIAS.setScale(2, RoundingMode.HALF_UP)
  }

  RateTax getRateTaxForMonthlySalary(BigDecimal monthlySalary) {
     RateTax.values().find { rt ->
      monthlySalary >= rt.lowerLimit && monthlySalary <= rt.upperLimit
    }
  } 

  PaysheetEmployee setIMSSPayedStatusToEmployee(PaysheetEmployee paysheetEmployee) {
    paysheetEmployee.status = PaysheetEmployeeStatus.IMSS_PAYED
    paysheetEmployee.save()
    paysheetEmployee
  }

  PaysheetEmployee setASSIMILABLEPayedStatusToEmployee(PaysheetEmployee paysheetEmployee) {
    paysheetEmployee.status = PaysheetEmployeeStatus.ASSIMILABLE_PAYED
    paysheetEmployee.save()
    paysheetEmployee
  }

  PaysheetEmployee setPayedStatusToEmployee(PaysheetEmployee paysheetEmployee) {
    paysheetEmployee.status = PaysheetEmployeeStatus.PAYED
    paysheetEmployee.save()
    paysheetEmployee
  }

  PaysheetEmployee setRejectedStatusToEmployee(PaysheetEmployee paysheetEmployee) {
    paysheetEmployee.status = PaysheetEmployeeStatus.REJECTED
    paysheetEmployee.save()
    paysheetEmployee
  }

}
