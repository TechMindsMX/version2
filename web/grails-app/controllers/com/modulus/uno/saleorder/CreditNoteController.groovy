package com.modulus.uno.saleorder

import grails.transaction.Transactional
import com.modulus.uno.CompanyService
import com.modulus.uno.status.CreditNoteStatus

@Transactional(readOnly = true)
class CreditNoteController {

  CreditNoteService creditNoteService
  CompanyService companyService

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
    respond creditNote, model:[saleOrder:creditNote.saleOrder, isEnabledToStamp:companyService.isCompanyEnabledToStamp(creditNote.saleOrder.company), errors:params.errors]
  }

  @Transactional
  def requestAuthorization(CreditNote creditNote) {
    creditNoteService.sendToAuthorize(creditNote)
    redirect action:"show", id:creditNote.id
  }

  @Transactional
  def authorize(CreditNote creditNote) {
    creditNoteService.processAuthorization(creditNote)
    redirect action:"show", id:creditNote.id
  }

  @Transactional
  def apply(CreditNote creditNote) {
    creditNoteService.processApplyCreditNote(creditNote)
    redirect action:"generatePdf", id:creditNote.id
  }

  @Transactional
  def generatePdf(CreditNote creditNote) {
    creditNoteService.generatePdf(creditNote)
    redirect action:'show', id:creditNote.id
  }


  @Transactional
  def deleteCreditNote(CreditNote creditNote) {
    SaleOrder saleOrder = creditNote.saleOrder
    creditNoteService.deleteCreditNote(creditNote)
    redirect controller:"saleOrder", action:"show", id:saleOrder.id
  }

  @Transactional
  def cancelCreditNote(CreditNote creditNote){
    creditNoteService.cancelCreditNote(creditNote)
    redirect controller:"saleOrder", action:"show", id:creditNote.saleOrder.id
  }

  @Transactional
  def rejectCreditNote(CreditNote creditNote){
    creditNoteService.rejectCreditNote(creditNote)
    redirect controller:"saleOrder", action:"show", id:creditNote.saleOrder.id
  }

  @Transactional
  def requestCancelCreditNote(CreditNote creditNote) {
    creditNoteService.sendToAuthorizeCancelCreditNote(creditNote)
    redirect action:"show", id:creditNote.id
  }

  @Transactional
  def authorizeCancelCreditNote(CreditNote creditNote) {
    creditNoteService.authorizeCancelCreditNote(creditNote)
    redirect action:"show", id:creditNote.id
  }

  @Transactional
  def applyCancelCreditNote(CreditNote creditNote) {
    creditNoteService.applyCancelCreditNote(creditNote)
    redirect action:"show", id:creditNote.id
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
