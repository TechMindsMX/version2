package com.modulus.uno.paysheet

import com.modulus.uno.DataImssEmployeeService
import com.modulus.uno.DataImssEmployee
import com.modulus.uno.EmployeeLink
import java.math.RoundingMode

class PaysheetEmployeeService {

  BreakdownPaymentEmployeeService breakdownPaymentEmployeeService
  DataImssEmployeeService dataImssEmployeeService

  PaysheetEmployee createPaysheetEmployeeFromPrePaysheetEmployee(Paysheet paysheet, PrePaysheetEmployee prePaysheetEmployee) {
    PaysheetEmployee paysheetEmployee = new PaysheetEmployee(
      prePaysheetEmployee:prePaysheetEmployee,
      paysheet:paysheet
    )
    paysheetEmployee.breakdownPayment = breakdownPaymentEmployeeService.generateBreakdownPaymentEmployee(paysheetEmployee)
    paysheetEmployee.salaryImss = calculateImssSalary(paysheetEmployee)
    paysheetEmployee.socialQuota = calculateSocialQuota(paysheetEmployee)
    paysheetEmployee.socialQuotaEmployer = calculateSocialQuotaEmployer(paysheetEmployee)
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

  BigDecimal calculateSocialQuotaEmployer(PaysheetEmployee paysheetEmployee) {
    (paysheetEmployee.breakdownPayment.socialQuotaEmployer / paysheetEmployee.paysheet.prePaysheet.paymentPeriod.getDays()).setScale(2, RoundingMode.HALF_UP)
  }

}
