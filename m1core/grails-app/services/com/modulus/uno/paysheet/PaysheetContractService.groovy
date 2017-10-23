package com.modulus.uno.paysheet

import com.modulus.uno.BusinessEntityService
import com.modulus.uno.BusinessEntity

class PaysheetContractService {
  
  BusinessEntityService businessEntityService

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
}
