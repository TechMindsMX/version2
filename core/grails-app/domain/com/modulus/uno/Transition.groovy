package com.modulus.uno

class Transition {

  String name
  State stateFrom
  State stateTo
  Company company

  static constraints = {
    name blank:false
  }

}
