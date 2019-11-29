package com.modulus.uno

class ContactInformation {

  String name
  String department
  String position

  static hasMany = [emails: Email, telephones: Telephone]

  static constraints = {
    name blank:false, size:1..100
    department blank:false, size:1..200
    position blank:false, size:1..200
  }
}
