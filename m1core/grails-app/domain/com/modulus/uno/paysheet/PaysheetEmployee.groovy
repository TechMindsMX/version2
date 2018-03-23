package com.modulus.uno.paysheet

class PaysheetEmployee {

  PrePaysheetEmployee prePaysheetEmployee
  BigDecimal salaryImss = new BigDecimal(0)
  BigDecimal socialQuota = new BigDecimal(0)
  BigDecimal subsidySalary = new BigDecimal(0)
  BigDecimal incomeTax = new BigDecimal(0)
  BigDecimal crudeAssimilable = new BigDecimal(0)
  BigDecimal incomeTaxAssimilable = new BigDecimal(0)
  BigDecimal netAssimilable = new BigDecimal(0)
  BigDecimal socialQuotaEmployer = new BigDecimal(0)
  BigDecimal paysheetTax = new BigDecimal(0)
  BigDecimal commission = new BigDecimal(0)
  BigDecimal ivaRate = new BigDecimal(0)
  PaysheetEmployeeStatus status = PaysheetEmployeeStatus.PENDING
  BreakdownPaymentEmployee breakdownPayment
	PaymentWay paymentWay = PaymentWay.BANKING

  static belongsTo = [paysheet:Paysheet]

  BigDecimal getImssSalaryNet() {
    this.salaryImss - this.socialQuota + this.subsidySalary - this.incomeTax + getTotalIncidencesImssPerceptions() - getTotalIncidencesImssDeductions()
  }

  BigDecimal getTotalSalaryEmployee() {
    getImssSalaryNet() + this.netAssimilable
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

  BigDecimal getTotalIncidencesImssPerceptions() {
    def incidencesPerceptionImss = this.prePaysheetEmployee.incidences.collect { incidence ->
      if (incidence.type != IncidenceType.DEDUCTION && incidence.paymentSchema == PaymentSchema.IMSS) {
        incidence
      }
    }.grep()

    incidencesPerceptionImss ? incidencesPerceptionImss*.exemptAmount.sum() + incidencesPerceptionImss*.taxedAmount.sum() + incidencesPerceptionImss*.extraHourIncidence?.amount.sum() : 0
  }

  BigDecimal getTotalIncidencesImssDeductions() {
    def incidencesDeductionImss = this.prePaysheetEmployee.incidences.collect { incidence ->
      if (incidence.type == IncidenceType.DEDUCTION && incidence.paymentSchema == PaymentSchema.IMSS) {
        incidence
      }
    }.grep()

    incidencesDeductionImss ? incidencesDeductionImss*.exemptAmount.sum() + incidencesDeductionImss*.taxedAmount.sum() : 0
  }

  BigDecimal getTotalIncidencesAssimilablePerceptions() {
    def incidencesPerceptionAssimilable = this.prePaysheetEmployee.incidences.collect { incidence ->
      if (incidence.type != IncidenceType.DEDUCTION && incidence.paymentSchema == PaymentSchema.ASSIMILABLE) {
        incidence
      }
    }.grep()

    incidencesPerceptionAssimilable ? incidencesPerceptionAssimilable*.exemptAmount.sum() + incidencesPerceptionAssimilable*.taxedAmount.sum() + incidencesPerceptionAssimilable*.extraHourIncidence?.amount.sum()  : 0

  }

  BigDecimal getTotalIncidencesAssimilableDeductions() {
    def incidencesDeductionAssimilable = this.prePaysheetEmployee.incidences.collect { incidence ->
      if (incidence.type == IncidenceType.DEDUCTION && incidence.paymentSchema == PaymentSchema.ASSIMILABLE) {
        incidence
      }
    }.grep()

    incidencesDeductionAssimilable ? incidencesDeductionAssimilable*.exemptAmount.sum() + incidencesDeductionAssimilable*.taxedAmount.sum() : 0

  }
}
