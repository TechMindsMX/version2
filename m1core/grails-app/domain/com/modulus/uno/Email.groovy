package com.modulus.uno

class Email {
  String address
  EmailType type

  static belongsTo = [ContactInformation]

  static constraints = {
    address blank:false, email:true, size:6..200
  }

}
