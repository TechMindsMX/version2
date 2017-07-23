package com.modulus.uno

class Menu {

  Role role
  String name

  static hasMany = [menuOperations: MenuOperation, menus: Menu]

  static constraints = {
    role unique: true
  }
}
