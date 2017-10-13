package com.modulus.uno

import grails.transaction.Transactional
import org.springframework.transaction.annotation.Propagation

@Transactional
class BusinessEntityService {

  def clientService
  def providerService
  def employeeService
  def bankAccountService
  def emailSenderService
  SaleOrderService saleOrderService
  PaymentService paymentService
  XlsLayoutsBusinessEntityService xlsLayoutsBusinessEntityService
  XlsImportService xlsImportService
  DataImssEmployeeService dataImssEmployeeService

  @Transactional
  def generatedBussinessEntityProperties(BusinessEntity businessEntity, def params, Company company) {
    LeadType leadType = LeadType."${params.clientProviderType}"
    if(params.persona == 'fisica'){
      appendNamesToBusinessEntity(businessEntity, (String[])[params.name, params.lastName, params.motherLastName,params.numeroEmpleado])
    } else {
      appendDataToBusinessEntity(businessEntity, params.businessName)
    }

    if (params.numeroEmpleado && params.banco && params.clabe && params.cuenta){
      Map propertie = ["clabe":params.clabe]
      createBankAccountAndAddToBusinesEntity(propertie,businessEntity)
    }

    if((leadType == LeadType.CLIENTE || leadType == LeadType.CLIENTE_PROVEEDOR) && businessEntity.id){
      clientService.addClientToCompany(businessEntity, company)
      emailSenderService.sendEmailForNewClient(company, businessEntity)
    }
    if((leadType == LeadType.PROVEEDOR || leadType == LeadType.CLIENTE_PROVEEDOR) && businessEntity.id){
      providerService.addProviderToCompany(businessEntity, company)
      emailSenderService.sendEmailForNewProvider(company, businessEntity)
    }
    if(leadType == LeadType.EMPLEADO){
      businessEntity.website = null
      employeeService.addEmployeeToCompany(businessEntity, company, params)
      emailSenderService.sendEmailForNewEmployee(company, businessEntity)
    }

  }

  @Transactional
  def appendNamesToBusinessEntity(BusinessEntity businessEntity, String[] properties) {
    def name = new ComposeName(value:properties[0], type:NameType.NOMBRE)
    def lastName = new ComposeName(value:properties[1], type:NameType.APELLIDO_PATERNO)
    def motherLastName = new ComposeName(value:properties[2], type:NameType.APELLIDO_MATERNO)
    if (properties.length > 3) {
      if (properties[3])
        businessEntity.addToNames(new ComposeName(value:properties[3], type:NameType.NUMERO_EMPLEADO))
    }
    businessEntity.addToNames(name)
    businessEntity.addToNames(lastName)
    businessEntity.addToNames(motherLastName)
    businessEntity.save()
    businessEntity
  }

  def appendDataToBusinessEntity(BusinessEntity businessEntity, String businessName) {
    businessEntity.addToNames(new ComposeName(value:businessName, type:NameType.RAZON_SOCIAL))
    businessEntity.save()
    businessEntity
  }

  @Transactional
  def createBankAccountAndAddToBusinesEntity(Map properties,BusinessEntity businessEntity) {
    def bankAccountCommand = bankAccountService.createABankAccountCommandByParams(properties)
    def bankAccount = bankAccountService.createABankAccount(bankAccountCommand)
    bankAccount.save()
    businessEntity.addToBanksAccounts(bankAccount)
    businessEntity.save()
  }

  @Transactional
  def createAddressForBusinessEntity(Address address, Long businessEntityId) {
    def businessEntity = BusinessEntity.get(businessEntityId)
    businessEntity.addToAddresses(address)
    businessEntity.save()
    businessEntity
  }

  @Transactional
  def deleteLinksForRfc(String rfc){
    def clientLink = ClientLink.findByClientRef(rfc)
    clientLink?.delete()
    def providerLink = ProviderLink.findByProviderRef(rfc)
    providerLink?.delete()
    def employeeLink = EmployeeLink.findByEmployeeRef(rfc)
    employeeLink?.delete()

  }

  def findBusinessEntityByKeyword(String keyword, String entity, Company company){
    if (!entity)
      entity=""
    def criteria = BusinessEntity.createCriteria()
    def list = criteria.listDistinct {
      or{
        like 'rfc', "%${keyword}%"
        names{
          like 'value', "%${keyword}%"
        }
      }

    }
    list.collect {
      if (((entity.equals("CLIENT") && clientService.isClientOfThisCompany(it, company))
          || (entity.equals("PROVIDER") && providerService.isProviderOfThisCompany(it, company))
          || (entity.equals("EMPLOYEE") && employeeService.isEmployeeOfThisCompany(it, company))
					|| entity=="")
          && company.businessEntities.contains(it)
      )
        it
    }.findResults{it}.unique()
  }

