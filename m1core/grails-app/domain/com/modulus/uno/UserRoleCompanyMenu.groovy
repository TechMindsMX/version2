package com.modulus.uno

import com.modulus.uno.menu.Menu

class UserRoleCompanyMenu {

  User user
  Company company
  Role role

  static hasMany = [menus: Menu]

  static constraints = {
    user nullable:false
    company nullable:false
    role nullable:false
  }

}
