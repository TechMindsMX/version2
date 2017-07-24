package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(PrePaysheetService)
@Mock([PrePaysheet, PrePaysheetEmployee, BusinessEntity, Company, EmployeeLink, DataImssEmployee])
class PrePaysheetServiceSpec extends Specification {

  BusinessEntityService businessEntityService = Mock(BusinessEntityService)

  def setup() {
    service.businessEntityService = businessEntityService
  }

  void "Should get employees available to add a prepaysheet"() {
    given: "PrePaysheet employees"
      PrePaysheet prePaysheet = new PrePaysheet(company:new Company().save(validate:false)).save(validate:false)
      PrePaysheetEmployee employeePrePaysheet = new PrePaysheetEmployee(rfc:"B").save(validate:false)
      prePaysheet.addToEmployees(employeePrePaysheet)
      prePaysheet.save(validate:false)
    and:
      List allEmployees = [new BusinessEntity(rfc:"A").save(validate:false), new BusinessEntity(rfc:"B").save(validate:false), new BusinessEntity(rfc:"C").save(validate:false)]
      businessEntityService.getAllActiveEmployeesForCompany(_) >> allEmployees
    when:
      def result = service.getEmployeesAvailableToAdd(prePaysheet)
    then:
      result.size() == 2
      result.rfc == ["A", "C"]
  }

  void "Should create and save a employee to prePaysheet"() {
    given:"A prePaysheet"
      Company company = new Company().save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(company:company).save(validate:false)
    and:"A employee"
      BusinessEntity employee = new BusinessEntity(rfc:"RFC").save(validate:false)
      EmployeeLink empLink = new EmployeeLink(curp:"CURP", number:"NOEMP", employeeRef:"RFC").save(validate:false)
    and:
      businessEntityService.getDataImssEmployee(_,_,_) >> null
    when:
      service.createAndSavePrePaysheetEmployee(employee, prePaysheet)
    then:
      prePaysheet.employees.size() == 1
  }

}
