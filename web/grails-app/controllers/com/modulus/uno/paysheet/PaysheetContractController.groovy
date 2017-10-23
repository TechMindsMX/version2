package com.modulus.uno.paysheet

import grails.transaction.Transactional

import com.modulus.uno.BusinessEntityService
import com.modulus.uno.CompanyService
import com.modulus.uno.Company
import com.modulus.uno.User

@Transactional(readOnly = true)
class PaysheetContractController {

  BusinessEntityService businessEntityService
  CompanyService companyService
  PaysheetContractService paysheetContractService
  
  def list() {
    params.max = 25
    Company company = Company.get(session.company)
    List<PaysheetContract> paysheetContractList = PaysheetContract.findAllByCompany(company, params)
    Integer paysheetContractCount = PaysheetContract.countByCompany(company)
    [company:company, paysheetContractList:paysheetContractList, paysheetContractCount:paysheetContractCount]
  }

  def create() {
    Company company = Company.get(session.company)
    def clients = businessEntityService.findBusinessEntityByKeyword("", "CLIENT", company)
    List<User> users = companyService.getUsersWithRoleForCompany("ROLE_OPERATOR_PAYSHEET", company)
    respond new PaysheetContract(), model:[company:company, clients:clients, users:users]
  }

  @Transactional
  def save(PaysheetContractCommand command){
    log.info "Saving paysheet contract: ${command.dump()}"
    Company company = Company.get(session.company)
    if (!command) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    def clients = businessEntityService.findBusinessEntityByKeyword("", "CLIENT", company)
    List<User> users = companyService.getUsersWithRoleForCompany("ROLE_OPERATOR_PAYSHEET", company)

    if (command.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond command.errors, view:"create", model:[company:company, clients:clients, users:users]
      return
    }

    PaysheetContract paysheetContract = command.createPaysheetContract()
    paysheetContractService.savePaysheetContract(paysheetContract)

    log.info "Paysheet Contract saved: ${paysheetContract.dump()}"
    if (paysheetContract.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond paysheetContract.errors, view:"create", model:[company:company, clients:clients, users:users]
      return
    }

    redirect action:"show", id:paysheetContract.id 
  }

  def show(PaysheetContract paysheetContract){
    respond paysheetContract
  }
}
