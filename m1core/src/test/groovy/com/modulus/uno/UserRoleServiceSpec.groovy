package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import java.lang.Void as Should
import grails.test.mixin.Mock

@TestFor(UserRoleService)
@Mock([User, Role, UserRole])
class UserRoleServiceSpec extends Specification {

  Should "Delete the userRoles of user in session"(){
    given:
      Role role1 = new Role(authorization:"ROLE_LEGAL_REPRESENTATIVE_EJECUTOR").save(validate:false)
      Role role2 = new Role(authorization:"ROLE_FICO_VISOR").save(validate:false)
      User user = new User(username:"user", password:"user")
      user.save(validate:false)
      UserRole userRole1 = new UserRole(user:user, role:role1)
      UserRole userRole2 = new UserRole(user:user, role:role2)
    when:
      service.deleteUserRolesForUser(user)
    then:
      UserRole.findAllByUser(user).size() == 0
  }

  Should "Create the current userRoles of user in session"(){
    given:
      Role role1 = new Role(authorization:"ROLE_LEGAL_REPRESENTATIVE_EJECUTOR").save(validate:false)
      Role role2 = new Role(authorization:"ROLE_FICO_VISOR").save(validate:false)
      User user = new User(username:"user",password:"user")
      user.save(validate:false)
    when:
      service.createUserRolesForUser(user, [role1, role2])
    then:
      UserRole.findAllByUser(user).size() == 2
  }

  Should "Delete a role for the user"() {
    given:"The user"
      User user = new User(username:"theUser").save(validate:false)
    and:"The roles"
      Role roleToDelete = new Role(authority:"role1").save(validate:false)
      def roles = [roleToDelete, new Role(authority:"role2").save(validate:false)]
    and:"The user-roles"
      roles.each { role ->
        UserRole userRole = new UserRole(user:user, role:role).save(validate:false)
      }
    when:
      def userWithoutRole = service.deleteRoleForUser(user, roleToDelete)
    then:
      user.getAuthorities().size() == 1
  }

}