  def getClientProviderType(String rfc){
    def providerLink = ProviderLink.findByProviderRef(rfc)
    def clientLink = ClientLink.findByClientRef(rfc)
    def employeeLink = EmployeeLink.findByEmployeeRef(rfc)
    if(providerLink && clientLink) return LeadType.CLIENTE_PROVEEDOR
      if(clientLink) return LeadType.CLIENTE
        if(providerLink) return LeadType.PROVEEDOR
          if (employeeLink) return LeadType.EMPLEADO
  }

  @Transactional
  def updateNamesToBusinessEntity(BusinessEntity businessEntity, String[] names) {
    businessEntity.names.each{
      if(it.type == NameType.NOMBRE){
        it.value = names[0]
      }
    }
    businessEntity.names.each{
      if(it.type == NameType.APELLIDO_PATERNO){
        it.value = names[1]
      }
    }
    businessEntity.names.each{
      if(it.type == NameType.APELLIDO_MATERNO){
        it.value = names[2]
      }
    }
    businessEntity.save()
    businessEntity
  }

  @Transactional
  def updateDataToBusinessEntity(BusinessEntity businessEntity, String businessName) {
    businessEntity.names.each{
      if(it.type == NameType.RAZON_SOCIAL){
        it.value = businessName
      }
    }
    businessEntity.save()
    businessEntity
  }

  def findBusinessEntityAndProviderLinkByRFC(String rfc){
    def businessEntity =  BusinessEntity.findByRfc(rfc)
    if(businessEntity)
      return ProviderLink.findByProviderRef(businessEntity.rfc)
      businessEntity

  }

  def knowIfBusinessEntityHaveABankAccountOrAddress(def rfc) {
    def businessEntity = BusinessEntity.findByRfc(rfc)
    [businessEntity.banksAccounts,businessEntity.addresses]
  }

  @Transactional
  def generateSubAccountStp(Company company, BusinessEntity businessEntity) {
    ClientLink client = ClientLink.findByCompanyAndClientRef(company, businessEntity.rfc)
    client = clientService.generateSubAccountStp(client)
    client
  }

  ClientLink getClientLinkOfBusinessEntityAndCompany(BusinessEntity businessEntity, Company company) {
    ClientLink.findByCompanyAndClientRef(company, businessEntity.rfc)
  }

  @Transactional
  def updateBusinessEntity(BusinessEntity businessEntity, Company company, def params) {
    LeadType leadType = LeadType."${params.clientProviderType}"
    if(businessEntity.type == BusinessEntityType.FISICA){
      updateNamesToBusinessEntity(businessEntity, (String[])[params.name, params.lastName, params.motherLastName])
    } else {
      updateDataToBusinessEntity(businessEntity, params.businessName)
    }


    if(leadType == LeadType.CLIENTE || leadType == LeadType.CLIENTE_PROVEEDOR){
      clientService.updateClientToCompany(businessEntity, params.backRfc)
    }
    if(leadType == LeadType.PROVEEDOR || leadType == LeadType.CLIENTE_PROVEEDOR){
      deleteLinksForRfc(businessEntity.rfc)
      providerService.addProviderToCompany(businessEntity, company)
    }
    if(leadType == LeadType.EMPLEADO){
      employeeService.updateEmployeeToCompany(businessEntity, company, params)
    }

  }

  Map getClientData(Company company, BusinessEntity businessEntity, LeadType relation) {
    Map clientData = [:]
    if (relation == LeadType.CLIENTE || relation == LeadType.CLIENTE_PROVEEDOR) {
      clientData.clientLink = getClientLinkOfBusinessEntityAndCompany(businessEntity, company)
      clientData.totalSoldForClient = saleOrderService.getTotalSoldForClient(company,businessEntity.rfc) ?: 0
      clientData.totalSoldForClientStatusConciliated = saleOrderService.getTotalSoldForClientStatusConciliated(company,businessEntity.rfc) ?: 0
      clientData.paymentsFromClientToPay = paymentService.getPaymentsFromClientToPay(company, businessEntity.rfc) ?: 0
      clientData.totalPending =  clientData.totalSoldForClient - clientData.totalSoldForClientStatusConciliated
    }
    clientData
  }

  DataImssEmployee getDataImssEmployee(Company company, BusinessEntity businessEntity, LeadType relation) {
    DataImssEmployee dataImssEmployee
    if (relation == LeadType.EMPLEADO) {
      EmployeeLink employee = EmployeeLink.findByCompanyAndEmployeeRef(company, businessEntity.rfc)
      dataImssEmployee = DataImssEmployee.findByEmployee(employee)
    }
    dataImssEmployee
  }

  def createLayoutForBusinessEntityType(String entityType) {
    xlsLayoutsBusinessEntityService."generateLayoutFor${entityType.toUpperCase()}"()
  }

