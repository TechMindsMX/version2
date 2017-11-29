package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

import com.modulus.uno.BusinessEntityService
import com.modulus.uno.CorporateService
import com.modulus.uno.BusinessEntity
import com.modulus.uno.Company
import com.modulus.uno.Corporate
import com.modulus.uno.User
import com.modulus.uno.ListEntitiesCommand

@TestFor(PaysheetContractService)
@Mock([PaysheetContract, BusinessEntity, Company, Corporate, User])
class PaysheetContractServiceSpec extends Specification {

  BusinessEntityService businessEntityService = Mock(BusinessEntityService)
  CorporateService corporateService = Mock(CorporateService)

  def setup() {
    service.businessEntityService = businessEntityService
    service.corporateService = corporateService
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

  void "Should get available users to add to paysheet contract"() {
    given:"The corporate with its users"
      Corporate corporate = new Corporate(users:[]).save(validate:false)
      List<User> usersCorp = [new User(username:"A", enabled:true).save(validate:false), new User(username:"B", enabled:true).save(validate:false), new User(username:"C", enabled:true).save(validate:false), new User(username:"D", enabled:true).save(validate:false), new User(username:"E").save(validate:false)]
      corporate.users.addAll(usersCorp)
      corporate.save(validate:false)
      corporateService.getCorporateFromCompany(_) >> corporate
    and:"The paysheetContract with one user"
      PaysheetContract paysheetContract = new PaysheetContract(company:new Company().save(validate:false)).save(validate:false)
      paysheetContract.addToUsers(usersCorp[0])
      paysheetContract.save(validate:false)
    when:
      def result = service.getUsersAvailableToAdd(paysheetContract)
    then:
      result.size() == 3
  }

  void "Should add users into paysheet contract"() {
    given:"the paysheet contract"
      PaysheetContract paysheetContract = new PaysheetContract(users:[]).save(validate:false)
    and:"the ids users"
      ListEntitiesCommand listUsers = new ListEntitiesCommand(checkBe:[1,2,3])
    and:"the current users"
      User user1 = new User(id:1, username:"A").save(validate:false)
      User user2 = new User(id:2, username:"B").save(validate:false)
      User user3 = new User(id:3, username:"C").save(validate:false)
      User user4 = new User(id:4, username:"D").save(validate:false)
      User user5 = new User(id:5, username:"E").save(validate:false)
    when:
      def result = service.addUsersToPaysheetContract(paysheetContract, listUsers)
    then:
      result.users.size() == 3
  }

}
