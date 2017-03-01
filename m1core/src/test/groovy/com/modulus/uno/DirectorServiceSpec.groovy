package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import java.lang.Void as Should
import grails.test.mixin.Mock

@TestFor(DirectorService)
@Mock([Company,Role,User,UserRoleCompany])
class DirectorServiceSpec extends Specification {

  Should "get the users of a company"(){
    given:"the company and the users of the company with their roles"
      Company company = createCompanyAndUsers()
    when:
      ArrayList<User> companyUsers = service.findUsersOfCompany(company.id)
    then:
      companyUsers.size() == 5
  }

  Should "get the users of a company by role"(){
    given:"the company"
      Company company = createCompanyAndUsers()
    and:"the role to looking for"
      String role = "ROLE_M1"
    when:
      ArrayList<User> companyUsersByRole = service.findUsersOfCompanyByRole(company.id,[role])
    then:
      companyUsersByRole.size() == 1
  }

 Should "get the users of a company by more roles"(){
    given:"the company"
      Company company = createCompanyAndUsers()
    and:"the role to looking for"
      String role1 = "ROLE_M1"
      String role2 = "ROLE_AUTHORIZER_VISOR"
      String role3 = "ROLE_FICO_EJECUTOR"
      String role4 = "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"
    when:
      ArrayList<User> companyUsersByRole = service.findUsersOfCompanyByRole(company.id, [role1, role2, role3, role4])
    then:
      companyUsersByRole.size() == 4
  }

  private Company createCompanyAndUsers(){
    Company company = new Company(rfc:"JIGE930831GJ1",
                                  bussinessName:"Organization")
    company.save(validate:false)

    ArrayList<User> users = [new User(username:"egjimenezg"),
                             new User(username:"gamalieljg"),
                              new User(username:"carlo"),
                              new User(username:"gilmar"),
                              new User(username:"carlogilmar")]
    users.each{ user ->
      user.save(validate:false)
    }

    ArrayList<Role> roles = [new Role(authority:"ROLE_M1"),
                             new Role(authority:"ROLE_CORPORATIVE"),
                             new Role(authority:"ROLE_AUTHORIZER_VISOR"),
                             new Role(authority:"ROLE_FICO_EJECUTOR"),
                             new Role(authority:"ROLE_LEGAL_REPRESENTATIVE_EJECUTOR")]

    roles.each{ role -> role.save(validate:false) }

    users.eachWithIndex{ user,index ->
      UserRoleCompany userRole = new UserRoleCompany(user:user,company:company)
      userRole.addToRoles(roles[index])
      userRole.save()
    }

    company
  }

}
