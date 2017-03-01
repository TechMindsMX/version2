package com.modulus.uno

class UserRoleCompany {

  User user
  Company company

  static hasMany = [roles: Role]

  static constraints = {
    user nullable:false
    company nullable:false
  }

}
