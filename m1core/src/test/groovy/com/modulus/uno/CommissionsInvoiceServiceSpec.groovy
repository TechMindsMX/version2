package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll
import grails.test.mixin.Mock

@TestFor(CommissionsInvoiceService)
@Mock([CommissionsInvoice, Company, CommissionTransaction, CommissionsInvoicePayment])
class CommissionsInvoiceServiceSpec extends Specification {

  InvoiceService invoiceService = Mock(InvoiceService)

  def setup() {
    service.invoiceService = invoiceService
  }

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

  void "Should stamp a commissions invoice"() {
    given:"A commissions invoice"
      Company company = new Company(rfc:"AAA010101AAA").save(validate:false)
      CommissionsInvoice invoice = new CommissionsInvoice(receiver:company).save(validate:false)
    and:
      invoiceService.stampCommissionsInvoice(invoice) >> "folioSat"
    when:"We stamp the invoice"
      def result = service.stampInvoice(invoice)
    then:"We expect"
      result.status == CommissionsInvoiceStatus.STAMPED
      result.folioSat == "folioSat"
  }

  void "Should get commissions summary from invoice"() {
    given:"A company"
      Company company = new Company(rfc:"AAA010101AAA").save(validate:false)
    and:"Commission transactions"
      CommissionTransaction commissionPago = new CommissionTransaction(type:CommissionType.PAGO, amount:new BigDecimal(100), status:CommissionTransactionStatus.INVOICED, company:company).save(validate:false)
      CommissionTransaction commissionFija = new CommissionTransaction(type:CommissionType.FIJA, amount:new BigDecimal(1000), status:CommissionTransactionStatus.INVOICED, company:company).save(validate:false)
    and:"The commissions invoice"
      CommissionsInvoice invoice = new CommissionsInvoice(receiver:company).save(validate:false)
      invoice.addToCommissions(commissionPago)
      invoice.addToCommissions(commissionFija)
      invoice.save(validate:false)
    when:
      def result = service.getCommissionsSummaryFromInvoice(invoice)
    then:
      result.size() == 2
  }

  void "Should delete a commissions invoice"() {
    given:"A company"
      Company company = new Company(rfc:"AAA010101AAA").save(validate:false)
    and:"Commission transactions"
      CommissionTransaction commissionPago = new CommissionTransaction(type:CommissionType.PAGO, amount:new BigDecimal(100), status:CommissionTransactionStatus.INVOICED, company:company).save(validate:false)
      CommissionTransaction commissionFija = new CommissionTransaction(type:CommissionType.FIJA, amount:new BigDecimal(1000), status:CommissionTransactionStatus.INVOICED, company:company).save(validate:false)
    and:"The commissions invoice"
      CommissionsInvoice invoice = new CommissionsInvoice(receiver:company).save(validate:false)
      invoice.addToCommissions(commissionPago)
      invoice.addToCommissions(commissionFija)
      invoice.save(validate:false)
    when:
      service.deleteCommissionsInvoice(invoice)
    then:
      commissionPago.status == CommissionTransactionStatus.PENDING
      commissionFija.status == CommissionTransactionStatus.PENDING
  }

  void "Should cancel a stamped commissions invoice"() {
    given:"A commissions invoice"
      Company company = new Company(rfc:"AAA010101AAA").save(validate:false)
    and:"Commission transactions"
      CommissionTransaction commissionPago = new CommissionTransaction(type:CommissionType.PAGO, amount:new BigDecimal(100), status:CommissionTransactionStatus.INVOICED, company:company).save(validate:false)
      CommissionTransaction commissionFija = new CommissionTransaction(type:CommissionType.FIJA, amount:new BigDecimal(1000), status:CommissionTransactionStatus.INVOICED, company:company).save(validate:false)
    and:"The commissions invoice"
      CommissionsInvoice invoice = new CommissionsInvoice(receiver:company, status:CommissionsInvoiceStatus.STAMPED).save(validate:false)
      invoice.addToCommissions(commissionPago)
      invoice.addToCommissions(commissionFija)
      invoice.save(validate:false)
    when:"We stamp the invoice"
      def result = service.cancelStampedCommissionsInvoice(invoice)
    then:"We expect"
      result.status == CommissionsInvoiceStatus.CANCELED
  }

  void "Should get total invoiced = 0 for a company when it hasn't invoices created or stamped"() {
    given:"A company"
      Company company = new Company(rfc:"AAA010101AAA").save(validate:false)
    when:
      def result = service.getTotalInvoicedCommissionsForCompany(company)
    then:
      result == 0
  }

  @Unroll
  void "Should get total invoiced = #total for a company with invoices"() {
    given:"A company"
      Company company = new Company(rfc:"AAA010101AAA").save(validate:false)
    and:"The invoices"
      CommissionsInvoice invoice = new CommissionsInvoice(receiver:company, status:CommissionsInvoiceStatus.CREATED, commissions:[]).save(validate:false)
      CommissionTransaction commission = new CommissionTransaction(amount:amountCommission, invoice:invoice, company:company).save(validate:false)
      invoice.addToCommissions(commission)
      invoice.save(validate:false)
    when:
      def result = service.getTotalInvoicedCommissionsForCompany(company)
    then:
      result == total
    where:
      amountCommission  || total
      100               ||  116
      1000              ||  1160
  }

  void "Should add payment to commissions invoice and don't change status to payed"() {
    given:"A commissions invoice"
      CommissionsInvoice invoice = new CommissionsInvoice(status:CommissionsInvoiceStatus.STAMPED, commissions:[]).save(validate:false)
    and:"The commission transactions"
      CommissionTransaction commissionPago = new CommissionTransaction(type:CommissionType.PAGO, amount:new BigDecimal(100), status:CommissionTransactionStatus.INVOICED).save(validate:false)
      CommissionTransaction commissionFija = new CommissionTransaction(type:CommissionType.FIJA, amount:new BigDecimal(1000), status:CommissionTransactionStatus.INVOICED).save(validate:false)
      invoice.addToCommissions(commissionPago)
      invoice.addToCommissions(commissionFija)
      invoice.save(validate:false)
    and:"The payment amount"
      BigDecimal amount = new BigDecimal(500)
    when:
      def result = service.createPaymentToCommissionsInvoiceWithAmount(invoice, amount)
    then:
      result.status == CommissionsInvoiceStatus.STAMPED
      result.totalPayed == amount
  }

  void "Should add payment to commissions invoice and change its status to payed"() {
    given:"A commissions invoice"
      CommissionsInvoice invoice = new CommissionsInvoice(status:CommissionsInvoiceStatus.STAMPED, commissions:[]).save(validate:false)
    and:"The commission transactions"
      CommissionTransaction commissionPago = new CommissionTransaction(type:CommissionType.PAGO, amount:new BigDecimal(100), status:CommissionTransactionStatus.INVOICED).save(validate:false)
      CommissionTransaction commissionFija = new CommissionTransaction(type:CommissionType.FIJA, amount:new BigDecimal(1000), status:CommissionTransactionStatus.INVOICED).save(validate:false)
      invoice.addToCommissions(commissionPago)
      invoice.addToCommissions(commissionFija)
      invoice.save(validate:false)
    and:"The payment amount"
      BigDecimal amount = new BigDecimal(1276)
    when:
      def result = service.createPaymentToCommissionsInvoiceWithAmount(invoice, amount)
    then:
      result.status == CommissionsInvoiceStatus.PAYED
      result.amountToPay == 0
  }

}

