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
    bankAccount.banco = Bank.findByBankingCode(command.bank)
    if(bankAccount.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond bankAccount.errors, view:'create', model:[banks:Bank.list().sort{ it.name }, params:params]
      return
    }

    Map result = bankAccountService.saveAndAsociateBankAccount(bankAccount, params)

    if(bankAccount.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond bankAccount.errors, view:'create', model:[banks:Bank.list().sort{ it.name }, params:params]
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
    bankAccount.banco = Bank.findByBankingCode(command.bank)
    log.info "Bank account to update: ${bankAccount.dump()}"

    if (params.relation == "CLIENTE"){
      bankAccount.branchNumber = "*".padLeft(5,"0")
      bankAccount.accountNumber = params.accountNumber.padLeft(11,"*")
    }

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

    def resultBankAccount = null
    def domain
    try{
      if (params.companyBankAccount){
        domain = Company.get(session.company)
        resultBankAccount = bankAccountService.updateBankAccountCompany(bankAccount, session.company)
      } else {
        domain = BusinessEntity.get(params.businessEntity)
        resultBankAccount = bankAccountService.updateBankAccountBusinessEntity(bankAccount, domain)
      }

      log.info "Bank account updated: ${bankAccount.dump()}"

      if(bankAccount.hasErrors()) {
        transactionStatus.setRollbackOnly()
        respond bankAccount.errors, view:'edit', model:[banks:Bank.list().sort{it.name}, params:params, relation:params.relation]
        return
      }

      redirect(controller:domain.class.simpleName, action:"show", id:domain.id)

    } catch (Exception e){
      transactionStatus.setRollbackOnly()
      flash.message = e.message
      render view:'edit', model:[bankAccount:bankAccount, banks:Bank.list().sort{it.name}, params:params,relation:params.relation]
    }

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
