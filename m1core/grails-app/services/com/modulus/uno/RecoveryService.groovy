package com.modulus.uno

import org.springframework.context.i18n.LocaleContextHolder as LCH
import grails.transaction.Transactional

class RecoveryService {

  def grailsApplication
  def registrationService
  def recoveryCollaboratorService
  def messageSource
  def emailSenderService
  CorporateService corporateService

  def sendConfirmationAccountToken(User user){
    String urlCorporate = corporateService.findUrlCorporateOfUser(user)
    def message = recoveryCollaboratorService.generateToken("${urlCorporate}${grailsApplication.config.recovery.register}", user)
    emailSenderService.sendEmailForConfirmAccount(message, user?.profile?.email)
  }

  @Transactional
  def confirmAccountForToken(token){
    def user = getUserByToken(token)
    if(!user) throw new UserNotFoundException(messageSource.getMessage('exception.user.not.found', null, LCH.getLocale()))
    if(user.enabled) throw new AccountEnabledException(messageSource.getMessage('exception.account.already.activated', null, LCH.getLocale()))
    user.enabled = true
    user.save()
    String name = "${user.profile.name} ${user.profile.lastName} ${user.profile.motherLastName}"
    def message = new NameCommand(name:name, type:EmailerMessageType.NEW_USER)
    emailSenderService.sendEmailForConfirmAccountForToken(user)
    user
  }

  def getUserByToken(String token){
    def registrationCode = RegistrationCode.findByToken(token)
    def user
    if (registrationCode) {
      def criteria = User.createCriteria()
      user = criteria.get {
        eq("username", registrationCode.username)
        profile {
          eq("email", registrationCode.email)
        }
      }
    }
    user
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
    String urlCorporate = corporateService.findUrlCorporateOfUser(user)
    def message = recoveryCollaboratorService.generateToken("${urlCorporate}${grailsApplication.config.recovery.forgot}", user)
    emailSenderService.sendEmailForRegistrationCode(message, email)
  }

  def changePasswordForToken(token, password){
    log.info "Changing password for token: ${token}"
    if(!registrationService.isValidToken(token)) throw new BusinessException(messageSource.getMessage('exception.not.valid.token', null, LCH.getLocale()))
    recoveryCollaboratorService.saveRegistrationCode(token)

    def user = getUserByToken(token)
    if(!user) throw new UserNotFoundException(messageSource.getMessage('exception.user.not.found', [], LCH.getLocale()))

    log.info "Updating password for user: ${user.username}"
    user.password = password
    user.save()
    user
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
