package com.modulus.uno.saleorder

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll

import com.modulus.uno.Company
import com.modulus.uno.User
import com.modulus.uno.Authorization
import com.modulus.uno.Commission
import com.modulus.uno.status.CreditNoteStatus
import com.modulus.uno.CommissionType

import com.modulus.uno.EmailSenderService
import com.modulus.uno.CommissionTransactionService
import com.modulus.uno.RestService

@TestFor(CreditNoteService)
@Mock([CreditNote, CreditNoteItem, User, SaleOrder, SaleOrderItem, Company, Authorization])
class CreditNoteServiceSpec extends Specification {

  EmailSenderService emailSenderService = Mock(EmailSenderService)
  InvoiceService invoiceService = Mock(InvoiceService)
  CommissionTransactionService commissionTransactionService = Mock(CommissionTransactionService)

  def setup(){
    service.emailSenderService = emailSenderService
    service.invoiceService = invoiceService
    service.commissionTransactionService = commissionTransactionService
  }

  void "Should change status to TO_AUTHORIZE for a credit note"() {
    given:"The credit note"
      CreditNote creditNote = new CreditNote(status:CreditNoteStatus.CREATED).save(validate:false)
    when:
      def result = service.sendToAuthorize(creditNote)
    then:
      result.status == CreditNoteStatus.TO_AUTHORIZE
      1 * emailSenderService.notifyCreditNoteChangeStatus(_)
  }

  void "Should add authorization to credit note but not change status to AUTHORIZED"() {
    given:"The credit note"
      CreditNote creditNote = new CreditNote(status:CreditNoteStatus.TO_AUTHORIZE, authorizations:[]).save(validate:false)
      CreditNoteItem item = new CreditNoteItem(quantity:1, price:10).save(validate:false)
      creditNote.addToItems(item)
      creditNote.save(validate:false)
    and:"The authorizations number for company"
      Company company = new Company(numberOfAuthorizations:2).save(validate:false)
      SaleOrder saleOrder = createSaleOrderWithCompany(company)
      creditNote.saleOrder = saleOrder
      creditNote.save(validate:false)
    and:
      User user = new User(username:"user").save(validate:false)
      service.springSecurityService = [currentUser:user]
    when:
      def result = service.processAuthorization(creditNote)
    then:
      result.status == CreditNoteStatus.TO_AUTHORIZE
      result.authorizations.size() == 1
  }

  void "Should add authorization to credit note and change status to AUTHORIZED"() {
    given:"The credit note"
      CreditNote creditNote = new CreditNote(status:CreditNoteStatus.TO_AUTHORIZE, authorizations:[new Authorization().save(validate:false)]).save(validate:false)
      CreditNoteItem item = new CreditNoteItem(quantity:1, price:10).save(validate:false)
      creditNote.addToItems(item)
      creditNote.save(validate:false)
    and:"The authorizations number for company"
      Company company = new Company(numberOfAuthorizations:2).save(validate:false)
      SaleOrder saleOrder = createSaleOrderWithCompany(company)
      creditNote.saleOrder = saleOrder
      creditNote.save(validate:false)
    and:
      User user = new User(username:"user").save(validate:false)
      service.springSecurityService = [currentUser:user]
    when:
      def result = service.processAuthorization(creditNote)
    then:
      result.status == CreditNoteStatus.AUTHORIZED
      result.authorizations.size() == 2
      1 * emailSenderService.notifyCreditNoteChangeStatus(_)
  }

  private SaleOrder createSaleOrderWithCompany(Company company) {
    SaleOrderItem item = new SaleOrderItem(quantity:1, price:100).save(validate:false)
    SaleOrder saleOrder = new SaleOrder(company:company, payments:[]).save(validate:false)
    saleOrder.addToItems(item)
    saleOrder.save(validate:false)
    saleOrder
  }
}
