package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import java.lang.Void as Should
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(BusinessEntityService)
@Mock([BusinessEntity, ComposeName, ClientLink, Company])
class BusinessEntityServiceSpec extends Specification {

  def names = []
  def bankAccounts = []
  def addresses = []

  def bankAccountService = Mock(BankAccountService)
  SaleOrderService saleOrderService = Mock(SaleOrderService)
  PaymentService paymentService = Mock(PaymentService)

  def setup() {
    names.removeAll()
    bankAccounts.removeAll()
    addresses.removeAll()
    service.bankAccountService = bankAccountService
    service.saleOrderService = saleOrderService
    service.paymentService = paymentService
  }

  @Unroll
  void "Should append names to business entity for properties #properties"() {
    given:"a business entity"
      def businessEntity = new BusinessEntity(rfc:"MDE130712JA6")
      businessEntity.save(validate:false)
    when:""
      def biSaved = service.appendNamesToBusinessEntity(businessEntity, properties)
    then:""
      biSaved.names.size() == sizeExpected
    where:
      properties                                      || sizeExpected
      ["Nombre", "Paterno", "Materno"] as String[]    ||  3
      ["Nombre", "Paterno", "Materno", "NoEmpleado"] as String[]  ||  4
  }

  void "Should append data to business entity"() {
    given:"a business entity"
      def businessEntity = new BusinessEntity(rfc:"MDE130712JA6")
      businessEntity.save(validate:false)
    when:""
      def biSaved = service.appendDataToBusinessEntity(businessEntity, "Negocio")
    then:""
      biSaved.names.size() == 1
  }

  void "Should create a bank account and adding to business entity"() {
    given:"A business entity and a bank account"
      def bankAccount = Mock(BankAccount)
      def businessEntity = new BusinessEntity()
      businessEntity.metaClass.addToBanksAccounts = {
        bankAccounts.add(bankAccount)
      }
    and:
      bankAccountService.createABankAccount(_) >> bankAccount
    when:
      def result = service.createBankAccountAndAddToBusinesEntity([:], businessEntity)
    then:
      1 * bankAccount.save()
      bankAccounts.size() == 1
  }

  void "Should add an address to business entity"() {
    given:"A business entity and an address"
      def address = Mock(Address)
      def businessEntity = new BusinessEntity(rfc:"AAA010101AAA")
      businessEntity.metaClass.addToAddresses = {
        addresses.add(address)
      }
      BusinessEntity.metaClass.static.get = {Long id -> businessEntity }
    when:
      def result = service.createAddressForBusinessEntity(address, 1)
    then:
      addresses.size() == 1
  }

  @Unroll
  void "Should obtain data for business entity of type client or client-provider"() {
    given:"A business entity"
      BusinessEntity businessEntity = new BusinessEntity(rfc:"RFC").save(validate:false)
    and:"A company"
      Company company = new Company().save(validate:false)
    and:"A client link"
      ClientLink clientLink = new ClientLink(company:company, clientRef:"RFC").save(validate:false)
    and:
      saleOrderService.getTotalSoldForClient(_,_) >> 0
      saleOrderService.getTotalSoldForClientStatusConciliated(_,_) >> 0
      paymentService.getPaymentsFromClientToPay(_,_) >> 0
    when:
      def data = service.getClientData(company, businessEntity, relation)
    then:
      data as Boolean == result
    where:
      relation                    ||  result
      LeadType.CLIENTE            ||  true
      LeadType.CLIENTE_PROVEEDOR  ||  true
      LeadType.PROVEEDOR          ||  false
      LeadType.EMPLEADO           ||  false

  }
}
