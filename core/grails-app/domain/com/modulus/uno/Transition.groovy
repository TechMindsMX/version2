package com.modulus.uno

class Transition implements Action {

  String name
  State stateFrom
  State stateTo

  static constraints = {
    name blank:false
  }

}
