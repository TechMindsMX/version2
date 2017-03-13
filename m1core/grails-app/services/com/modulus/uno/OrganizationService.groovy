package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class OrganizationService {

  RoleService roleService
  UserRoleService userRoleService

  User createRolesForUserInCompanies(User user, Map rolesForCompanies){
    rolesForCompanies.each { companyName, rolesForThisCompany ->
      Company company = Company.findByUuid(companyName)

      List<Role> roles = rolesForThisCompany.collect { role, value ->
        Role.findByAuthority(role)
      }
      if (rolesForThisCompany) {
        roleService.createRolesForUserAtThisCompany(roles, user, company)
      }
    }
    user
  }

  List<UserRoleCompany> findRolesForUserInCompanies(String username, Corporate corporate){
    User user = User.findByUsername(username)
    corporate.companies.collect{ Company company ->
      roleService.findRolesForUserAtThisCompany(user, company)
    }.findAll{ it }
  }

  User deleteRolesForUserInCompanies(User user, def companies){
    companies.each{ Company company ->
      roleService.deleteRolesForUserAtThisCompany(user, company)
    }
    user
  }

  //TODO: Refactor companyName a uuid
  User updateRolesForUserInCompanies(String username, Map rolesForCompanies){
    User user = User.findByUsername(username)
    def companies = rolesForCompanies.collect { companyName, rolesForThisCompany ->
      Company company = Company.findByUuid(companyName)
    }
    deleteRolesForUserInCompanies(user,companies)
    createRolesForUserInCompanies(user,rolesForCompanies)
    userRoleService.deleteUserRolesForUser(user)
  }

  List<Company> findAllCompaniesOfUser(User user){
    List<UserRoleCompany> userRolesCompanies = UserRoleCompany.findAllByUser(user)
    userRolesCompanies*.company
  }

}
