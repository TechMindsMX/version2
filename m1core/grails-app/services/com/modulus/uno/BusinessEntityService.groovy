package com.modulus.uno

import grails.transaction.Transactional

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
      emailSenderService.sendEmailForNewProvider(company, provider)
    }
    if(leadType == LeadType.EMPLEADO){
      businessEntity.website = null
      employeeService.addEmployeeToCompany(businessEntity, company, params)
      emailSenderService.sendEmailForNewEmployee(company, businessEntity)
    }

  }

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

  def createBankAccountAndAddToBusinesEntity(Map properties,BusinessEntity businessEntity) {
    def bankAccountCommand = bankAccountService.createABankAccountCommandByParams(properties)
    def bankAccount = bankAccountService.createABankAccount(bankAccountCommand)
    bankAccount.save()
    businessEntity.addToBanksAccounts(bankAccount)
    businessEntity.save()
  }

  def createAddressForBusinessEntity(Address address, Long businessEntityId) {
    def businessEntity = BusinessEntity.get(businessEntityId)
    businessEntity.addToAddresses(address)
    businessEntity.save()
    businessEntity
  }

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
      if ((entity.equals("CLIENT") && clientService.isClientOfThisCompany(it, company))
          || (entity.equals("PROVIDER") && providerService.isProviderOfThisCompany(it, company))
          || (entity.equals("EMPLOYEE") && employeeService.isEmployeeOfThisCompany(it, company))
          || (entity.equals("") && (clientService.isClientOfThisCompany(it, company) || providerService.isProviderOfThisCompany(it, company) || employeeService.isEmployeeOfThisCompany(it, company)))
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

  def generateSubAccountStp(Company company, BusinessEntity businessEntity) {
    ClientLink client = ClientLink.findByCompanyAndClientRef(company, businessEntity.rfc)
    client = clientService.generateSubAccountStp(client)
    client
  }

  ClientLink getClientLinkOfBusinessEntityAndCompany(BusinessEntity businessEntity, Company company) {
    ClientLink.findByCompanyAndClientRef(company, businessEntity.rfc)
  }

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

  boolean existsBusinessEntityInCompany(String rfc, Company company) {
    ProviderLink.findByProviderRef(rfc) || ClientLink.findByClientRef(rfc) || EmployeeLink.findByEmployeeRef(rfc)
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
    File xlsFile = new File(file.getOriginalFilename())
    xlsFile.createNewFile()
    FileOutputStream fos = new FileOutputStream(xlsFile);
    fos.write(file.getBytes());
    fos.close();
    List data = xlsImportService.parseXlsMassiveEmployee(xlsFile)
      //data.each { employee ->
        //saveEmployeeImportData(employee)
        //crear business entity
        //crear employee link
        //crear data imss employee
      //}
  }

  /*@Transactional
  def saveEmployeeImportData(Map employee) {
    BusinessEntity businessEntity = createBusinessEntityForMapEmployee(company, employee)
  }

  def createBusinessEntityForRowEmployee(Company company, Map employeeMap) {
    BusinessEntity businessEntity = new BusinessEntity(
      rfc:employeeMap.RFC,
      type:BusinessEntityType.FISICA,
      status:BusinessEntityStatus.TO_AUTHORIZE
    )
    ComposeName lastName = new ComposeName(value:employeeMap.PATERNO, type:NameType.APELLIDO_PATERNO)
    ComposeName motherLastName = new ComposeName(value:employeeMap.MATERNO, type:NameType.APELLIDO_MATERNO)
    ComposeName name = new ComposeName(value:employeeMap.NOMBRE, type:NameType.NOMBRE)
    businessEntity.addToNames(lastName)
    businessEntity.addToNames(motherLastName)
    businessEntity.addToNames(name)
    businessEntity.save()
  }*/

}
