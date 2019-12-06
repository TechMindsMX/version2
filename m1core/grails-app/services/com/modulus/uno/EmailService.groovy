package com.modulus.uno

import grails.transaction.Transactional

class EmailService {

  @Transactional
  def save(Email email, Long contactId) {
    ContactInformation contact = ContactInformation.get(contactId)
    log.info "Adding email: ${email.dump()} to contact: ${contact}"

    contact.addToEmails(email)
    contact.save()
    log.info "Emails for contact: ${contact.emails}"
    contact
  }

}
