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
    UserRoleCompany urc = UserRoleCompany.findByUser(user)
    log.info "User role company: ${urc?.dump()}"
    if (urc?.roles.find { it.authority == role.authority }) {
      log.info "Delete role: ${role.authority} for user: ${user.username}"
      urc.removeFromRoles(role)
      urc.save()
      log.info "Current roles for user: ${urc.roles*.dump()}"
    }
    user
  }

}
