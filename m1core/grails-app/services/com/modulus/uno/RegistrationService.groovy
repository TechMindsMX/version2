package com.modulus.uno

class RegistrationService {

  def findEmailByToken(String token) {
    def registrationCode = RegistrationCode.findByToken(token)
    if(!registrationCode)throw new BusinessException("No pude encontrar el token: ${token}")
      registrationCode.email
  }

  def isValidToken(String token){
    def registrationCode = RegistrationCode.findByToken(token)
    registrationCode.isValid()
  }

}
