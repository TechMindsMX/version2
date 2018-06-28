package com.modulus.uno.paysheet

import grails.transaction.Transactional

import com.modulus.uno.BusinessEntityService
import com.modulus.uno.CompanyService
import com.modulus.uno.Company
import com.modulus.uno.User
import com.modulus.uno.BusinessEntity
import com.modulus.uno.ListEntitiesCommand

@Transactional(readOnly = true)
class PaysheetContractController {

  BusinessEntityService businessEntityService
  CompanyService companyService
  PaysheetContractService paysheetContractService
  
  def list() {
    params.max = 25
    params.sort = "client.rfc"
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

  def edit(PaysheetContract paysheetContract) {
    Company company = Company.get(session.company)
    def clients = businessEntityService.findBusinessEntityByKeyword("", "CLIENT", company)
    clients.add(0, paysheetContract.client)
    List<User> users = companyService.getUsersWithRoleForCompany("ROLE_OPERATOR_PAYSHEET", company)
    respond paysheetContract, model:[company:company, clients:clients, users:users] 
  }
  
  @Transactional
  def update(PaysheetContractCommand command) {
    log.info "Updating paysheet contract: ${command.dump()}"
    Company company = Company.get(session.company)
    if (!command) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    PaysheetContract paysheetContract = command.updatePaysheetContract()
    log.info "PaysheetContrat to update: ${paysheetContract.dump()}"

    def clients = businessEntityService.findBusinessEntityByKeyword("", "CLIENT", company)
    clients.add(0, paysheetContract.client)
    List<User> users = companyService.getUsersWithRoleForCompany("ROLE_OPERATOR_PAYSHEET", company)

    if (command.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond command.errors, view:"edit", model:[company:company, clients:clients, users:users]
      return
    }

    paysheetContractService.savePaysheetContract(paysheetContract)

    log.info "Paysheet Contract updated: ${paysheetContract.dump()}"
    if (paysheetContract.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond paysheetContract.errors, view:"edit", model:[company:company, clients:clients, users:users]
      return
    }

    redirect action:"show", id:paysheetContract.id 
  }

  def addEmployees(PaysheetContract paysheetContract){
    List<BusinessEntity> availableEmployees = paysheetContractService.getEmployeesAvailableToAdd(paysheetContract)
    render view:"show", model:[paysheetContract:paysheetContract, availableEmployees:availableEmployees]
  }

  @Transactional
  def saveEmployees(PaysheetContract paysheetContract) {
    log.info "Saving employees selected into paysheet contract: ${paysheetContract.id}"
    log.info "Employees to save: ${params.entities}"

    if (!params.entities) {
      flash.message = "No seleccionó empleados"
      redirect action:"addEmployees", id:paysheetContract.id
      return
    }

    paysheetContractService.addEmployeesToPaysheetContract(paysheetContract, params)

    redirect action:"show", id:paysheetContract.id 
  }

  @Transactional
  def deleteEmployee(PaysheetContract paysheetContract) {
    log.info "Delete employee ${params.employeeId} from paysheet contract ${paysheetContract.id}"
    paysheetContractService.deleteEmployeeFromPaysheetContract(paysheetContract, params.employeeId.toLong()) 
    redirect action:"show", id:paysheetContract.id 
  }

  def chooseUsers(PaysheetContract paysheetContract){
    List<User> availableUsers = paysheetContractService.getUsersAvailableToAdd(paysheetContract)
    render view:"show", model:[paysheetContract:paysheetContract, availableUsers:availableUsers]
  }

  @Transactional
  def addUsers(ListEntitiesCommand listUsers) {
    log.info "Users to save: ${listUsers.checkBe}"
    PaysheetContract paysheetContract = PaysheetContract.get(params.paysheetContractId)

    if (!listUsers.checkBe) {
      flash.message = "No seleccionó Usuarios"
      redirect action:"chooseUsers", id:paysheetContract.id
      return
    }

    paysheetContractService.addUsersToPaysheetContract(paysheetContract, listUsers)

    redirect action:"show", id:paysheetContract.id 
  }

  @Transactional
  def deleteUser(PaysheetContract paysheetContract) {
    log.info "Delete employee ${params.userId} from paysheet contract ${paysheetContract.id}"
    paysheetContractService.deleteUserFromPaysheetContract(paysheetContract, params.userId.toLong()) 
    redirect action:"show", id:paysheetContract.id 
  }

}
