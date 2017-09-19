package com.modulus.uno.menu

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(MenuService)
@Mock([Menu])
class MenuServiceSpec extends Specification {

  def setup() { }

  def cleanup() { }

  void "Create a simple new menu"(){
    given:
      String menuName = "Tesorero/Contador"
      String url = "/url"
      String parameters = null
    when:
      Menu menu = service.newMenu(menuName, url, parameters)
    then:
      menu.id
      menu.name == "Tesorero/Contador"
  }

  void "Add operation to an existing menu"() {
    given: "An existing menu"
      Menu menu = new Menu(name: "Tesorero/Contador")
      Menu submenu = new Menu(name:"Menu", internalUrl:"/menu")
      menu.save(validate:false)
      submenu.save(validate:false)
    when: "Add menu for a role"
      Menu newMenu = service.addSubmenuToMenu(menu, submenu)
    then: "Check structure"
      newMenu.menus.size() == 1
      newMenu.id
  }

  void "Add submenu to an existing menu"() {
    given: "An existing menu"
      Menu menu = new Menu(name:"Administrador")
      menu.save()
    when: "add another menu"
      Menu mainMenu = service.addSubmenuToMenu(menu, "Administrar", "/url", null)
    then: "we got new menu"
      mainMenu.name == "Administrador"
      mainMenu.menus.size() == 1
      mainMenu.menus[0].name == "Administrar"
  }

  void "Remove operation to an existing menu"() {
    given: "An existing menu"
      Menu menu = new Menu(name: "Tesorero/Contador")
      Menu submenu = new Menu(name:"Menu", internalUrl:"/menu")
      menu.save(validate:false)
      submenu.save(validate:false)
    and: "Add menu for a role"
      Menu newMenu = service.addSubmenuToMenu(menu, submenu)
    when:
      Menu updatedMenu = service.removeSubmenuToMenu(menu, submenu)
    then: "Check structure"
      updatedMenu.menus.size() == 0
  }

}
