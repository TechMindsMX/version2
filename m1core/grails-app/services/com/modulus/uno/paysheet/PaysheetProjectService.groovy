package com.modulus.uno.paysheet

import grails.transaction.Transactional
import com.modulus.uno.Company
import com.modulus.uno.Corporate
import com.modulus.uno.CompanyService
import com.modulus.uno.CorporateService
import com.modulus.uno.CompanyStatus
import com.modulus.uno.BusinessEntity
import com.modulus.uno.BusinessEntityService

class PaysheetProjectService {

  CompanyService companyService
  CorporateService corporateService
  BusinessEntityService businessEntityService

  @Transactional
  PaysheetProject savePaysheetProject(PaysheetProject paysheetProject) {
    paysheetProject.save()
    paysheetProject
  }

  @Transactional
  void deletePaysheetProject(PaysheetProject paysheetProject) {
    paysheetProject.delete()
  }

  PaysheetProject getPaysheetProjectByPaysheetContractAndName(PaysheetContract paysheetContract, String name) {
    PaysheetProject.findByPaysheetContractAndName(paysheetContract, name)
  }

  List<Company> getCompaniesInCorporate(Long idCompany) {
    Corporate corporate = corporateService.getCorporateFromCompany(idCompany)
    companyService.findCompaniesByCorporateAndStatus(CompanyStatus.ACCEPTED, corporate.id)
  }

  @Transactional
  PayerPaysheetProject savePayerPaysheetProject(PayerPaysheetProject payerPaysheetProject) {
    payerPaysheetProject.save()
    log.info "Payer Paysheet project saved: ${payerPaysheetProject.dump()}"
    payerPaysheetProject  
  }

  @Transactional
  def deletePayer(PayerPaysheetProject payerPaysheetProject) {
    payerPaysheetProject.delete()
  }

  List<BusinessEntity> getAvailableEmployeesToAdd(PaysheetProject paysheetProject) {
    (paysheetProject.paysheetContract.employees - paysheetProject.employees).toList().sort { it.toString() }
  }

  @Transactional
  def addEmployeesToPaysheetProject(PaysheetProject paysheetProject, def params) {
    log.info "Adding selected employees: ${params.entities}"
    List<BusinessEntity> employees = businessEntityService.getBusinessEntitiesFromIds(params.entities)
    paysheetProject.employees.addAll(employees)
    paysheetProject.save()
    log.info "Employees in paysheetProject: ${paysheetProject.employees}"
    paysheetProject
  }
  
  @Transactional
  def deleteEmployeeFromPaysheetProject(PaysheetProject paysheetProject, Long idEmployee){
    BusinessEntity employee = BusinessEntity.get(idEmployee)
    paysheetProject.removeFromEmployees(employee)
    paysheetProject.save()
    paysheetProject
  }

}
