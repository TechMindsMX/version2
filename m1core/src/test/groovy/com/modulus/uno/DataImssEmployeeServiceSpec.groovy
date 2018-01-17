package com.modulus.uno

import spock.lang.Specification
import spock.lang.Unroll
import grails.test.mixin.TestFor
import grails.test.mixin.Mock

@TestFor(DataImssEmployeeService)
@Mock([DataImssEmployee, EmployeeLink])
class DataImssEmployeeServiceSpec extends Specification {

  void "Should save the data imss employee"() {
    given:"A employee"
      EmployeeLink employee = new EmployeeLink().save(validate:false)
    and:"The data imss"
      DataImssEmployee dataImss = new DataImssEmployee(employee:employee, nss:"NSS", registrationDate:new Date(), baseImssMonthlySalary:new BigDecimal(1000), totalMonthlySalary:new BigDecimal(2500), holidayBonusRate:new BigDecimal(25), annualBonusDays:15, paymentPeriod:PaymentPeriod.BIWEEKLY)
    when:
      def result = service.saveDataImss(dataImss)
    then:
      result.id
  }

  @Unroll
  void "Should don't save the data imss employee"() {
    when:
      def result = service.saveDataImss(dataImss)
    then:
      result.hasErrors()
    where:
    dataImss  ||  errors
    new DataImssEmployee(employee:new EmployeeLink().save(validate:false), nss:"NSS", registrationDate:new Date(), baseImssMonthlySalary:new BigDecimal(1000), totalMonthlySalary:new BigDecimal(2500), holidayBonusRate:new BigDecimal(25), annualBonusDays:10, paymentPeriod:PaymentPeriod.BIWEEKLY) ||  true
    new DataImssEmployee(employee:new EmployeeLink().save(validate:false), nss:"NSS", registrationDate:new Date(), baseImssMonthlySalary:new BigDecimal(1000), totalMonthlySalary:new BigDecimal(2500), holidayBonusRate:new BigDecimal(120), annualBonusDays:10, paymentPeriod:PaymentPeriod.BIWEEKLY) ||  true

  }

  void "Should create a data imss employee from row employee massive"() {
    given:"The row employee"
      Map rowEmployee = [NSS:"NssEmployee", FECHA_ALTA:"10-02-2010", SA_BRUTO:"1000", IAS:"5000", PRIMA_VAC:"25", DIAS_AGUINALDO:"15", PERIODO_PAGO:"Quincenal"]
    and:"The employee link"
      EmployeeLink employeeLink = new EmployeeLink().save(validate:false)
    when:
      def dataImss = service.createDataImssForRowEmployee(rowEmployee, employeeLink)
    then:
      dataImss.id
  }

}
