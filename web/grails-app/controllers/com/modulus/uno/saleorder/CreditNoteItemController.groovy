package com.modulus.uno.saleorder

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON

@Transactional(readOnly = true)
class CreditNoteItemController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  @Transactional
  def save(CreditNoteItemCommand command) {
    if (!command) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    CreditNoteItem creditNoteItem = command.createCreditNoteItem()

    if (creditNoteItem.hasErrors()) {
      transactionStatus.setRollbackOnly()
      redirect controller:"creditNote", action:"show", id:creditNoteItem.creditNote.id, params:[errors:creditNoteItem.errors]
      return
    }

    creditNoteItem.save()

    redirect controller:"creditNote", action:"show", id:creditNoteItem.creditNote.id
  }

}
