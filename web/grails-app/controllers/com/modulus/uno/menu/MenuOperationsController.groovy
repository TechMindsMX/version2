package com.modulus.uno.menu

import com.modulus.uno.Role
import grails.core.GrailsApplication

class MenuOperationsController {

  def index() {
    log.debug "${Role.list()}"
    log.debug "${Role.getClassesWithMenus(grailsApplication)}"
    log.debug "${Role.getInstancesWithMenus()}"
    def roles = Role.list()
    def menus = Menu.findAllWhere(parentMenu: null)
    [roles:roles, menus:menus]
  }
}
