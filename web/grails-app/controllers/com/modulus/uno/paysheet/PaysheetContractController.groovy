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
  
  def create() {
    Company company = Company.get(session.company)
    def clients = businessEntityService.findBusinessEntityByKeyword("", "CLIENT", company)
    List<User> users = companyService.getUsersWithRoleForCompany("ROLE_OPERATOR_PAYSHEET", company)
    respond new PaysheetContract(), model:[company:company, clients:clients, users:users]
  }

}
