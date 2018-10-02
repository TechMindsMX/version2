package com.modulus.uno.menu

import grails.transaction.Transactional
import com.modulus.uno.Role
import com.modulus.uno.User
import com.modulus.uno.Company
import com.modulus.uno.UserRoleCompanyMenu

@Transactional
class MenuOperationsService {

  def getMenusForTheseRoles(roles){
    roles.collect{ role ->
      Role.getMenusForThisIntance(role.id)
    }.flatten()*.menu.unique{ a, b -> a.id <=> b.id}
  }

	def removeMenuForAllRolesAssigned(Menu menu) {
    def menuLinks = MenuLink.findAllWhere(menu:menu)
		menuLinks.each { it.delete() }
	}

  def getMenusForUser(User user, Company company) {
    def allMenus = UserRoleCompanyMenu.findAllByUserAndCompany(user, company)?.menus.toList().flatten()
    allMenus?.unique { a, b -> a.id <=> b.id }
  }

}
