package com.modulus.uno.paysheet

class PaysheetEmployee {

  PrePaysheetEmployee prePaysheetEmployee
  BigDecimal salaryImss
  BigDecimal socialQuota
  BigDecimal subsidySalary
  BigDecimal incomeTax
  BigDecimal salaryAssimilable
  BigDecimal socialQuotaEmployer
  BigDecimal paysheetTax
  BigDecimal commission
  BigDecimal ivaRate
  PaysheetEmployeeStatus status
  BreakdownPaymentEmployee breakdownPayment

  static belongsTo = [paysheet:Paysheet]

  BigDecimal getImssSalaryNet() {
    this.salaryImss - this.socialQuota + this.subsidySalary - this.incomeTax
  }

  BigDecimal getTotalSalaryEmployee() {
    getImssSalaryNet() + this.salaryAssimilable
  }

  BigDecimal getPaysheetCost() {
    getTotalSalaryEmployee() + this.socialQuotaEmployer + this.paysheetTax
  }

  BigDecimal getPaysheetTotal() {
    getPaysheetCost() + this.commission
  }

  BigDecimal getPaysheetIva() {
    getPaysheetTotal() * (this.ivaRate / 100)
  }

  BigDecimal getTotalToInvoice() {
    getPaysheetTotal() + getPaysheetIva()
  }
}
