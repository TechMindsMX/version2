package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll
import grails.test.mixin.Mock

@TestFor(CommissionsInvoiceService)
@Mock([CommissionsInvoice, Company, CommissionTransaction])
class CommissionsInvoiceServiceSpec extends Specification {

  void "Should save a commissions invoice"() {
    given:"A company"
      Company company = new Company(rfc:"AAA010101AAA").save(validate:false)
    and:"Commission transactions"
      CommissionTransaction commissionPago = new CommissionTransaction(type:CommissionType.PAGO, amount:new BigDecimal(100), status:CommissionTransactionStatus.PENDING, company:company).save(validate:false)
      CommissionTransaction commissionFija = new CommissionTransaction(type:CommissionType.FIJA, amount:new BigDecimal(1000), status:CommissionTransactionStatus.PENDING, company:company).save(validate:false)
    when:"We save the commission"
      def invoice = service.createCommissionsInvoiceForCompany(company)
    then:
      invoice.id
      invoice.status == CommissionsInvoiceStatus.CREATED
      invoice.commissions.size() == 2
  }

  void "Should get the commissions invoice for a company"() {
    given:"A company"
      Company company = new Company(rfc:"AAA010101AAA").save(validate:false)
    and:"Commissions invoice"
      CommissionsInvoice invoice1 = new CommissionsInvoice(receiver:company).save(validate:false)
      CommissionsInvoice invoice2 = new CommissionsInvoice(receiver:company).save(validate:false)
    when:"We get the commissions invoices"
      def invoices = service.getCommissionsInvoiceForCompany(company, [max:25, offset:0, sort:"dateCreated", order:"desc"])
    then:
      invoices.listInvoices.size() == 2
  }

}

