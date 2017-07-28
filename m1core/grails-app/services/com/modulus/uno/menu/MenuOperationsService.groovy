package com.modulus.uno.menu

import grails.transaction.Transactional
import com.modulus.uno.Role

@Transactional
class MenuOperationsService {

  def getMenusForTheseRoles(roles){
    roles.collect{ role ->
      Role.getMenusForThisIntance(role.id)
    }.flatten()*.menu
  }

}
