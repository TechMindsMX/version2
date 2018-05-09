package com.modulus.uno.saleorder

import grails.transaction.Transactional

@Transactional(readOnly = true)
class CreditNoteController {

  CreditNoteService creditNoteService

  def create(SaleOrder saleOrder) {
    render view:"create", model:[saleOrder:saleOrder]
  }

  @Transactional
  def save(CreditNoteCommand creditNoteCommand) {
    log.info "Credit Note command: ${creditNoteCommand.dump()}"
    if (!creditNoteCommand) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    } 

    CreditNote creditNote = creditNoteCommand.createCreditNote()
    if (creditNote.hasErrors()) {
      transactionStatus.setRollbackOnly()
      flash.message = "No se pudo crear la Nota de Crédito"
      redirect action:'create', id:creditNoteCommand.saleOrderId
    }

    log.info "Credit Note to save: ${creditNote.dump()}"
    creditNoteService.saveCreditNote(creditNote)

    redirect action:"show", id:creditNote.id
  }

  def show(CreditNote creditNote) {
    respond creditNote, model:[saleOrder:creditNote.saleOrder, errors:params.errors]
  }

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'creditNote.label', default: 'CreditNote'), params.id])
        redirect controller:"saleOrder", action: "list"
      }
      '*'{ render status: NOT_FOUND }
    }
  }

}
