package com.modulus.uno

import grails.transaction.Transactional
import com.modulus.uno.twoFactorAuth.AuthenticatorService

class UserService {

  String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl="
  String APP_NAME = "ModulusUno"

  def documentService
  AuthenticatorService authenticatorService

  User findByUsername(String username) {
    log.info "Find user with username: ${username}"
    User.findByUsername(username)
  }

  @Transactional
  def addInformationToLegalRepresentative(def user, def params) {
    def profile = user.profile
    profile.name = params.profile.name
    profile.lastName = params.profile.lastName
    profile.motherLastName = params.profile.motherLastName
    profile.email = params.profile.email ?: ""
    if (params.profile.rfc)
      profile.rfc = params.profile.rfc
    if (params.profile.curp)
      profile.curp = params.profile.curp
    profile.trademark = params.profile.trademark
    profile.fullName = params.profile.fullName ?: ""
    profile.birthDate = params.profile.birthDate
    profile.save()
    user.profile = profile
    user.save()
    user
  }

  def containsUsersWithDocumentsByCompany(def listOfUsers,Company company) {
    def mapUserWithDocs = []
    listOfUsers.each{ user ->
      def listOfDocuments = documentService.getDocumentsByCompanyForLegalRepresentative(user.profile.documents,company.id)
      def documentsAvilable = false
      if (listOfDocuments.size() == 4)
        documentsAvilable = true
      mapUserWithDocs.add(documentsAvilable)
    }
    if (mapUserWithDocs.contains(false)) {
      return false
    } else {
      return true
    }
  }

  @Transactional
  def createRelationshipIntoLegalRepresentativeAndAsset(S3Asset asset, def userId) {
    def user = User.findById(userId)
    user.profile.addtoDocuments = asset
    user.save()
    user
  }

  @Transactional
  User createUserWithoutRole(User user,Profile profile){
    profile.save()
    user.profile = profile
    user.save()
    user
  }

  @Transactional
  User setAuthorityToUser(User user,String authority){
    Role role = Role.findByAuthority(authority)
    UserRole.create user, role
    user.save()
    user
  }

  def getUsersByRole(String srol){
    def users = User.list().findAll { user ->
      if (user.authorities.any { it.authority == srol })
        return user
    }
    users
  }

  @Transactional
  private def createTelephone(params) {
    def telephone = new Telephone()
    telephone.number = params.number
    telephone.extension = params.extension
    telephone.type = params.type
    telephone.save(failOnError:true)
    telephone
  }

  @Transactional
  private def updateTelephoneOfUser(params) {
    def telephone = Telephone.findById(params.telephone.id)
    telephone.number = params.telephone.number
    telephone.extension = params.telephone.extension
    telephone.type = params.telephone.type
    telephone.save()
  }

  User findUserFromUsernameAndEmail(String username, String email) {
    def criteria = User.createCriteria()
    criteria.get {
      eq("username", username)
      profile {
        eq("email", email)
      }
    }
  }

  @Transactional
  User generateKey2FA(User user) {
    user.key2FA = authenticatorService.generateKey()
    user.save()
    user
  }

  @Transactional
  User setEnableTwoFactor(User user) {
    user.enable2FA = !user.enable2FA
    user.save()
    user
  }
  
  String generateQRAuthenticatorUrl(User user) {
    QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, user.username, user.key2FA, APP_NAME), "UTF-8")
  }

}
