package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(EmployeeService)
@Mock([EmployeeLink, Company])
class EmployeeServiceSpec extends Specification {

  void "Should create a employee link from row employee"() {
    given:"The row employee"
      Map rowEmployee = [RFC:"XYZW123456ABC", CURP:"XYZW123456ABCDEF00", NO_EMPL:"NumEmpl"]
    and:"The company"
      Company company = new Company().save(validate:false)
    when:
      def employee = service.createEmployeeForRowEmployee(rowEmployee, company)
    then:
      employee.id
      employee.employeeRef == rowEmployee.RFC
  }

  void "Should not create a employee link from row employee when CURP is wrong"() {
    given:"The row employee"
      Map rowEmployee = [RFC:"XYZ123456ABC", CURP:"AAAA111111ABCDEF00", NO_EMPL:"NumEmpl"]
    and:"The company"
      Company company = new Company().save(validate:false)
    when:
      def employee = service.createEmployeeForRowEmployee(rowEmployee, company)
    then:
      employee.hasErrors()
  }

  void "Should delete a employee"() {
    given:"The Company"
      Company company = new Company().save(validate:false)
    and:"The employee"
      EmployeeLink employeeLink = new EmployeeLink(employeeRef:"TheRFC", company:company).save(validate:false)
    and:"The RFC"
      String rfc = "TheRFC"
    when:
      def result = service.deleteEmployeeLinkForRfcAndCompany(rfc, company)
    then:
      !EmployeeLink.findByEmployeeRefAndCompany(rfc, company)
  }
}
