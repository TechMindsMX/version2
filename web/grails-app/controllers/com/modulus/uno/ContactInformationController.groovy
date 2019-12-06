package com.modulus.uno

class ContactInformationController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def contactInformationService

  def index() { }

  def createForCompany() {
    respond new ContactInformation(), model:[company:Company.get(session.company)]
  }

  def saveForCompany(ContactInformation contactInformation) {
    if (contactInformation.hasErrors()) {
      respond contactInformation.errors, view:'createForCompany', model:[company:Company.get(session.company)]
      return
    }

    contactInformationService.saveCompanyContact(contactInformation, new Long(session.company))

    redirect(action:"show", controller:"company", id:"${session.company}")
  }
}
