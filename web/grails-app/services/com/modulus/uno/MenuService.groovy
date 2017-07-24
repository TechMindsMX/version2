package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class MenuService {

  Menu newMenu(String menuName){
    Menu menu = new Menu(name: menuName)
    menu.save()
    menu
  }

  Menu addOperationToMenu(Menu menu, MenuOperation menuOperation){
    menu.addToMenuOperations(menuOperation)
    menu.save()
    menu
  }

  Menu addSubmenuToMenu(Menu menu, String submenuName){
    Menu submenu = new Menu(name: submenuName)
    menu.addToMenus(submenu)
    menu.save()
    menu
  }
}
