package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

import com.modulus.uno.BusinessEntityService
import com.modulus.uno.BusinessEntity
import com.modulus.uno.Company

@TestFor(PaysheetContractService)
@Mock([PaysheetContract, BusinessEntity, Company])
class PaysheetContractServiceSpec extends Specification {

  BusinessEntityService businessEntityService = Mock(BusinessEntityService)

  def setup() {
    service.businessEntityService = businessEntityService
  }

  void "Should get available employees to add to paysheet contract"() {
    given:"The company and the employees"
      Company company = new Company().save(validate:false)
      BusinessEntity be1 = new BusinessEntity().save(validate:false)
      BusinessEntity be2 = new BusinessEntity().save(validate:false)
      BusinessEntity be3 = new BusinessEntity().save(validate:false)
      BusinessEntity be4 = new BusinessEntity().save(validate:false)
      company.addToBusinessEntities(be1)
      company.addToBusinessEntities(be2)
      company.addToBusinessEntities(be3)
      company.addToBusinessEntities(be4)
      company.save(validate:false)
    and:
      PaysheetContract paysheetContract = new PaysheetContract(company:company).save(validate:false)
      paysheetContract.addToEmployees(be2)
      paysheetContract.addToEmployees(be3)
      paysheetContract.save(validate:false)
    and:"The company employees"
      businessEntityService.getAllActiveEmployeesForCompany(_) >> [be1, be2, be3, be4]
    when:
      def result = service.getEmployeesAvailableToAdd(paysheetContract)
    then:
      result.size() == 2
  }

  void "Should add employees into paysheet contract"() {
    given:
      PaysheetContract paysheetContract = new PaysheetContract(employees:[]).save(validate:false)
    and:
      def params = [entities:"1,2"]
      businessEntityService.getBusinessEntitiesFromIds(_) >> [new BusinessEntity().save(validate:false), new BusinessEntity().save(validate:false)]
    when:
      def result = service.addEmployeesToPaysheetContract(paysheetContract, params)
    then:
      result.employees.size() == 2
  }

  void "Should remove employee from paysheet contract"() {
    given:
      PaysheetContract paysheetContract = new PaysheetContract().save(validate:false)
      BusinessEntity be1 = new BusinessEntity().save(validate:false)
      BusinessEntity be2 = new BusinessEntity().save(validate:false)
      paysheetContract.addToEmployees(be1)
      paysheetContract.addToEmployees(be2)
      paysheetContract.save(validate:false)
    and:
      Long idEmployee = 1
    when:
      def result = service.deleteEmployeeFromPaysheetContract(paysheetContract, idEmployee)
    then:
      result.employees.size() == 1
  }

}
