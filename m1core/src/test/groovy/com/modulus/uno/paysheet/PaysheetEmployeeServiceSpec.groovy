package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll
import java.math.RoundingMode

import com.modulus.uno.DataImssEmployee
import com.modulus.uno.DataImssEmployeeService
import com.modulus.uno.PaymentPeriod
import com.modulus.uno.EmployeeLink
import com.modulus.uno.Company
import com.modulus.uno.Bank

@TestFor(PaysheetEmployeeService)
@Mock([PaysheetEmployee, Paysheet, PrePaysheet, PrePaysheetEmployee, DataImssEmployee, EmployeeLink, Company, BreakdownPaymentEmployee, PaysheetProject, Bank])
class PaysheetEmployeeServiceSpec extends Specification {

  DataImssEmployeeService dataImssEmployeeService = Mock(DataImssEmployeeService)
  BreakdownPaymentEmployeeService breakdownPaymentEmployeeService = Mock(BreakdownPaymentEmployeeService)
  PaysheetProjectService paysheetProjectService = Mock(PaysheetProjectService)

  def setup() {
    service.dataImssEmployeeService = dataImssEmployeeService
    service.breakdownPaymentEmployeeService = breakdownPaymentEmployeeService
    service.paysheetProjectService = paysheetProjectService
    grailsApplication.config.paysheet.paysheetTax = "3.00"
    grailsApplication.config.iva = 16
  }

