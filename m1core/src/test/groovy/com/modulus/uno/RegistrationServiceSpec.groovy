package com.modulus.uno

import spock.lang.Specification

class RegistrationServiceSpec extends Specification {

  def service = new RegistrationService()

  void "should find email by token"(){
  given:"A token"
    def token = 'token'
  and:"An Email"
    def email = 'josdem@email.com'
  and:"A registration code"
    def registrationCode = Mock(RegistrationCode)
    registrationCode.email >> email
    RegistrationCode.metaClass.static.findByToken = { registrationCode  }
  when:"We find registration code with that token"
    def result = service.findEmailByToken(token)
  then:"We expect email"
    'josdem@email.com' == result
  }

}
