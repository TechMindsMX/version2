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

  static cheers(String name){
    "Hello $name !!!"
  }

  static getClassesWitMenues(grailsApplication){
    def domainClassFamilies = [:]
    grailsApplication.domainClasses.each { artefact ->
      if( MenuOperations.class.isAssignableFrom(artefact.clazz)) {
        domainClassFamilies[artefact.clazz.name] = [GrailsNameUtils.getPropertyName(artefact.clazz)]
        domainClassFamilies[artefact.clazz.name].addAll(artefact.subClasses.collect { GrailsNameUtils.getPropertyName(it.clazz) })
      }
    }
    domainClassFamilies
  }
}