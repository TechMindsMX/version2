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
import com.modulus.uno.RestException

import com.modulus.uno.invoice.DatosDeFacturacion
import com.modulus.uno.invoice.Contribuyente
import com.modulus.uno.invoice.DatosFiscales

@TestFor(CreditNoteService)
@Mock([CreditNote, CreditNoteItem, User, SaleOrder, SaleOrderItem, Company, Authorization])
class CreditNoteServiceSpec extends Specification {

  EmailSenderService emailSenderService = Mock(EmailSenderService)
  InvoiceService invoiceService = Mock(InvoiceService)
  RestService restService = Mock(RestService)
  CommissionTransactionService commissionTransactionService = Mock(CommissionTransactionService)

  def setup(){
    service.emailSenderService = emailSenderService
    service.invoiceService = invoiceService
    service.restService = restService
    service.commissionTransactionService = commissionTransactionService
    grailsApplication.config.modulus.facturaCreate = "EndPoint_to_stamp"
  }

  @Unroll
  void "Should change status to #newStatus for a credit note when call the service #theService"() {
    given:"The credit note"
      CreditNote creditNote = new CreditNote(status:currentStatus).save(validate:false)
    when:
      def result = service."${theService}"(creditNote)
    then:
      result.status == newStatus
      1 * emailSenderService.notifyCreditNoteChangeStatus(_)
    where:
      currentStatus       |       theService      ||    newStatus
      CreditNoteStatus.CREATED        |   "sendToAuthorize"     || CreditNoteStatus.TO_AUTHORIZE 
      CreditNoteStatus.TO_AUTHORIZE   |   "cancelCreditNote"    || CreditNoteStatus.CANCELED
      CreditNoteStatus.AUTHORIZED     |   "rejectCreditNote"    || CreditNoteStatus.REJECTED
      CreditNoteStatus.APPLIED        |   "sendToAuthorizeCancelCreditNote"    || CreditNoteStatus.CANCEL_TO_AUTHORIZE
      CreditNoteStatus.CANCEL_TO_AUTHORIZE        |   "authorizeCancelCreditNote"    || CreditNoteStatus.CANCEL_AUTHORIZED
      CreditNoteStatus.CANCEL_AUTHORIZED        |   "executeCancelCreditNote"    || CreditNoteStatus.CANCEL_APPLIED
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

  void "Should apply a credit note"() {
    given:"The credit note"
      CreditNote creditNote = new CreditNote(status:CreditNoteStatus.TO_AUTHORIZE, authorizations:[new Authorization().save(validate:false)]).save(validate:false)
      CreditNoteItem item = new CreditNoteItem(quantity:1, price:10).save(validate:false)
      creditNote.addToItems(item)
      creditNote.save(validate:false)
    and:"The sale order"
      Company company = new Company(numberOfAuthorizations:2).save(validate:false)
      SaleOrder saleOrder = createSaleOrderWithCompany(company)
      creditNote.saleOrder = saleOrder
      creditNote.save(validate:false)
    and:
      invoiceService.createInvoiceFromSaleOrder(_) >> new FacturaCommand(datosDeFacturacion:new DatosDeFacturacion(), receptor: new Contribuyente(datosFiscales:new DatosFiscales()))
      restService.sendFacturaCommandWithAuth(_, _) >> [text:"{\"stampId\":\"CREDIT_NOTE_STAMP_UUID\", \"serie\":\"SERIE\", \"folio\":\"FOLIO\"}"]
    when:
      def result = service.processApplyCreditNote(creditNote)
    then:
      result.status == CreditNoteStatus.XML_GENERATED
      1 * emailSenderService.notifyCreditNoteChangeStatus(_)
      1 * commissionTransactionService.registerCommissionForCreditNote(_)
  }

  void "Should throw an exception when the stamp service return an error"() {
    given:"The credit note"
      CreditNote creditNote = new CreditNote(status:CreditNoteStatus.TO_AUTHORIZE, authorizations:[new Authorization().save(validate:false)]).save(validate:false)
      CreditNoteItem item = new CreditNoteItem(quantity:1, price:10).save(validate:false)
      creditNote.addToItems(item)
      creditNote.save(validate:false)
    and:"The sale order"
      Company company = new Company(numberOfAuthorizations:2).save(validate:false)
      SaleOrder saleOrder = createSaleOrderWithCompany(company)
      creditNote.saleOrder = saleOrder
      creditNote.save(validate:false)
    and:
      invoiceService.createInvoiceFromSaleOrder(_) >> new FacturaCommand(datosDeFacturacion:new DatosDeFacturacion(), receptor: new Contribuyente(datosFiscales:new DatosFiscales()))
      restService.sendFacturaCommandWithAuth(_, _) >> [text:"{\"error\":\"ERROR WHEN TRY STAMP\"}"]
    when:
      def result = service.processApplyCreditNote(creditNote)
    then:
      thrown RestException
  }

  private SaleOrder createSaleOrderWithCompany(Company company) {
    SaleOrderItem item = new SaleOrderItem(quantity:1, price:100).save(validate:false)
    SaleOrder saleOrder = new SaleOrder(company:company, payments:[], folio:"STAMP_UUID_STAMP_UUID_STAMP_UUID_STAMP_UUID_STAMP_UUID_").save(validate:false)
    saleOrder.addToItems(item)
    saleOrder.save(validate:false)
    saleOrder
  }
}
