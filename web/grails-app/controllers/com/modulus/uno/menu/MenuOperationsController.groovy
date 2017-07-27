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

  def show(Role role){
    def menus = Menu.findAllWhere(parentMenu: null)
    def menusOfInstance = Role.getMenusForThisIntance(role.id)
    [role:role, menus:menus, menusOfInstance:menusOfInstance]
  }

  def save(Menu menu){
    Role role = Role.get(params.roleId)
    role.addMenu(menu.id)
    redirect action: 'show', id:role.id
  }

  def delete(Menu menu){
    Role role = Role.get(params.roleId)
    role.removeMenu(menu.id)
    redirect action: 'show', id:role.id
  }

}
