package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

class BankAccountController {

  def bankAccountService

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def index(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    respond BankAccount.list(params), model:[bankAccountCount: BankAccount.count()]
  }

  def show(BankAccount bankAccount) {
    respond bankAccount
  }

  def create() {
    respond new BankAccount(params), model:[banks:Bank.list().sort{ it.name }, relation:params.relation]
  }

  @Transactional
  def save(BankAccountCommand command) {
    def bankAccount = command.createBankAccount()
    if(bankAccount.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond bankAccount.errors, view:'create', model:[banks:Bank.list().sort{ it.name }, params:params]
      return
    }

    Map result = bankAccountService.saveAndAsociateBankAccount(bankAccount, params)

    if(bankAccount.hasErrors() || result.error) {
      transactionStatus.setRollbackOnly()
      respond bankAccount.errors, view:'create', model:[banks:Bank.list().sort{ it.name }, params:params, error:result.error]
      return
    }

    redirect controller:result.controller, action:"show", id:result.id
  }

  def edit(BankAccount bankAccount) {
    respond bankAccount, model:[banks:Bank.list().sort{ it.name }, params:params, relation:params.relation]
  }

  @Transactional
  def update(BankAccountCommand command) {
    BankAccount bankAccount = BankAccount.get(params.id)
    bankAccount.properties = command.createBankAccount().properties
    log.info "Bank account to update: ${bankAccount.dump()}"

    if (bankAccount == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    if(bankAccount.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond bankAccount.errors, view:'edit', model:[banks:Bank.list().sort{it.name}, params:params, relation:params.relation]
      return
    }

    Map result = bankAccountService.saveAndAsociateBankAccount(bankAccount, params)

    if(bankAccount.hasErrors() || result.error) {
      transactionStatus.setRollbackOnly()
      respond bankAccount.errors, view:'edit', model:[banks:Bank.list().sort{ it.name }, params:params, error:result.error]
      return
    }

    redirect controller:result.controller, action:"show", id:result.id
  }

  @Transactional
  def delete(BankAccount bankAccount){

    if (bankAccount == null) {
      transactionStatus.setRollbackOnly()
        notFound()
        return
      }

      bankAccount.delete flush:true

      request.withFormat {
        form multipartForm {
          flash.message = message(code: 'default.deleted.message', args: [message(code: 'bankAccount.label', default: 'BankAccount'), bankAccount.id])
          redirect action:"index", method:"GET"
        }
        '*'{ render status: NO_CONTENT }
      }
  }

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'bankAccount.label', default: 'BankAccount'), params.id])
        redirect action: "index", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }

}
