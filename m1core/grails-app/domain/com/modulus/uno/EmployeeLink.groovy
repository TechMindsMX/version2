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
    curp blank:true, size:18..18, validator: { val, obj ->
        if (val.substring(0,10) == obj.employeeRef.substring(0,10)) {
          return true
        } else {
          return false
        }
      }
  }

}
