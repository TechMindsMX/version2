package com.modulus.uno.saleorder

import grails.transaction.Transactional
import com.modulus.uno.Authorization
import com.modulus.uno.EmailSenderService
import com.modulus.uno.status.CreditNoteStatus

class CreditNoteService {
  
  def springSecurityService
  EmailSenderService emailSenderService

  @Transactional
  CreditNote saveCreditNote(CreditNote creditNote) {
    creditNote.save()
    creditNote
  }

  @Transactional
  CreditNote sendToAuthorize(CreditNote creditNote) {
    creditNote.status = CreditNoteStatus.TO_AUTHORIZE
    creditNote.save()
    emailSenderService.notifyCreditNoteChangeStatus(creditNote)
    creditNote
  }

  @Transactional
  CreditNote processAuthorization(CreditNote creditNote) {
    addAuthorizationToCreditNote(creditNote)
    if (isFullAuthorized(creditNote)) {
      authorizeCreditNote(creditNote)
    }
    creditNote
  }

  @Transactional
  def addAuthorizationToCreditNote(CreditNote creditNote) {
    Authorization authorization = new Authorization(user:springSecurityService.currentUser).save()
    creditNote.addToAuthorizations(authorization)
    creditNote.save()
  }

  Boolean isFullAuthorized (CreditNote creditNote) {
    def alreadyAuthorizations = creditNote.authorizations ? creditNote.authorizations.size() : 0
    alreadyAuthorizations >= creditNote.saleOrder.company.numberOfAuthorizations
  }

  @Transactional
  CreditNote authorizeCreditNote(CreditNote creditNote) {
    creditNote.status = CreditNoteStatus.AUTHORIZED
    creditNote.save()
    emailSenderService.notifyCreditNoteChangeStatus(creditNote)
    creditNote
  }

}
