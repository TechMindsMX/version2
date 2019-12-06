package com.modulus.uno

import grails.transaction.Transactional

class TelephoneService {

  def save(Telephone telephone, User user){
    user.profile.addToTelephones(telephone)
    user.save()
    telephone
  }

  def saveForCompany(Telephone telephone, Company company) {
    log.info "Adding telephone: ${telephone} to company: ${company}"
    company.addToTelephones(telephone)
    company.save()
    log.info "Telephones for company: ${company.telephones}"
    telephone
  }

  def saveForContact(Telephone telephone, Long contactId) {
    ContactInformation contact = ContactInformation.get(contactId)
    log.info "Adding telephone: ${telephone} to contact: ${contact}"

    contact.addToTelephones(telephone)
    contact.save()
    log.info "Telephones for contact: ${contact.telephones}"
    telephone
  }

  @Transactional
  def updateTelephone(Telephone telephone) {
    log.info "Saving telephone: ${telephone.dump()}"
    telephone.save()
    telephone
  }
}
