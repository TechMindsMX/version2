package com.modulus.uno
import grails.validation.Validateable

class CompanyCommand implements Validateable {

  String artemisaId
  String rfc
  String bussinessName
  String webSite
  String employeeNumbers
  String grossAnnualBilling
  String numberOfAuthorizations
  String taxRegime
  String corporateId
  Integer firstPaymentDay 
  Integer secondPaymentDay 

  Company createCompany() {
    new Company(
      artemisaId: this.artemisaId,
      rfc: this.rfc,
      bussinessName: this.bussinessName,
      webSite: this.webSite,
      employeeNumbers: this.employeeNumbers,
      grossAnnualBilling: this.grossAnnualBilling,
      numberOfAuthorizations: this.numberOfAuthorizations,
      taxRegime: CompanyTaxRegime."${this.taxRegime}",
      firstPaymentDay: this.firstPaymentDay,
      secondPaymentDay: this.secondPaymentDay
    )
  }
}
