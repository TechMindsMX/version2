package com.modulus.uno.credit

import com.modulus.uno.Company
import com.modulus.uno.BusinessEntity
import grails.transaction.Transactional

@Transactional
class CreditService {


  def createCreditForCompany(Credit credit, Company company) {
    // if (!company) {
    //   return credit
    // }

    credit.company = company
    credit.save()
    credit
  }

  def updateCreditForCompany(CreditCommand creditCommand, Company company) {
    Credit credit = Credit.get(creditCommand.id)

    def params = creditCommand.createCredit().properties
    credit.properties = params
    credit.company = company
    credit.save()
    credit
  }

  def list(Company company, def params) {
    params.sort = params.sort ?: 'name'
    params.max = params.max ? Integer.max(params.max, 25) : 25

    def credits = Credit.findAllByCompany(company, params)
    def total = Credit.countByCompany(company)

    [credits: credits, total: total]
  }

  Map findBusinessAndCompanyByRfc(String rfc){
    BusinessEntity businessEntity =  BusinessEntity.findByRfc(rfc)
    Company company = Company.list().find { it.businessEntities.id.contains(businessEntity.id) }
    [company: company, business: businessEntity]
  }
}
