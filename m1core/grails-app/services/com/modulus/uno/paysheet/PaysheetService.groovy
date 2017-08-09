package com.modulus.uno.paysheet

class PaysheetService {

  BreakdownPaymentEmployeeService breakdownPaymentEmployeeService

  PaysheetEmployee createPaysheetEmployeeFromPrePaysheetEmployee(Paysheet paysheet, PrePaysheetEmployee prePaysheetEmployee) {
    PaysheetEmployee paysheetEmployee = new PaysheetEmployee(
      prePaysheetEmployee:prePaysheetEmployee,
      paysheet:paysheet
    )
    calculateSalaryImssForEmployee(paysheetEmployee)
    calculateSalaryAssimilable(paysheetEmployee)
  }

  def calculateSalaryImssForEmployee(PaysheetEmployee paysheetEmployee) {
    BreakdownPaymentEmployee breakdownPayment = breakdownPaymentEmployeeService.generateBreakdownPaymentEmployee(paysheetEmployee)
    paysheetEmployee.socialQuota = breakdownPayment.socialQuotaEmployeeTotal
  }


}
