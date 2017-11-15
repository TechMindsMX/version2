package com.modulus.uno

import spock.lang.Specification
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(RecoveryService)
@Mock([User, Profile, RegistrationCode])
class RecoveryServiceSpec extends Specification {

  def recoveryCollaboratorService = Mock(RecoveryCollaboratorService)
  def registrationService = Mock(RegistrationService)
  def messageSource = new MessageSourceMock()
  def grailsApplication = new GrailsApplicationMock()
  def emailSenderService = Mock(EmailSenderService)
  def corporateService = Mock(CorporateService)

  def setup() {
    service.recoveryCollaboratorService = recoveryCollaboratorService
    service.corporateService = corporateService
    service.registrationService = registrationService
    service.grailsApplication = grailsApplication
    service.messageSource = messageSource
    service.emailSenderService = emailSenderService
  }

  void "should send confirmation account token"() {
    given:"An email"
      def email = 'josdem@email.com'
      Profile profile = new Profile(email:email, name:"Some", lastName:"Body", motherLastName:"Any").save(validate:false)
      User user = new User(username:"theUser", password:"oldPass", profile:profile).save(validate:false)
    and:"A MessageCommand"
      def message = Mock(MessageCommand)
    and:
      recoveryCollaboratorService.generateToken(_, _) >> message
      corporateService.findUrlCorporateOfUser(_) >> "http://url.com"

    when:"We generate token"
      service.sendConfirmationAccountToken(user)
    then:"We expect generate token and send email"
      1 * emailSenderService.sendEmailForConfirmAccount(_, _)
  }

  void "should confirm account for token"(){
    given: "Token and password"
      def token = 'token'
      def email = 'josdem@email.com'
    and: "user"
      Profile profile = new Profile(email:email, name:"Some", lastName:"Body", motherLastName:"Any").save(validate:false)
      User user = new User(username:"theUser", password:"oldPass", profile:profile, enabled:false).save(validate:false)
    and:"the code"
      RegistrationCode code = new RegistrationCode(username:"theUser", email:email, token:token).save(validate:false)
    when: "We confirm account for token"
      service.confirmAccountForToken(token)
    then: "We expect user enabled"
      1 * emailSenderService.sendEmailForConfirmAccountForToken(user)
      user.enabled == true
  }

  void "should generate registration code for email"() {
    given: "An email"
      def email = 'josdem@email.com'
    and: "User mock"
      def user = Mock(User)
      def profile = new Profile(email:email, firsName:'firsName', motherLastName:'motherLastName', lastName:'lastName')
      def message = Mock(TokenCommand)
      user.profile >> profile
      Profile.metaClass.static.findByEmail = { profile }
      User.metaClass.static.findByProfile = { user }
    when: "We find user by email"
      recoveryCollaboratorService.generateToken('http://url.comforgot', user) >> message
      user.enabled >> true
      service.generateRegistrationCodeForEmail(email)
    then: "We expect send message to the email service"
      1 * corporateService.findUrlCorporateOfUser(_) >> "http://url.com"
      1 * emailSenderService.sendEmailForRegistrationCode(message, email)
  }


  void "should not generate registration code for email since user not found"() {
  given: "An email"
    def email = 'josdem@email.com'
  and: "User mock"
    def profile = new Profile(email:email, firsName:'firsName', motherLastName:'motherLastName', lastName:'lastName')
    def message = Mock(TokenCommand)
    Profile.metaClass.static.findByEmail = { profile }
    User.metaClass.static.findByProfile = { null }
  when: "We find user by email"
    service.generateRegistrationCodeForEmail(email)
  then: "We expect get an exception since user not found"
    thrown UserNotFoundException
  }

  void "should not generate registration code for email since account is not activated"() {
    given: "An email"
      def email = 'josdem@email.com'
    and: "User mock"
      def user = Mock(User)
      def profile = new Profile(email:email, firsName:'firsName', motherLastName:'motherLastName', lastName:'lastName')
      def message = Mock(TokenCommand)
      Profile.metaClass.static.findByEmail = { profile }
      User.metaClass.static.findByProfile = { user }
    when: "We find user by email"
      service.generateRegistrationCodeForEmail(email)
    then: "We expect get an exception since account is not activated"
      thrown AccountNoActivatedException
  }

  void "should change password for token"(){
    given: "Token and password"
      def token = 'token'
      def password = 'newPassword'
      def email = 'josdem@email.com'
    and: "user"
      Profile profile = new Profile(email:email, name:"Some", lastName:"Body", motherLastName:"Any").save(validate:false)
      User user = new User(username:"theUser", password:"oldPass", profile:profile).save(validate:false)
    and:"the code"
      RegistrationCode code = new RegistrationCode(username:"theUser", email:email, token:token).save(validate:false)
    and:
      registrationService.isValidToken(_) >> true
    when: "We send change password for token"
      service.changePasswordForToken(token, password)
    then: "We expect save new password"
      user.password == password
  }

  void "should not change password for token since user not exist"(){
    given: "Token and password"
      def token = 'token'
      def password = 'password'
      def email = 'josdem@email.com'
    and: "user"
      def profile = new Profile(email:email, firsName:'firsName', motherLastName:'motherLastName', lastName:'lastName')
      Profile.metaClass.static.findByEmail = { profile }
      User.metaClass.static.findByProfile = { null }
      registrationService.isValidToken(token) >> true
    when: "We send change password for token"
      service.changePasswordForToken(token, password)
    then: "We expect get an exception since user not found"
      thrown UserNotFoundException
  }

  void "should not change password for token since user not exist"(){
    given: "Token and password"
      def token = 'token'
      def password = 'password'
    when: "We send change password for token"
      service.changePasswordForToken(token, password)
    then: "We expect get an exception since is not a valid token"
      thrown BusinessException
  }
}
