package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class RoleService {

  UserRoleService userRoleService
  def springSecurityService

  UserRoleCompany createRolesForUserAtThisCompany(List<Role> roles, User user, Company company) {
    UserRoleCompany userRoleByCompany = new UserRoleCompany(user:user,company:company)
    roles.collect{ Role role ->
      userRoleByCompany.addToRoles(role)
    }
    userRoleByCompany.save()
    userRoleByCompany
  }

  UserRoleCompany findRolesForUserAtThisCompany(User user, Company company){
    UserRoleCompany.findByCompanyAndUser(company, user)
  }

  void deleteRolesForUserAtThisCompany(User user, Company company){
    UserRoleCompany userRoleCompany = findRolesForUserAtThisCompany(user, company)
    if (userRoleCompany) {
      userRoleCompany.delete()
    }
  }

  def updateTheUserRolesOfUserAtThisCompany(User user, Company company){
    UserRoleCompany userRoleCompany = findRolesForUserAtThisCompany(user,company)
    userRoleService.deleteUserRolesForUser(user)
    userRoleService.createUserRolesForUser(user, userRoleCompany.roles)
    springSecurityService.reauthenticate(user.username)
  }


}