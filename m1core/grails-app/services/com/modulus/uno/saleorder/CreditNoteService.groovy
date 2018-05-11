package com.modulus.uno.saleorder

import grails.transaction.Transactional
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
    //emailSenderService.notifyCreditNoteChangeStatus(creditNote)
    creditNote
  }

}
