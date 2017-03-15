package com.modulus.uno

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

}
