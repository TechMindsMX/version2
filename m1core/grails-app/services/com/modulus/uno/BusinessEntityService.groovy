package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class BusinessEntityService {

  def clientService
  def providerService
  def employeeService
  def bankAccountService

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
    }
    if((leadType == LeadType.PROVEEDOR || leadType == LeadType.CLIENTE_PROVEEDOR) && businessEntity.id){
      providerService.addProviderToCompany(businessEntity, company)
    }
    if(leadType == LeadType.EMPLEADO){
      businessEntity.website = null
      employeeService.addEmployeeToCompany(businessEntity, company,params.curp)
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
    client = clientService.generateSubAccountStp(client, businessEntity)
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

    deleteLinksForRfc(businessEntity.rfc)

    if(leadType == LeadType.CLIENTE || leadType == LeadType.CLIENTE_PROVEEDOR){
      clientService.addClientToCompany(businessEntity, company)
    }
    if(leadType == LeadType.PROVEEDOR || leadType == LeadType.CLIENTE_PROVEEDOR){
      providerService.addProviderToCompany(businessEntity, company)
    }
    if(leadType == LeadType.EMPLEADO){
      employeeService.addEmployeeToCompany(businessEntity, company, params.curp)
    }

  }

}
