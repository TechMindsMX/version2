package com.modulus.uno

import com.modulus.uno.menu.MenuOperationsService

class MenuTagLib {
  static namespace = "menus"
  static defaultEncodeAs = [taglib:'text']

  MenuOperationsService menuOperationsService
  def springSecurityService

  def getMenus = { attrs ->
    def currentUser = springSecurityService.getCurrentUser()
		if (currentUser) {
    	def roles = currentUser.getAuthorities()
    	def menus = menuOperationsService.getMenusForTheseRoles(roles)
    	out << render(template:'/layouts/menus', model:[menus:menus])
		}
  }

}
