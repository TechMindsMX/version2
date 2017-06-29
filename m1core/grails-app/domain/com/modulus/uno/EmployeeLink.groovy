package com.modulus.uno

class EmployeeLink {

  String type
  //TODO: no debería ser el RFC, debería ser Long id de la clase que implementa
  String employeeRef
  String curp
  String number

  static belongsTo = [company:Company]

  static constraints = {
    number nullable:true
  }

}
