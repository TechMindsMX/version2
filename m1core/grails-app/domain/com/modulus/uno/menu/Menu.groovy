package com.modulus.uno.menu

class Menu {

  String name
  String internalUrl
  String parameters

  static hasMany = [menus: Menu]

  static constraints = {
    name()
    internalUrl()
    //parentMenu nullable: true
    parameters nullable: true
  }

  String toString(){
    "$name ($internalUrl)"
  }
}
