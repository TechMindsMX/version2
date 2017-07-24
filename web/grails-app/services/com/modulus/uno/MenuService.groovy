package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class MenuService {

  Menu newMenu(String menuName, String internalUrl){
    Menu menu = new Menu(name: menuName, internalUrl: internalUrl)
    menu.save()
    menu
  }

  Menu addSubmenuToMenu(Menu menu, Menu submenu){
    menu.addToMenus(submenu)
    menu.save()
    menu
  }

  Menu addSubmenuToMenu(Menu menu, String submenuName, String internalUrl){
    Menu submenu = new Menu(name: submenuName, url: internalUrl)
    menu.addToMenus(submenu)
    menu.save()
    menu
  }
}
