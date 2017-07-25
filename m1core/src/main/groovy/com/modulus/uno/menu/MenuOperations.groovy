package com.modulus.uno.menu

import grails.util.*

trait MenuOperations {

  MenuOperations addMenu(Long menuId){
    if(this.id == null) throw new RuntimeException("You need to save the domain instance before adding menu")
    Menu menu = Menu.get(menuId)
    MenuLink menuLink = new MenuLink(menu: menu, menuRef: this.id, type: GrailsNameUtils.getPropertyName(this.class))
    menuLink.save()
    this
  }

}