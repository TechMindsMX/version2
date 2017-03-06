package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class LegalRepresentativeService {

  def springSecurityService

  def isCurrentUserLegalRepresentativeOfCompany(Company company){
    def currentUser = springSecurityService.getCurrentUser()
    company.legalRepresentatives.any{ representante -> representante.id == currentUser.id }
  }

  def getAvailableLegalRepresentativesForCompany(Company company){
    def notAvailableRepresentatives = []
    //TODO: Quitar .get y sólo dejar servicio cuando la seguridad este correctamente implementada
    def currentUser = springSecurityService.getCurrentUser()
    def legalRepresentatives = findUsersWithLegalRepresentativeRole()
    notAvailableRepresentatives << currentUser
    notAvailableRepresentatives += company.legalRepresentatives
    legalRepresentatives -= notAvailableRepresentatives.unique()
    legalRepresentatives
  }

  def findUsersWithLegalRepresentativeRole(){
    def role = Role.findByAuthority('ROLE_LEGAL_REPRESENTATIVE')
    def users = UserRole.findAllByRole(role)*.user
    users
  }

  def assignLegalRepresentativeToCompany(Long companyId,Long userId){
    def currentUser = springSecurityService.getCurrentUser()
    def company = Company.get(companyId)
    def user = User.get(userId)

    if(currentUser.id == user.id)
      addLegalRepresentativeRoleToUser(user)

    company.addToLegalRepresentatives(user)
    company.save()
    user
  }

  def addLegalRepresentativeRoleToUser(User user){
    def role = Role.findByAuthority("ROLE_LEGAL_REPRESENTATIVE")

    if(!UserRole.get(user.id,role.id)){
      UserRole.create(user,role, true)
    }
  }

  def findUserByRFC(String rfc,Company company){
    def notAvailableRepresentatives = []
    notAvailableRepresentatives << springSecurityService.getCurrentUser()
    notAvailableRepresentatives += company.legalRepresentatives

    def role = Role.findByAuthority('ROLE_LEGAL_REPRESENTATIVE')
    def users = User.withCriteria{
      profile{
        eq('rfc',rfc)
      }
    }

    users -= notAvailableRepresentatives
    users.find{ it.authorities.find{ it.id == role.id} }
  }

}
