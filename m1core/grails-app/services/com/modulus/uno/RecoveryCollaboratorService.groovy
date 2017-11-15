package com.modulus.uno

class RecoveryCollaboratorService {

  def generateToken(String baseUrl, User user){
    def registration = new RegistrationCode(username:user.username, email:user.profile.email)
    registration.save()
    def message = new TokenCommand(email:user.profile.email, token:"${baseUrl}${registration.token}")
    message
  }

  def saveRegistrationCode(token){
    def registrationCode = RegistrationCode.findByTokenAndStatus(token, RegistrationCodeStatus.VALID)
    registrationCode.status = RegistrationCodeStatus.INVALID
    registrationCode.save()
  }

}
