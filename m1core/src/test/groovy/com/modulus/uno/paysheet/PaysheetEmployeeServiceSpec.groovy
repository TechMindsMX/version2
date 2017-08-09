package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
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

}
