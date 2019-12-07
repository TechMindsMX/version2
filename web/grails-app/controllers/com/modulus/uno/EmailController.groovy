package com.modulus.uno

class EmailController {

  def emailService

  static allowedMethods = [save: "POST", update: "PUT"]

  def editForContact(Email email) {
    respond email
  }

  def updateForContact(Email email) {
    log.info "Updating email"

    if (email.hasErrors()) {
      respond email.errors, view:'editForContact'
      return
    }

    emailService.updateEmail(email)

    redirect(action:"show",controller:"company",id:"${session.company}")
  }

  def createForContact() {
    respond new Email(), model: [contact: ContactInformation.get(params.id), company: Company.get(session.company)]
  }

  def save(Email email) {
    if (email.hasErrors()) {
      respond email.errors, view:'createForContact', model:[contact: ContactInformation.get(params.contactId), company: Company.get(session.company)]
      return
    }

    emailService.save(email, new Long(params.contactId))

    redirect(action:"show", controller:"company", id:"${session.company}")
  }
}
