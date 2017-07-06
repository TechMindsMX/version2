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
    try {
      List domainClassReturn = bankAccountService.addBankAccountToRelationShip(bankAccount,session.company.toLong(), params.long('businessEntity') ?: 0, params)
      redirect(controller:domainClassReturn.get(0),action:"show",id:domainClassReturn.get(1))

    } catch (Exception e) {
      flash.message = e.message
      render view:'create', model:[bankAccount:bankAccount, banks:Bank.list().sort{it.name}, params:params, relation:params.relation]
    }
  }

  def edit(BankAccount bankAccount) {
    respond bankAccount, model:[banks:Bank.list().sort{ it.name }, params:params, relation:params.relation]
  }

  @Transactional
  def update(BankAccount bankAccount) {
    bankAccount.banco = Bank.findByBankingCode(params.bank)

    if (params.relation == "CLIENTE"){
      bankAccount.branchNumber = "*".padLeft(5,"0")
      bankAccount.accountNumber = params.accountNumberEnd.padLeft(11,"*")
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
    try{
      if (params.companyBankAccount){
        resultBankAccount = bankAccountService.updateBankAccountCompany(bankAccount, session.company)
        redirect(controller:"company",action:"show",id:session.company)
      } else {
        def businessEntity = BusinessEntity.get(params.businessEntity)
        resultBankAccount = bankAccountService.updateBankAccountBusinessEntity(bankAccount, businessEntity)
        redirect(controller:"businessEntity",action:"show",id:businessEntity.id)
      }
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
