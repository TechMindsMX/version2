package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import java.lang.Void as Should
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(BusinessEntityService)
@Mock([BusinessEntity, ComposeName, ClientLink, Company, EmployeeLink, BankAccount])
class BusinessEntityServiceSpec extends Specification {

  def names = []
  def bankAccounts = []
  def addresses = []

  def bankAccountService = Mock(BankAccountService)
  SaleOrderService saleOrderService = Mock(SaleOrderService)
  PaymentService paymentService = Mock(PaymentService)
  XlsLayoutsBusinessEntityService xlsLayoutsBusinessEntityService = Mock(XlsLayoutsBusinessEntityService)
  EmployeeService employeeService = Mock(EmployeeService)

  def setup() {
    names.removeAll()
    bankAccounts.removeAll()
    addresses.removeAll()
    service.bankAccountService = bankAccountService
    service.saleOrderService = saleOrderService
    service.paymentService = paymentService
    service.xlsLayoutsBusinessEntityService = xlsLayoutsBusinessEntityService
    service.employeeService = employeeService
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

  @Unroll
  void "Should generate the layout for massive registration from business entity type"() {
    given:"The business entity type"
      String entityType = businessEntityType
    when:
      service.createLayoutForBusinessEntityType(entityType)
    then:
      callsClient * xlsLayoutsBusinessEntityService.generateLayoutForCLIENTE()
      callsClientProvider * xlsLayoutsBusinessEntityService.generateLayoutForCLIENTE_PROVEEDOR()
      callsProvider * xlsLayoutsBusinessEntityService.generateLayoutForPROVEEDOR()
      callsEmployee * xlsLayoutsBusinessEntityService.generateLayoutForEMPLEADO()
    where:
      businessEntityType  || callsClient  | callsClientProvider | callsProvider | callsEmployee
      "CLIENTE"           ||  1           | 0                   | 0             | 0
      "CLIENTE_PROVEEDOR" ||  0           | 1                   | 0             | 0
      "PROVEEDOR"         ||  0           | 0                   | 1             | 0
      "EMPLEADO"          ||  0           | 0                   | 0             | 1
  }

  void "Should create a compose name for row employee from file massive registration"() {
    given:"The row"
      Map rowEmployee = [PATERNO:"ApPaterno", MATERNO:"ApMaterno", NOMBRE:"Nombre"]
    and:"A businessEntity"
      BusinessEntity businessEntity = new BusinessEntity().save(validate:false)
    when:
      def be = service.createComposeNameForBusinessEntityFromRowEmployee(businessEntity, rowEmployee)
    then:
      be.names[0].value == "ApPaterno"
      be.names[0].type == NameType.APELLIDO_PATERNO
      be.names[1].value == "ApMaterno"
      be.names[1].type == NameType.APELLIDO_MATERNO
      be.names[2].value == "Nombre"
      be.names[2].type == NameType.NOMBRE
  }

  void "Should create a business entity for row employee from file massive"() {
    given:"The row employee"
      Map rowEmployee = [RFC:"PAGC770214422", PATERNO:"ApPaterno", MATERNO:"ApMaterno", NOMBRE:"Nombre"]
    when:
      def be = service.createBusinessEntityForRowEmployee(rowEmployee)
    then:
      be.id
      be.rfc == "PAGC770214422"
  }

  void "Should not create a business entity object for row employee when RFC is wrong"() {
    given:"The row employee"
      Map rowEmployee = [RFC:"XYZ123456ABC", PATERNO:"ApPaterno", MATERNO:"ApMaterno", NOMBRE:"Nombre"]
    when:
      def be = service.createBusinessEntityForRowEmployee(rowEmployee)
    then:
      be.hasErrors()
  }

  @Unroll
  void "Should obtain #expected for row employee #row"() {
    given:"A company"
      Company company = new Company(rfc:"RFCCompany").save(validate:false)
    and:"The row employee"
      Map rowEmployee = row
    and:"Find employee"
      employeeService.employeeAlreadyExistsInCompany(_,_) >> existingEmployee
      employeeService.createEmployeeForRowEmployee(_,_) >> employeeLink
      bankAccountService.createBankAccountForBusinessEntityFromRowEmployee(_,_) >> bankAccount
    when:
      def result = service.saveEmployeeImportData(rowEmployee, company)
    then:
      result == expected
    where:
      row       | existingEmployee    | employeeLink    |   bankAccount ||  expected
      [RFC:"PAG770214501", CURP:"PAGC770214HOCLTH00", PATERNO:"ApPaterno", MATERNO:"ApMaterno", NOMBRE:"Nombre", NO_EMPL:"EMP-100"]   |   null    | new EmployeeLink().save(validate:false) | null || "Error en el RFC"
      [RFC:"PAGC770214422", CURP:"PAGC770214HOCLTH00", PATERNO:"ApPaterno", MATERNO:"ApMaterno", NOMBRE:"Nombre", NO_EMPL:"EMP-100", CLABE:"036180009876543217", NUMTARJETA:"1234567890123456"]  |   null   | new EmployeeLink().save(validate:false)  | new BankAccount().save(validate:false) || "Registrado"
      [RFC:"PAGC770214422", CURP:"PAGC871011HOCLTH00", PATERNO:"ApPaterno", MATERNO:"ApMaterno", NOMBRE:"Nombre", NO_EMPL:"EMP-100"]  |   new EmployeeLink().save(validate:false)   |  null | null  || "Error, el RFC del empleado ya existe"
  }

}
