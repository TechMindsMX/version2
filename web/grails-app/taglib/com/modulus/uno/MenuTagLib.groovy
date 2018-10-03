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
		  if ((!roles.authority.contains("ROLE_M1") && !roles.authority.contains("ROLE_CORPORATIVE") && session.company) || roles.authority.contains("ROLE_M1") || roles.authority.contains("ROLE_CORPORATIVE") ) {
        def rolesMenus = menuOperationsService.getMenusForTheseRoles(roles)
        def userMenus = rolesMenus + rolesMenus*.menus.flatten()
        if (!roles.authority.contains("ROLE_M1") && !roles.authority.contains("ROLE_CORPORATIVE")) {
          Company company = Company.get(session.company)
    	    userMenus = menuOperationsService.getMenusForUser(currentUser, company)
        }
    	  out << render(template:'/layouts/menus', model:[menus:rolesMenus, userMenus:userMenus])
		  }
    }
  }

}
