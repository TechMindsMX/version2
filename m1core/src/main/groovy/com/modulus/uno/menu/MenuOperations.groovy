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

  MenuOperations removeMenu(Long menuId){
    if(this.id == null) throw new RuntimeException("You need instance domain before remove menu")
    Menu menu = Menu.get(menuId)
    MenuLink menuLink = MenuLink.findWhere(menu: menu, menuRef: this.id, type: GrailsNameUtils.getPropertyName(this.class))
    menuLink.delete(flush:true)
    this
  }

  static getClassesWithMenus(grailsApplication){
    def domainClassFamilies = []
    grailsApplication.domainClasses.each { artefact ->
      if( MenuOperations.class.isAssignableFrom(artefact.clazz)) {
        domainClassFamilies << artefact.clazz.name
      }
    }
    domainClassFamilies
  }

  static getInstancesWithMenus(){
    this.class
  }

  static getMenusForThisIntance(Long idInstance){
    MenuLink.findAllWhere(menuRef:idInstance,type:GrailsNameUtils.getPropertyName(this))
  }

}