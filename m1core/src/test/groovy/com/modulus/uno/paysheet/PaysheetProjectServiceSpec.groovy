package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll
import java.text.*
import com.modulus.uno.Company
import com.modulus.uno.Corporate
import com.modulus.uno.BusinessEntity
import com.modulus.uno.EmployeeLink
import com.modulus.uno.ComposeName
import com.modulus.uno.NameType
import com.modulus.uno.UserService
import com.modulus.uno.CorporateService

@TestFor(PaysheetProjectService)
@Mock([PaysheetProject, Company, PaysheetContract, Corporate, UserEmployee, EmployeeLink, BusinessEntity, ComposeName])
class PaysheetProjectServiceSpec extends Specification {

  CorporateService corporateService = Mock(CorporateService)
  UserService userService = Mock(UserService)

  def setup() {
    service.userService = userService
    service.corporateService = corporateService
  }

  void "Should create users for employees from paysheet project"() {
    given:"The paysheet project"
      Company company = new Company(rfc:"COMPANY").save(validate:false)
      PaysheetContract paysheetContract = new PaysheetContract(company:company).save(validate:false)
      PaysheetProject paysheetProject = new PaysheetProject(paysheetContract:paysheetContract).save(validate:false)
    and:"The paysheet project employees"
      BusinessEntity employee1 = new BusinessEntity(rfc:"UNO").save(validate:false)
      employee1.names = [new ComposeName(value:"NUNO", type:NameType.NOMBRE).save(validate:false), new ComposeName(value:"PUNO", type:NameType.APELLIDO_PATERNO).save(validate:false), new ComposeName(value:"MUNO", type:NameType.APELLIDO_MATERNO).save(validate:false)]
      company.businessEntities = [employee1]
      company.save(validate:false)
      paysheetProject.employees = [employee1]
      paysheetProject.save(validate:false)
    and:"The employee link"
      EmployeeLink employeeLink = new EmployeeLink(employeeRef:"UNO", company:company).save(validate:false)
    and:
      corporateService.getCorporateFromCompany(_) >> new Corporate().save(validate:false)
    when:
      def result = service.generateUsersForEmployeesFromPaysheetProject(paysheetProject)
    then:
      1 * userService.createUserWithoutRole(_, _)
      1 * userService.setAuthorityToUser(_, _)
  }

  void "Should create user for a paysheet project employee"() {
    given:"The paysheet project"
      Company company = new Company(rfc:"COMPANY").save(validate:false)
      PaysheetContract paysheetContract = new PaysheetContract(company:company).save(validate:false)
      PaysheetProject paysheetProject = new PaysheetProject(paysheetContract:paysheetContract).save(validate:false)
    and:"The employee"
      BusinessEntity employee1 = new BusinessEntity(rfc:"UNO").save(validate:false)
      employee1.names = [new ComposeName(value:"NUNO", type:NameType.NOMBRE).save(validate:false), new ComposeName(value:"PUNO", type:NameType.APELLIDO_PATERNO).save(validate:false), new ComposeName(value:"MUNO", type:NameType.APELLIDO_MATERNO).save(validate:false)]
      company.businessEntities = [employee1]
      company.save(validate:false)
    and:
      corporateService.getCorporateFromCompany(_) >> new Corporate().save(validate:false)
    when:
      def result = service.createUserForPaysheetProjectEmployee(paysheetProject, employee1)
    then:
      result
  }

}
