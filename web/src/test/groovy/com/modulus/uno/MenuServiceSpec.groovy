package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(MenuService)
@Mock([MenuOperation, Role, Menu])
class MenuServiceSpec extends Specification {

  def setup() { }

  def cleanup() { }

  void "Create a menu with menu name and role"() {
    given: "A menu and role"
      MenuOperation menuOperation = new MenuOperation(name:"Menu", internalUrl:"/menu")
      menuOperation.save()
      Role role = new Role(authority: "ROLE_USER")
      role.save()
    when: "Add menu for a role"
      Menu menu = service.addMenuForRole(menuOperation, "ROLE_USER")
    then: "Check structure"
      menu.role.authority == "ROLE_USER"
      menu.menuOperations.size() == 1
      menu.id
  }

  void "Add menu operation to an existing menu for role"() {
    given: "An existing menu with a menu operation"
      MenuOperation menuOperation1 = new MenuOperation(name:"Menu", internalUrl:"/menu")
      menuOperation1.save()
      Role role = new Role(authority: "ROLE_USER")
      role.save()
      Menu menu1 = new Menu(role: role, menuOperations: [menuOperation1])
      menu1.save(validate:false)
    when:
      MenuOperation menuOperation2 = new MenuOperation(name:"Another Menu", internalUrl:"/menu/another")
      Menu menu2 = service.addMenuForRole(menuOperation2, "ROLE_USER")
    then:
      menu1.id == menu2.id
      menu2.role.authority == "ROLE_USER"
      menu2.menuOperations.size() == 2
  }
}
