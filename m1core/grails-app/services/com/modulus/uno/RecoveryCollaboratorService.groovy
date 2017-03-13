package com.modulus.uno

class RecoveryCollaboratorService {

  def generateToken(String baseUrl, String email){
    def registration = new RegistrationCode(email:email)
    registration.save()
    def message = new TokenCommand(email:email, token:"${baseUrl}${registration.token}")
    message
  }

  def saveRegistrationCode(token){
    def registrationCode = RegistrationCode.findByTokenAndStatus(token, RegistrationCodeStatus.VALID)
    registrationCode.status = RegistrationCodeStatus.INVALID
    registrationCode.save()
  }

}
