package com.modulus.uno

class Menu {

  String name
  String internalUrl

  static hasMany = [menus: Menu]
  static belongsTo = [parentMenu: Menu]

  static constraints = {
    name()
    internalUrl()
    parentMenu nullable: true
  }

  String toString(){
    "$name ($internalUrl)"
  }
}
