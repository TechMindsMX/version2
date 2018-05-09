package com.modulus.uno.saleorder

import grails.transaction.Transactional

class CreditNoteService {
  
  @Transactional
  CreditNote saveCreditNote(CreditNote creditNote) {
    creditNote.save()
    creditNote
  }

}
