package com.modulus.uno

class Menu {

  Role role

  static hasMany = [menuOperations: MenuOperation]

  static constraints = {
    role unique: true
  }
}
