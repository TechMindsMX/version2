package com.modulus.uno.paysheet

import com.modulus.uno.BusinessEntityService
import com.modulus.uno.CorporateService
import com.modulus.uno.BusinessEntity
import com.modulus.uno.Company
import com.modulus.uno.Corporate
import com.modulus.uno.User
import com.modulus.uno.ListEntitiesCommand

class PaysheetContractService {
  
  BusinessEntityService businessEntityService
  CorporateService corporateService

  def savePaysheetContract(PaysheetContract paysheetContract){
    paysheetContract.save()
    paysheetContract
  }
 
  def getEmployeesAvailableToAdd(PaysheetContract paysheetContract){
    List<BusinessEntity> allEmployees = businessEntityService.getAllActiveEmployeesForCompany(paysheetContract.company)
    List<BusinessEntity> availableEmployees = allEmployees.collect { employee ->
      if (!paysheetContract.employees.contains(employee)) { employee }
    }.grep()
    availableEmployees
  }

  def addEmployeesToPaysheetContract(PaysheetContract paysheetContract, def params) {
    List<BusinessEntity> employees = businessEntityService.getBusinessEntitiesFromIds(params.entities)
    paysheetContract.employees.addAll(employees)
    paysheetContract.save()
    log.info "Employees in paysheetContract: ${paysheetContract.employees}"
    paysheetContract
  }

  def deleteEmployeeFromPaysheetContract(PaysheetContract paysheetContract, Long idEmployee){
    BusinessEntity employee = BusinessEntity.get(idEmployee)
    paysheetContract.removeFromEmployees(employee)
    paysheetContract.save()
    paysheetContract
  }

  List<PaysheetContract> getPaysheetContractsWithProjectsOfCompany(Company company){
    List<PaysheetContract> all = PaysheetContract.findAllByCompany(company)
    List<PaysheetContract> result = all.collect {
      if (it.projects) {
        return it
      }
    }.grep()
    result
  }

  def getUsersAvailableToAdd(PaysheetContract paysheetContract){
    Corporate corporate = corporateService.getCorporateFromCompany(paysheetContract.company.id)
    corporate.users.findAll { it.enabled }.toList() - paysheetContract.users.toList()
  }

  def addUsersToPaysheetContract(PaysheetContract paysheetContract, ListEntitiesCommand listUsers) {
    List<User> users = User.findAllByIdInList(listUsers.checkBe)
    paysheetContract.users.addAll(users)
    paysheetContract.save()
    paysheetContract
  }

}
