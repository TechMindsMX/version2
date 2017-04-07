package com.modulus.uno

class TelephoneService {

  def save(Telephone telephone, User user){
    user.profile.addToTelephones(telephone)
    user.save()
    telephone
  }

  def saveForCompany(Telephone telephone, Company company) {
    company.addToTelephones(telephone)
    company.save()
    telephone
  }

}
