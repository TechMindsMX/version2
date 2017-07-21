package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(MenuService)
@Mock([MenuOperation, Role, Menu])
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
      Menu menu = service.addMenuForRole(menuOperation, "ROLE_USER")
    then:
      menu.role.authority == "ROLE_USER"
      menu.menuOperations.size() == 1
  }
}
