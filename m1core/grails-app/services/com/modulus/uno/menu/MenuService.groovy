package com.modulus.uno.menu

import grails.transaction.Transactional

@Transactional
class MenuService {

  Menu newMenu(String menuName, String internalUrl, String parameters){
    Menu menu = new Menu(name: menuName, internalUrl: internalUrl, parameters: parameters)
    menu.save()
    menu
  }

  Menu addSubmenuToMenu(Menu menu, Menu submenu){
    menu.addToMenus(submenu)
    menu.save()
    menu
  }

  Menu addSubmenuToMenu(Menu menu, String submenuName, String internalUrl, String parameters){
    Menu submenu = new Menu(name: submenuName, url: internalUrl)
    menu.addToMenus(submenu)
    menu.save()
    menu
  }
}
