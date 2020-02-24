package com.modulus.uno

class ContactInformationController {

  static allowedMethods = [saveForCompany: "POST", update: "PUT"]

  def contactInformationService

  def edit(ContactInformation contactInformation) {
    respond contactInformation, model:[company:Company.get(session.company)]
  }

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

  def update(ContactInformation contactInformation) {
    if (contactInformation.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond contactInformation.errors, view:'edit'
      return
    }

    contactInformationService.updateContactInformation(contactInformation)

    redirect(action:"show",controller:"company",id:"${session.company}")
  }
}
