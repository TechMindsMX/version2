package com.modulus.uno

import org.springframework.transaction.annotation.Propagation
import grails.transaction.Transactional

@Transactional
class UserRoleService {

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  User deleteUserRolesForUser(User user){
    List<UserRole> userRoles = UserRole.findAllByUser(user)
    userRoles.each{ UserRole userRole ->
      userRole.delete()
    }
    user
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  User createUserRolesForUser(User user, def roles){
    roles.each{Role role ->
      new UserRole(user:user,role:role).save()
    }
    user
  }

  @Transactional
  User deleteRoleForUser(User user, Role role) {
    UserRole userRole = UserRole.findByUserAndRole(user, role)
    if (userRole) {
      userRole.delete()
    }
    user
  }

}
