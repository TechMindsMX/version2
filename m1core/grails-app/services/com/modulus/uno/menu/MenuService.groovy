package com.modulus.uno.menu

import grails.transaction.Transactional
import org.springframework.transaction.annotation.Propagation

@Transactional
class MenuService {

  Menu newMenu(String menuName, String internalUrl, String parameters){
    Menu menu = new Menu(name: menuName, internalUrl: internalUrl, parameters: parameters)
    menu.save()
    menu
  }

  Menu addSubmenuToMenu(Menu menu, Menu submenu){
    menu.menus << submenu
    //submenu.parentMenu = menu
    menu.save()
    //submenu.save()
    println menu.validate()
    println menu.menus
    menu
  }

  Menu addSubmenuToMenu(Long menuId, Long submenuId){
    addSubmenuToMenu(Menu.get(menuId), Menu.get(submenuId))
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  Menu addFewSubmenusToMenu(Long menuId, List<Long> submenuIds){
    submenuIds.each { submenuId ->
      addSubmenuToMenu(menuId, submenuId)
    }
    Menu.get(menuId)
  }

  Menu addSubmenuToMenu(Menu menu, String submenuName, String internalUrl, String parameters){
    Menu submenu = new Menu(name: submenuName, url: internalUrl)
    menu.addToMenus(submenu)
    menu.save()
    menu
  }
}