  def processXlsMassiveForEMPLEADO(def file, Company company) {
    log.info "Processing massive registration for Employee"
    List data = xlsImportService.parseXlsMassiveEmployee(file)
    List results = processDataFromXls(data, company)
    log.info "Data: ${data}"
    log.info "Results: ${results}"
    [data:data, results:results]
  }


  List processDataFromXls(List data, Company company) {
    List results = []
    data.each { employee ->
      String result = saveEmployeeImportData(employee, company)
      results.add(result)
      if (result == "Registrado") {
        addEmployeeToCompany(employee.RFC, company)
      }
    }
    results
  }

  @Transactional
  def addEmployeeToCompany(String rfc, Company company) {
    log.debug "Adding employee to company: ${rfc}"
    BusinessEntity businessEntity = BusinessEntity.findByRfc(rfc)
    company.addToBusinessEntities((EmployeeBusinessEntity) businessEntity)
    company.save()
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  def saveEmployeeImportData(Map rowEmployee, Company company) {
    if (employeeService.employeeAlreadyExistsInCompany(rowEmployee.RFC, company)) {
      transactionStatus.setRollbackOnly()
      return "Error: el RFC del empleado ya existe"
    }

    EmployeeLink employeeLink = employeeService.createEmployeeForRowEmployee(rowEmployee, company)
    if (!employeeLink || employeeLink?.hasErrors()) {
      transactionStatus.setRollbackOnly()
      return "Error: CURP"
    }

    BusinessEntity businessEntity = createBusinessEntityForRowEmployee(rowEmployee)
    if (businessEntity.hasErrors()) {
      transactionStatus.setRollbackOnly()
      return "Error: RFC"
    }

    BankAccount bankAccount = bankAccountService.createBankAccountForBusinessEntityFromRowEmployee(businessEntity, rowEmployee)
    if (!bankAccount || bankAccount?.hasErrors()) {
      transactionStatus.setRollbackOnly()
      return "Error: datos bancarios"
    }

    if (rowEmployee.IMSS == "S") {
      DataImssEmployee dataImssEmployee = dataImssEmployeeService.createDataImssForRowEmployee(rowEmployee, employeeLink)
      if (!dataImssEmployee || dataImssEmployee?.hasErrors()) {
        transactionStatus.setRollbackOnly()
        return "Error: datos de IMSS"
      }
    }

    "Registrado"
  }

  def createBusinessEntityForRowEmployee(Map employeeMap) {
    BusinessEntity businessEntity = new BusinessEntity(
      rfc:employeeMap.RFC,
      type:BusinessEntityType.FISICA,
      status:BusinessEntityStatus.TO_AUTHORIZE
    )

    businessEntity.save()

    createComposeNameForBusinessEntityFromRowEmployee(businessEntity, employeeMap)
  }

  def createComposeNameForBusinessEntityFromRowEmployee(BusinessEntity businessEntity, Map employeeMap) {
    ComposeName lastName = new ComposeName(value:employeeMap.PATERNO, type:NameType.APELLIDO_PATERNO)
    ComposeName motherLastName = new ComposeName(value:employeeMap.MATERNO, type:NameType.APELLIDO_MATERNO)
    ComposeName name = new ComposeName(value:employeeMap.NOMBRE, type:NameType.NOMBRE)
    businessEntity.addToNames(lastName)
    businessEntity.addToNames(motherLastName)
    businessEntity.addToNames(name)
    businessEntity.save()
    businessEntity
  }

  def getBusinessEntitiesToAuthorizeForCompany(Company company) {
    def beToAuthorize = company.businessEntities.findAll { be ->
      be.status == BusinessEntityStatus.TO_AUTHORIZE
    }
    beToAuthorize
  }

  @Transactional
  def authorizeBusinessEntities(String ids) {
    if (ids) {
      List<BusinessEntity> businessEntities = getBusinessEntitiesFromIds(ids)
      log.info "Business entities: ${businessEntities}"
      businessEntities.each { be ->
        be.status = BusinessEntityStatus.ACTIVE
        be.save(flush:true)
      }
    }
  }

  List<BusinessEntity> getBusinessEntitiesFromIds(String ids) {
    Scanner scanner = new Scanner(ids.replace(","," "))
    List<Integer> intIds = []
    while (scanner.hasNextInt()) {
      intIds.add(scanner.nextInt())
    }
    BusinessEntity.getAll(intIds)
  }

  List<BusinessEntity> getAllActiveEmployeesForCompany(Company company) {
    company.businessEntities.findAll { be ->
      (be.status == BusinessEntityStatus.ACTIVE) && (EmployeeLink.findByEmployeeRef(be.rfc))
    }.sort { it.id }
  }

  boolean existsBusinessEntityInCompany(String rfc, Company company) {
    ProviderLink.findByProviderRef(rfc) || ClientLink.findByClientRef(rfc) || EmployeeLink.findByEmployeeRef(rfc)
  }

}
