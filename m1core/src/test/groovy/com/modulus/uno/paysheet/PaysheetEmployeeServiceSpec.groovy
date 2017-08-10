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

@TestFor(PaysheetEmployeeService)
@Mock([PaysheetEmployee, Paysheet, PrePaysheet, PrePaysheetEmployee, DataImssEmployee, EmployeeLink])
class PaysheetEmployeeServiceSpec extends Specification {

  DataImssEmployeeService dataImssEmployeeService = Mock(DataImssEmployeeService)

  def setup() {
    service.dataImssEmployeeService = dataImssEmployeeService
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
      new BigDecimal(0.01).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(407.02).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(777.06).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(407.02).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(1768.96).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(407.02).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(1768.97).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(406.83).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(2000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(406.83).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(2653.38).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(406.83).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(2653.39).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(406.62).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(406.62).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3472.84).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(406.62).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3472.85).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(392.77).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3500).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(392.77).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3537.87).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(392.77).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3537.88).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(382.46).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(382.46).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4446.15).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(382.46).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4446.16).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(354.23).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4600).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(354.23).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4717.18).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(354.23).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4717.19).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(324.87).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4777.06).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(324.87).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(5335.42).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(324.87).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(5335.43).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(294.63).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(6000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(294.63).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(6224.67).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(294.63).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(6224.68).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(253.54).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(253.54).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7113.90).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(253.54).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7113.91).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(217.61).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7300).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(217.61).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7382.33).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(217.61).setScale(2, RoundingMode.HALF_UP)
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
      new BigDecimal(4777.06).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(308.88).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4342.94).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(261.65).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7452.56).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(602.74).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(6507.12).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(497.11).setScale(2, RoundingMode.HALF_UP)
  }

}
