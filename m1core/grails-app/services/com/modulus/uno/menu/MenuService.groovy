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
    if(!menu.menus) menu.menus = []
    menu.menus << submenu
    menu.save()
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

  Menu removeSubmenuToMenu(Menu menu, Menu submenu){
    menu.menus.remove(submenu)
    menu.save()
    menu
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  Menu removeSubmenuToMenu(Long menuId, Long submenuId){
    removeSubmenuToMenu(Menu.get(menuId), Menu.get(submenuId))
  }
}
