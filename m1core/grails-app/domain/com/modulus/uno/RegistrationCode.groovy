package com.modulus.uno

class RegistrationCode {
  String email
  Date dateCreated
  String token = UUID.randomUUID().toString().replaceAll('-','')
  RegistrationCodeStatus status = RegistrationCodeStatus.VALID

  static constraints = {
    email blank:false,email:true,size:6..200
  }

  def isValid(){
    status == RegistrationCodeStatus.VALID ? true : false
  }
}
