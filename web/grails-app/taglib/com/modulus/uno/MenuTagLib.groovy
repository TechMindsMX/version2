package com.modulus.uno

import com.modulus.uno.menu.MenuOperationsService

class MenuTagLib {
  static namespace = "menus"
  static defaultEncodeAs = [taglib:'text']

  MenuOperationsService menuOperationsService
  def springSecurityService

  def getMenus = { attrs ->
    def currentUser = springSecurityService.getCurrentUser()
    def roles = currentUser.getAuthorities()
    menuOperationsService.getMenusForTheseRoles(roles)

    out << "<h1>HOLAAAAA</h1>"
  }

}
