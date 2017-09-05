package com.modulus.uno

import spock.lang.Specification
import grails.test.mixin.TestFor
import grails.test.mixin.Mock

@TestFor(DataImssEmployeeService)
@Mock([DataImssEmployee, EmployeeLink])
class DataImssEmployeeServiceSpec extends Specification {

  void "Should save the data imss employee"() {
    given:"A employee"
      EmployeeLink employee = new EmployeeLink().save(validate:false)
    and:"The data imss"
      DataImssEmployee dataImss = new DataImssEmployee(employee:employee, nss:"NSS", registrationDate:new Date(), baseImssMonthlySalary:new BigDecimal(1000), netMonthlySalary:new BigDecimal(2500), holidayBonusRate:new BigDecimal(25), annualBonusDays:15, paymentPeriod:PaymentPeriod.BIWEEKLY)
    when:
      def result = service.saveDataImss(dataImss)
    then:
      result.id
  }
}
