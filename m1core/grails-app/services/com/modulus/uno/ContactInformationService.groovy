package com.modulus.uno

import grails.transaction.Transactional

class ContactInformationService {

  @Transactional
  def saveCompanyContact(ContactInformation contactInformation, Long companyId) {
    Company company = Company.get(companyId)
    log.info "Adding contact information: ${contactInformation.name} to company: ${company}"

    company.addToContacts(contactInformation)

    company.save()
    log.info "Contacts for company: ${company.contacts}"
    company
  }

}
