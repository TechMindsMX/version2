package com.modulus.uno.paysheet

class PaysheetEmployee {

  PrePaysheetEmployee prePaysheetEmployee
  BigDecimal salaryImss = new BigDecimal(0)
  BigDecimal socialQuota = new BigDecimal(0)
  BigDecimal subsidySalary = new BigDecimal(0)
  BigDecimal incomeTax = new BigDecimal(0)
  BigDecimal salaryAssimilable = new BigDecimal(0)
  BigDecimal socialQuotaEmployer = new BigDecimal(0)
  BigDecimal paysheetTax = new BigDecimal(0)
  BigDecimal commission = new BigDecimal(0)
  BigDecimal ivaRate = new BigDecimal(0)
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
