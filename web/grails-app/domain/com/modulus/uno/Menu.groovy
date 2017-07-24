package com.modulus.uno

class Menu {

  String name

  static hasMany = [menuOperations: MenuOperation, menus: Menu]

  static constraints = {
    name()
  }
}
