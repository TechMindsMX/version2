package com.modulus.uno.paysheet

import grails.transaction.Transactional
import org.springframework.transaction.annotation.Propagation
import com.modulus.uno.CompanyService
import com.modulus.uno.CorporateService
import com.modulus.uno.BusinessEntityService
import com.modulus.uno.UserService
import com.modulus.uno.RoleService
import com.modulus.uno.Company
import com.modulus.uno.Corporate
import com.modulus.uno.BusinessEntity
import com.modulus.uno.User
import com.modulus.uno.Profile
import com.modulus.uno.Role
import com.modulus.uno.UserRoleCompany
import com.modulus.uno.CompanyStatus
import com.modulus.uno.NameType

class PaysheetProjectService {

  CompanyService companyService
  CorporateService corporateService
  BusinessEntityService businessEntityService
  UserService userService
  RoleService roleService

  @Transactional
  PaysheetProject savePaysheetProject(PaysheetProject paysheetProject) {
    paysheetProject.save()
    paysheetProject
  }

  @Transactional
  void deletePaysheetProject(PaysheetProject paysheetProject) {
    paysheetProject.delete()
  }

  PaysheetProject getPaysheetProjectByPaysheetContractAndName(PaysheetContract paysheetContract, String name) {
    PaysheetProject.findByPaysheetContractAndName(paysheetContract, name)
  }

  List<Company> getCompaniesInCorporate(Long idCompany) {
    Corporate corporate = corporateService.getCorporateFromCompany(idCompany)
    companyService.findCompaniesByCorporateAndStatus(CompanyStatus.ACCEPTED, corporate.id)
  }

  @Transactional
  PayerPaysheetProject savePayerPaysheetProject(PayerPaysheetProject payerPaysheetProject) {
    payerPaysheetProject.save()
    log.info "Payer Paysheet project saved: ${payerPaysheetProject.dump()}"
    payerPaysheetProject  
  }

  @Transactional
  def deletePayer(PayerPaysheetProject payerPaysheetProject) {
    payerPaysheetProject.delete()
  }

  List<BusinessEntity> getAvailableEmployeesToAdd(PaysheetProject paysheetProject) {
    (paysheetProject.paysheetContract.employees - paysheetProject.employees).toList().sort { it.toString() }
  }

  @Transactional
  def addEmployeesToPaysheetProject(PaysheetProject paysheetProject, def params) {
    log.info "Adding selected employees: ${params.entities}"
    List<BusinessEntity> employees = businessEntityService.getBusinessEntitiesFromIds(params.entities)
    paysheetProject.employees.addAll(employees)
    paysheetProject.save()
    log.info "Employees in paysheetProject: ${paysheetProject.employees}"
    paysheetProject
  }
  
  @Transactional
  def deleteEmployeeFromPaysheetProject(PaysheetProject paysheetProject, Long idEmployee){
    BusinessEntity employee = BusinessEntity.get(idEmployee)
    paysheetProject.removeFromEmployees(employee)
    paysheetProject.save()
    paysheetProject
  }

  @Transactional
  BillerPaysheetProject saveBillerPaysheetProject(BillerPaysheetProject billerPaysheetProject) {
    billerPaysheetProject.save()
    log.info "Biller Paysheet project saved: ${billerPaysheetProject.dump()}"
    billerPaysheetProject  
  }

  @Transactional
  def deleteBiller(BillerPaysheetProject billerPaysheetProject) {
    billerPaysheetProject.delete()
  }

  @Transactional
  def generateUsersForEmployeesFromPaysheetProject(PaysheetProject paysheetProject) {
    def employeesWithoutUser = paysheetProject.employees - paysheetProject.users?.businessEntity
    employeesWithoutUser.each { employee ->
      log.info "Create user for employee ${employee.dump()}"
      UserEmployee userEmployee = createUserForPaysheetProjectEmployee(paysheetProject, employee.id)
      log.info "User employee created: ${userEmployee.user.username}"
    }
    paysheetProject
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  UserEmployee createUserForPaysheetProjectEmployee(PaysheetProject paysheetProject, Long businessEntityId) {
    User user = createUserFromEmployee(businessEntityId)

    associateUserEmployeeToCorporate(paysheetProject.paysheetContract.company, user)
    associateUserEmployeeToCompany(paysheetProject.paysheetContract.company, user)

    UserEmployee userEmployee = new UserEmployee (
      user: user,
      businessEntity: BusinessEntity.get(businessEntityId),
      paysheetProject: paysheetProject
    )
    userEmployee.save()
    userEmployee
  }

  User createUserFromEmployee(Long businessEntityId) {
    BusinessEntity businessEntity = BusinessEntity.get(businessEntityId)
    User user = User.findByUsername(businessEntity.rfc)
    if (user) {
      return user
    }

    user = new User (
      username:businessEntity.rfc,
      password:businessEntity.curp,
      enabled:true,
      accountExpired:false,
      accountLocked:false,
      passwordExpired:false
    )

    Profile profile = new Profile (
      name: businessEntity.names.find { it.type == NameType.NOMBRE }.value,
      lastName: businessEntity.names.find { it.type == NameType.APELLIDO_PATERNO }.value,
      motherLastName: businessEntity.names.find { it.type == NameType.APELLIDO_MATERNO }.value,
      email: "fakemail@mail.com"
    )

    userService.createUserWithoutRole(user, profile)
    userService.setAuthorityToUser(user, "ROLE_EMPLOYEE")
    log.info "User created: ${user.dump()}"
    user
  }

  def associateUserEmployeeToCorporate(Company company, User user) {
    Corporate corporateUser = corporateService.findCorporateOfUser(user)
    Corporate corporate = corporateService.getCorporateFromCompany(company.id)

    if (!corporateUser || corporateUser.id != corporate.id) {
      corporateService.addUserToCorporate(corporate.id, user)
    }
  }

  def associateUserEmployeeToCompany(Company company, User user) {
    Role roleEmployee = user.authorities.find { it.authority == "ROLE_EMPLOYEE" } 
    UserRoleCompany currentUserRoleCompany = roleService.findRolesForUserAtThisCompany(user, company)
    if (!currentUserRoleCompany || (currentUserRoleCompany && !currentUserRoleCompany?.roles?.contains(roleEmployee))) {
      roleService.createRolesForUserAtThisCompany([roleEmployee], user, company)
    }
  }
}
