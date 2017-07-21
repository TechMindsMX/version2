package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class MenuService {

  Menu addMenuForRole(MenuOperation menuOperation, String roleName) {
    Role role = Role.findByAuthority(roleName)
    Menu menu = Menu.findByRole(role)
    if(!menu){
      menu = new Menu(role: role)
      menu.save()
    }
    menu.addToMenuOperations(menuOperation)
    menu.save()
    menu
  }
}
