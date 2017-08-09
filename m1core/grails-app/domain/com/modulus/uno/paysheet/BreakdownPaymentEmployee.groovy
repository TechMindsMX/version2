package com.modulus.uno.paysheet

import java.math.RoundingMode

class BreakdownPaymentEmployee {

  BigDecimal integratedDailySalary = new BigDecimal(0)
  BigDecimal baseQuotation = new BigDecimal(0)
  BigDecimal fixedFee = new BigDecimal(0)
  BigDecimal diseaseAndMaternityBase = new BigDecimal(0)
  BigDecimal diseaseAndMaternity = new BigDecimal(0)
  BigDecimal diseaseAndMaternityEmployer = new BigDecimal(0)
  BigDecimal pension = new BigDecimal(0)
  BigDecimal pensionEmployer = new BigDecimal(0)
  BigDecimal loan = new BigDecimal(0)
  BigDecimal loanEmployer = new BigDecimal(0)
  BigDecimal disabilityAndLife = new BigDecimal(0)
  BigDecimal disabilityAndLifeEmployer = new BigDecimal(0)
  BigDecimal kindergarten = new BigDecimal(0)
  BigDecimal occupationalRisk = new BigDecimal(0)
  BigDecimal retirementSaving = new BigDecimal(0)
  BigDecimal infonavit = new BigDecimal(0)
  BigDecimal unemploymentAndEld = new BigDecimal(0)
  BigDecimal unemploymentAndEldEmployer = new BigDecimal(0)

  static belongsTo = [paysheetEmployee:PaysheetEmployee]

  BigDecimal getSocialQuotaEmployeeTotal() {
    (this.diseaseAndMaternity + this.pension + this.loan + this.disabilityAndLife + this.unemploymentAndEld).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getSocialQuotaEmployer() {
    (this.fixedFee + this.diseaseAndMaternityEmployer + this.pensionEmployer + this.loanEmployer + this.disabilityAndLifeEmployer + this.kindergarten + this.occupationalRisk + this.retirementSaving + this.unemploymentAndEldEmployer + this.infonavit).setScale(2, RoundingMode.HALF_UP)
  }

}
