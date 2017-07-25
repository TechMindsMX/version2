package com.modulus.uno.menu

class MenuLink {

  Menu menu  
  Long menuRef
  String type

  static constraints = {
    type blank:false
    menuRef min:0L
  }

  static mapping = {
    cache 'read-write'
    menu cache:true, fetch:'join'
  }
}
