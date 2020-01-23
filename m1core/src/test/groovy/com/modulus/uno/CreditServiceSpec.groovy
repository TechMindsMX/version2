package com.modulus.uno.credit

import com.modulus.uno.Company
import spock.lang.Specification
import java.lang.Void as Should

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
// import spock.lang.Unroll
// import spock.lang.Ignore

@TestFor(CreditService)
@Mock([Company, Credit])
class CreditServiceSpec extends Specification {

  Should "create a credit for a Company"() {
    given:"a company"
      def company = new Company(rfc:"JIGE930831NZ1",
        bussinessName:"Apple Computers",
        webSite:"http://www.apple.com",
        employeeNumbers:40,
        grossAnnualBilling:4000).save(validate:false)

    and:"the credit"
      def credit = new Credit(
        name: "ADELANTO DE NOMINA",
        productType: ProductType.ADVANCE,
        portfolioManagementType: PortfolioManagementType.OUTSTANDING_BALANCE,
        frequencyType: FrequencyType.BIWEEKLY,
        creditLineType: CreditLineType.ONE_LINE,
        dispersionType: DispersionType.INTERBANK_TRANSFER,
        enabled: true
      )

    when:
      credit = service.createCreditForCompany(credit, company)

    then:
      credit.id
      credit.name == "ADELANTO DE NOMINA"
      credit.productType == ProductType.ADVANCE
      credit.portfolioManagementType == PortfolioManagementType.OUTSTANDING_BALANCE
      credit.frequencyType == FrequencyType.BIWEEKLY
      credit.creditLineType == CreditLineType.ONE_LINE
      credit.dispersionType == DispersionType.INTERBANK_TRANSFER
      credit.enabled
      credit.company == company
  }

  Should "not create a credit for an Company"() {
    given:"a credit"
      def credit = new Credit(
        name: "ADELANTO DE NOMINA",
        productType: ProductType.ADVANCE,
        portfolioManagementType: PortfolioManagementType.OUTSTANDING_BALANCE,
        frequencyType: FrequencyType.BIWEEKLY,
        creditLineType: CreditLineType.ONE_LINE,
        dispersionType: DispersionType.INTERBANK_TRANSFER,
        enabled: true
      )

    when:
      def creditCreated = service.createCreditForCompany(credit, null)

    then:
      creditCreated.hasErrors()
  }

}
