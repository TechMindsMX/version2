package com.modulus.uno.paysheet

import grails.transaction.Transactional
import com.modulus.uno.Company
import com.modulus.uno.Corporate
import com.modulus.uno.CompanyService
import com.modulus.uno.CorporateService
import com.modulus.uno.CompanyStatus

class PaysheetProjectService {

  CompanyService companyService
  CorporateService corporateService

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

  @Transactional
  BillerPaysheetProject saveBillerPaysheetProject(BillerPaysheetProject billerPaysheetProject) {
    billerPaysheetProject.save()
    log.info "Biller Paysheet project saved: ${billerPaysheetProject.dump()}"
    billerPaysheetProject  
  }

  @Transactional
  def deleteBiller(BillerPaysheetProject billerPaysheetProject) {
    billerPaysheetProject.delete()
  }

}
