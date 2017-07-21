package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(PrePaysheetService)
@Mock([PrePaysheet, PrePaysheetEmployee, BusinessEntity, Company])
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
}
