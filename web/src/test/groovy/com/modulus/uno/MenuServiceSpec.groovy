package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(MenuService)
@Mock([MenuOperation])
class MenuServiceSpec extends Specification {

  def setup() { }

  def cleanup() { }

  void "Add menu operation to menu for role"() {
    given: "A menu and role"
      MenuOperation menuOperation = new MenuOperation(name:"Menu", internalUrl:"/menu")
      menuOperation.save()
      Role role = new Role(authority: "ROLE_USER")
      role.save()
    when:
      boolean added = service.addMenuForRole(menuOperation, "ROLE_USER")
    then:
      added
  }
}
