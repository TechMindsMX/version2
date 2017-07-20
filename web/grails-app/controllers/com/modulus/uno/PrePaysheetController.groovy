package com.modulus.uno

import grails.transaction.Transactional

@Transactional(readOnly = true)
class PrePaysheetController {

  PrePaysheetService prePaysheetService

  def create() {
    Company company = Company.get(session.company)
    respond new PrePaysheet(), model:[company:company]
  }

  def save(PrePaysheetCommand command) {
    log.info "Saving prePaysheet: ${command.dump()}"
    Company company = Company.get(session.company)
    if (!command) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    if (command.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond command.errors, view:"create", model:[company:company]
      return
    }

    PrePaysheet prePaysheet = command.createPrePaysheet()
    prePaysheetService.savePrePaysheet(prePaysheet)

    if (prePaysheet.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond prePaysheet.errors, view:"create", model:[company:company]
      return
    }

    redirect action:"show", id:prePaysheet.id
  }

  def show(PrePaysheet prePaysheet) {
    respond prePaysheet
  }

}
