package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class MenuService {

  Menu addMenuForRole(MenuOperation menuOperation, String roleName) {
    Role role = Role.findByAuthority(roleName)
    Menu menu = Menu.findOrCreateByRole(role)
    menu.addToMenuOperations(menuOperation)
    menu.save()
    menu
  }
}
