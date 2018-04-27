package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll
import java.text.*
import com.modulus.uno.Company
import com.modulus.uno.User
import com.modulus.uno.Role
import com.modulus.uno.UserRole
import com.modulus.uno.UserRoleCompany
import com.modulus.uno.Corporate
import com.modulus.uno.BusinessEntity
import com.modulus.uno.EmployeeLink
import com.modulus.uno.ComposeName
import com.modulus.uno.NameType
import com.modulus.uno.UserService
import com.modulus.uno.CorporateService
import com.modulus.uno.RoleService

@TestFor(PaysheetProjectService)
@Mock([PaysheetProject, Company, PaysheetContract, Corporate, UserEmployee, EmployeeLink, BusinessEntity, ComposeName, User, UserRole, UserRoleCompany, Role])
class PaysheetProjectServiceSpec extends Specification {

  CorporateService corporateService = Mock(CorporateService)
  UserService userService = Mock(UserService)
  RoleService roleService = Mock(RoleService)

  def setup() {
    service.userService = userService
    service.corporateService = corporateService
    service.roleService = roleService
  }

  void "Should create users for employees from paysheet project when none has user yet"() {
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
      1 * corporateService.addUserToCorporate(_, _)
      1 * roleService.createRolesForUserAtThisCompany(_, _, _)
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
      def result = service.createUserForPaysheetProjectEmployee(paysheetProject, employee1.id)
    then:
      result
  }

  void "Should create users for employees from paysheet project when some have user already"() {
    given:"The paysheet project"
      Company company = new Company(rfc:"COMPANY").save(validate:false)
      PaysheetContract paysheetContract = new PaysheetContract(company:company).save(validate:false)
      PaysheetProject paysheetProject = new PaysheetProject(paysheetContract:paysheetContract).save(validate:false)
    and:"The paysheet project employees"
      BusinessEntity employee1 = new BusinessEntity(rfc:"UNO").save(validate:false)
      employee1.names = [new ComposeName(value:"NUNO", type:NameType.NOMBRE).save(validate:false), new ComposeName(value:"PUNO", type:NameType.APELLIDO_PATERNO).save(validate:false), new ComposeName(value:"MUNO", type:NameType.APELLIDO_MATERNO).save(validate:false)]
      BusinessEntity employee2 = new BusinessEntity(rfc:"DOS").save(validate:false)
      employee2.names = [new ComposeName(value:"NDOS", type:NameType.NOMBRE).save(validate:false), new ComposeName(value:"PDOS", type:NameType.APELLIDO_PATERNO).save(validate:false), new ComposeName(value:"MDOS", type:NameType.APELLIDO_MATERNO).save(validate:false)]
      company.businessEntities = [employee1, employee2]
      company.save(validate:false)
      paysheetProject.employees = [employee1, employee2]
      paysheetProject.save(validate:false)
    and:"The employee link"
      EmployeeLink employeeLink = new EmployeeLink(employeeRef:"UNO", company:company).save(validate:false)
    and:"The user exiting"
      User user = new User(username:"UNO").save(validate:false)
    and:
      corporateService.getCorporateFromCompany(_) >> new Corporate().save(validate:false)
    when:
      def result = service.generateUsersForEmployeesFromPaysheetProject(paysheetProject)
    then:
      1 * userService.createUserWithoutRole(_, _)
      1 * userService.setAuthorityToUser(_, _)
      2 * corporateService.addUserToCorporate(_, _)
      2 * roleService.createRolesForUserAtThisCompany(_, _, _)
  }

  void "Should create users for employees from paysheet project when they have user already and exists in the corporate too"() {
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
    and:"The user exiting"
      User user = new User(username:"UNO").save(validate:false)
    and:
      Corporate corporate = new Corporate().save(validate:false)
      corporateService.getCorporateFromCompany(_) >> corporate
      corporateService.findCorporateOfUser(_) >> corporate
    when:
      def result = service.generateUsersForEmployeesFromPaysheetProject(paysheetProject)
    then:
      0 * userService.createUserWithoutRole(_, _)
      0 * userService.setAuthorityToUser(_, _)
      0 * corporateService.addUserToCorporate(_, _)
      1 * roleService.createRolesForUserAtThisCompany(_, _, _)
  }

  void "Should create users for employees from paysheet project when they have user already and exists in the corporate and in the company too"() {
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
    and:"The user exiting"
      User user = new User(username:"UNO").save(validate:false)
      Role roleEmployee = new Role(authority:"ROLE_EMPLOYEE").save(validate:false)
      UserRole userRole = new UserRole(user:user, role:roleEmployee).save(validate:false)
    and:
      Corporate corporate = new Corporate().save(validate:false)
      corporateService.getCorporateFromCompany(_) >> corporate
      corporateService.findCorporateOfUser(_) >> corporate
    and:
      roleService.findRolesForUserAtThisCompany(_, _) >> new UserRoleCompany(roles:[roleEmployee]).save(validate:false)
    when:
      def result = service.generateUsersForEmployeesFromPaysheetProject(paysheetProject)
    then:
      0 * userService.createUserWithoutRole(_, _)
      0 * userService.setAuthorityToUser(_, _)
      0 * corporateService.addUserToCorporate(_, _)
      0 * roleService.createRolesForUserAtThisCompany(_, _, _)
  }

  void "Should don't create users for employees from paysheet project because exist already"() {
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
    and:"The user exiting"
      User user = new User(username:"UNO").save(validate:false)
    and:
      UserEmployee userEmployee = new UserEmployee(user:user, businessEntity:employee1, paysheetProject:paysheetProject).save(validate:false)
      paysheetProject.addToUsers(userEmployee)
      paysheetProject.save(validate:false)
    and:
      Corporate corporate = new Corporate().save(validate:false)
      corporateService.getCorporateFromCompany(_) >> corporate
      corporateService.findCorporateOfUser(_) >> corporate
    when:
      def result = service.generateUsersForEmployeesFromPaysheetProject(paysheetProject)
    then:
      0 * userService.createUserWithoutRole(_, _)
      0 * userService.setAuthorityToUser(_, _)
      0 * corporateService.addUserToCorporate(_, _)
      0 * roleService.createRolesForUserAtThisCompany(_, _, _)
  }

}
