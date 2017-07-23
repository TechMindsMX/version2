package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(MenuService)
@Mock([MenuOperation, Role, Menu])
class MenuServiceSpec extends Specification {

  def setup() { }

  def cleanup() { }

  void "Create a simple new menu"(){
    given:
      String menuName = "Tesorero/Contador"
      String roleName = "ROLE_FICO_VISOR"
    and:
      new Role(authority: "ROLE_FICO_VISOR").save()
    when:
      Menu menu = service.newMenu(menuName, roleName)
    then:
      menu.id
      menu.role.authority == "ROLE_FICO_VISOR"
      menu.name == "Tesorero/Contador"
  }

  void "Add operation to an existing menu"() {
    given: "An existing menu"
      Role role = new Role(authority: "ROLE_FICO_VISOR")
      role.save()
      Menu menu = new Menu(name: "Tesorero/Contador", role: role)
      MenuOperation menuOperation = new MenuOperation(name:"Menu", internalUrl:"/menu")
      menuOperation.save()
    when: "Add menu for a role"
      Menu newMenu = service.addOperationToMenu(menu, menuOperation)
    then: "Check structure"
      newMenu.menuOperations.size() == 1
      newMenu.id
  }

  void "Add submenu to an existing menu"() {
    given: "An existing menu"
      Menu menu = new Menu(name:"Administrador")
      menu.save(validate:false)
    when: "add another menu"
      Menu mainMenu = service.addSubmenuToMenu(menu, "Administrar")
    then: "we got new menu"
      mainMenu.name == "Administrador"
      mainMenu.menus.size() == 1
      mainMenu.menus[0].name == "Administrar"
      mainMenu.role == mainMenu.menus[0].role
  }
}
