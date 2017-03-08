package com.modulus.uno

import org.springframework.context.i18n.LocaleContextHolder as LCH

class RecoveryService {
  def grailsApplication
  def registrationService
  def recoveryCollaboratorService
  def messageSource
  def emailSenderService

  def sendConfirmationAccountToken(String email){
    def message = recoveryCollaboratorService.generateToken("${grailsApplication.config.recovery.register}", email)
    emailSenderService.sendEmailForConfirmAccount(message, email)
  }

  def confirmAccountForToken(token){
    def user = getUserByToken(token)
    if(!user) throw new UserNotFoundException(messageSource.getMessage('exception.user.not.found', null, LCH.getLocale()))
    if(user.enabled) throw new AccountEnabledException(messageSource.getMessage('exception.user.not.found', null, LCH.getLocale()))

    user.enabled = true
    user.save()

    String name = "${user.profile.name} ${user.profile.lastName} ${user.profile.motherLastName}"
    def message = new NameCommand(name:name, type:EmailerMessageType.NEW_USER)
    emailSenderService.sendEmailForConfirmAccountForToken(user)
    user
  }

  def getUserByToken(String token){
    def email = registrationService.findEmailByToken(token)
    def profile = Profile.findByEmail(email)
    User.findByProfile(profile)
  }

  def obtainRegistrationCodeForToken(String token) {
    def registrationCode = RegistrationCode.findByTokenAndStatus(token, RegistrationCodeStatus.VALID)
    if(!registrationCode) throw new BusinessException(messageSource.getMessage('exception.not.valid.token', null, LCH.getLocale()))
      registrationCode
  }

  def generateRegistrationCodeForEmail(String email) {
    def profile = Profile.findByEmail(email)
    def user = User.findByProfile(profile)
    if(!user) throw new UserNotFoundException(messageSource.getMessage('exception.user.not.found', null, LCH.getLocale()))
    if(!user.enabled) throw new AccountNoActivatedException(messageSource.getMessage('exception.account.not.activated', null, LCH.getLocale()))
    def message = recoveryCollaboratorService.generateToken("${grailsApplication.config.recovery.forgot}", email)
    emailSenderService.sendEmailForRegistrationCode(message, email)
  }

  def changePasswordForToken(token, password){
    if(!registrationService.isValidToken(token)) throw new BusinessException(messageSource.getMessage('exception.not.valid.token', null, LCH.getLocale()))
    recoveryCollaboratorService.saveRegistrationCode(token)

    def user = getUserByToken(token)
    if(!user) throw new UserNotFoundException(messageSource.getMessage('exception.user.not.found', [], LCH.getLocale()))

    user.password = password
    user.save()
  }

  def activateAccountByToken(String token) {
    def firstAccess = FirstAccessToLegalRepresentatives.findByToken(token)
    if (!firstAccess)
      return true
    if (!firstAccess.enabled)
      return true
    return false

  }

  def activeAccountAndChangePassword(String token,String password) {
    def firstAccessRow = FirstAccessToLegalRepresentatives.findByToken(token)
    def user = firstAccessRow.user
    firstAccessRow.enabled = false
    firstAccessRow.save()
    user.password = password
    user.accountLocked = false
    user.enabled = true
    user.save()
  }

}