  void "Should calculate imss salary for employee"() {
    given:"The paysheet employee"
      PrePaysheet prePaysheet = new PrePaysheet(paymentPeriod:PaymentPeriod.WEEKLY).save(validate:false)
      Paysheet paysheet = new Paysheet(prePaysheet:prePaysheet).save(validate:false)
      PrePaysheetEmployee prePaysheetEmployee = new PrePaysheetEmployee(rfc:"RFC").save(validate:false)
      PaysheetEmployee paysheetEmployee = new PaysheetEmployee(paysheet:paysheet, prePaysheetEmployee:prePaysheetEmployee).save(validate:false)
    and:
      dataImssEmployeeService.getDataImssForEmployee(_) >> new DataImssEmployee(baseImssMonthlySalary:new BigDecimal(4714.12))
    when:
      BigDecimal salaryImss = service.calculateImssSalary(paysheetEmployee)
    then:
      salaryImss == new BigDecimal(1099.96).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate the salary subsidy = #subs for employee with base salary = #bs"() {
    given:"The paysheet employee"
      PrePaysheet prePaysheet = new PrePaysheet(paymentPeriod:PaymentPeriod.WEEKLY).save(validate:false)
      Paysheet paysheet = new Paysheet(prePaysheet:prePaysheet).save(validate:false)
      PrePaysheetEmployee prePaysheetEmployee = new PrePaysheetEmployee(rfc:"RFC").save(validate:false)
      PaysheetEmployee paysheetEmployee = new PaysheetEmployee(paysheet:paysheet, prePaysheetEmployee:prePaysheetEmployee).save(validate:false)
    and:
      dataImssEmployeeService.getDataImssForEmployee(_) >> new DataImssEmployee(baseImssMonthlySalary:bs)
    when:
      BigDecimal subsidy = service.calculateSubsidySalary(paysheetEmployee)
    then:
      subsidy == subs
    where:
      bs                                                         ||  subs
      new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(0.01).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.97).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(777.06).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.97).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(1768.96).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.97).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(1768.97).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.93).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(2000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.93).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(2653.38).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.93).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(2653.39).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.88).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.88).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3472.84).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.88).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3472.85).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(91.65).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3500).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(91.65).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3537.87).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(91.65).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3537.88).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(89.24).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(89.24).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4446.15).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(89.24).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4446.16).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(82.65).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4600).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(82.65).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4717.18).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(82.65).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4717.19).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(75.80).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4777.06).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(75.80).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(5335.42).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(75.80).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(5335.43).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(68.75).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(6000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(68.75).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(6224.67).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(68.75).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(6224.68).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(59.16).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(59.16).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7113.90).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(59.16).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7113.91).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(50.78).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7300).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(50.78).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7382.33).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(50.78).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7382.34).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(10000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate the income tax = #it for employee with base salary = #bs"() {
    given:"The paysheet employee"
      PrePaysheet prePaysheet = new PrePaysheet(paymentPeriod:PaymentPeriod.WEEKLY).save(validate:false)
      Paysheet paysheet = new Paysheet(prePaysheet:prePaysheet).save(validate:false)
      PrePaysheetEmployee prePaysheetEmployee = new PrePaysheetEmployee(rfc:"RFC").save(validate:false)
      PaysheetEmployee paysheetEmployee = new PaysheetEmployee(paysheet:paysheet, prePaysheetEmployee:prePaysheetEmployee).save(validate:false)
    and:
      dataImssEmployeeService.getDataImssForEmployee(_) >> new DataImssEmployee(baseImssMonthlySalary:bs)
    when:
      BigDecimal incomeTax = service.calculateIncomeTax(paysheetEmployee)
    then:
      incomeTax == it
    where:
      bs                                                         ||  it
      new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4777.06).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(72.07).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4342.94).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(61.05).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7452.56).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(140.64).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(6507.12).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(115.99).setScale(2, RoundingMode.HALF_UP)
  }

  void "Should create a paysheet employee from a prepaysheet employee"() {
    given:"the paysheet"
      PrePaysheet prePaysheet = new PrePaysheet(paymentPeriod:PaymentPeriod.WEEKLY).save(validate:false)
      Paysheet paysheet = new Paysheet(prePaysheet:prePaysheet, company:new Company().save(validate:false)).save(validate:false)
    and:"the prePaysheet Employee"
      PrePaysheetEmployee prePaysheetEmployee = new PrePaysheetEmployee(rfc:"RFC", netPayment:new BigDecimal(5000), bank:new Bank().save(validate:false)).save(validate:false)
      PaysheetEmployee paysheetEmployee = new PaysheetEmployee(paysheet:paysheet, prePaysheetEmployee:prePaysheetEmployee).save(validate:false)
    and:
      BreakdownPaymentEmployee breakdownPaymentEmployee = new BreakdownPaymentEmployee(diseaseAndMaternity:new BigDecimal(0), pension:new BigDecimal(18.72), loan:new BigDecimal(12.48), disabilityAndLife: new BigDecimal(31.21), unemploymentAndEld:new BigDecimal(56.17), fixedFee:new BigDecimal(468.16), diseaseAndMaternityEmployer:new BigDecimal(0), pensionEmployer:new BigDecimal(52.43), loanEmployer:new BigDecimal(34.95), disabilityAndLifeEmployer:new BigDecimal(87.38), kindergarten:new BigDecimal(49.93), occupationalRisk:new BigDecimal(27.14), retirementSaving:new BigDecimal(99.86), unemploymentAndEldEmployer:new BigDecimal(157.28), infonavit:new BigDecimal(249.65), paysheetEmployee:paysheetEmployee).save(validate:false)
      PaysheetProject paysheetProject = new PaysheetProject(commission:new BigDecimal(5)).save(validate:false)
      DataImssEmployee dataImssEmployee = new DataImssEmployee(baseImssMonthlySalary:new BigDecimal(4714.12))
    and:
      breakdownPaymentEmployeeService.generateBreakdownPaymentEmployee(_) >> breakdownPaymentEmployee
      dataImssEmployeeService.getDataImssForEmployee(_) >> dataImssEmployee
      paysheetProjectService.getPaysheetProjectByCompanyAndName(_, _) >> paysheetProject
    when:
      PaysheetEmployee result = service.createPaysheetEmployeeFromPrePaysheetEmployee(paysheet, prePaysheetEmployee)
    then:
      result.id
      result.salaryImss == 1099.96
      result.socialQuota == 27.67
      result.socialQuotaEmployer == 286.25
			result.paymentWay == PaymentWay.BANKING
  }

}
