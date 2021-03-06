package com.modulus.uno

import grails.transaction.Transactional
import org.springframework.transaction.annotation.Propagation
import com.modulus.uno.paysheet.ContractType
import com.modulus.uno.paysheet.RegimeType
import com.modulus.uno.paysheet.WorkDayType
import com.modulus.uno.saleorder.SaleOrderService

@Transactional
class BusinessEntityService {

  def clientService
  def providerService
  def employeeService
  def bankAccountService
  def addressService
  def emailSenderService
  SaleOrderService saleOrderService
  PaymentService paymentService
  XlsLayoutsBusinessEntityService xlsLayoutsBusinessEntityService
  XlsImportService xlsImportService
  DataImssEmployeeService dataImssEmployeeService
  UserService userService

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

  def findBusinessEntityByKeyword(String keyword, String entity, Company company){
    if (!entity)
      entity=""
    def criteria = BusinessEntity.createCriteria()
    def list = criteria.listDistinct {
      eq 'status', BusinessEntityStatus.ACTIVE
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
  def updateBusinessNameToBusinessEntity(BusinessEntity businessEntity, String businessName) {
    businessEntity.names.each{
      if(it.type == NameType.RAZON_SOCIAL){
        it.value = businessName
      }
    }
    businessEntity.save()
    businessEntity
  }

  def findBusinessEntityAndProviderLinkByRFC(String rfc){
    BusinessEntity businessEntity =  BusinessEntity.findByRfc(rfc)
    Company company = Company.list().find { it.businessEntities.id.contains(businessEntity.id) } 
    if(businessEntity)
      return ProviderLink.findByProviderRefAndCompany(businessEntity.rfc, company)
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
    updateDataToBusinessEntity(businessEntity, company, params)
    updateLeadTypeToBusinessEntity(businessEntity, params.clientProviderType, company)
    businessEntity
  }

  def updateDataToBusinessEntity(BusinessEntity businessEntity, Company company, def params) {
    if(businessEntity.type == BusinessEntityType.FISICA){
      updateNamesToBusinessEntity(businessEntity, (String[])[params.name, params.lastName, params.motherLastName])
      updateDataToEmployeeLink(businessEntity, company, params)
    } else {
      updateBusinessNameToBusinessEntity(businessEntity, params.businessName)
    }
  }

  def updateDataToEmployeeLink(BusinessEntity businessEntity, Company company, def params) {
    LeadType currentLeadType = getClientProviderType(businessEntity.rfc)
    if (currentLeadType == LeadType.EMPLEADO) {
      employeeService.updateEmployeeToCompany(businessEntity, company, params)
    }
  }

  def updateLeadTypeToBusinessEntity(BusinessEntity businessEntity, String leadType, Company company) {
    LeadType newLeadType = LeadType."${leadType}"
    LeadType currentLeadType = getClientProviderType(businessEntity.rfc)
    log.info "Update lead type from ${currentLeadType} to ${newLeadType}"
    if ((newLeadType == LeadType.EMPLEADO || currentLeadType == LeadType.EMPLEADO) && currentLeadType != newLeadType) {
      throw new BusinessException("No es posible cambiar de ${currentLeadType} a ${newLeadType}")
    }
    if (currentLeadType != newLeadType) {
      "updateFrom${currentLeadType}To${newLeadType}"(businessEntity, company)
    }
  }

  def updateFromCLIENTEToPROVEEDOR(BusinessEntity businessEntity, Company company) {
    clientService.deleteClientLinkForRfcAndCompany(businessEntity.rfc, company)
    providerService.addProviderToCompany(businessEntity, company)
  }

  def updateFromCLIENTEToCLIENTE_PROVEEDOR(BusinessEntity businessEntity, Company company) {
    providerService.addProviderToCompany(businessEntity, company)
  }

  def updateFromPROVEEDORToCLIENTE(BusinessEntity businessEntity, Company company) {
    providerService.deleteProviderLinkForRfcAndCompany(businessEntity.rfc, company)
    clientService.addClientToCompany(businessEntity, company)
  }

  def updateFromPROVEEDORToCLIENTE_PROVEEDOR(BusinessEntity businessEntity, Company company) {
    clientService.addClientToCompany(businessEntity, company)
  }

  def updateFromCLIENTE_PROVEEDORToCLIENTE(BusinessEntity businessEntity, Company company) {
    providerService.deleteProviderLinkForRfcAndCompany(businessEntity.rfc, company)
  }

  def updateFromCLIENTE_PROVEEDORToPROVEEDOR(BusinessEntity businessEntity, Company company) {
    clientService.deleteClientLinkForRfcAndCompany(businessEntity.rfc, company)
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

  def exportXlsForBusinessRelationships(String entityType, Company company) {
    def businessEntityList = company.businessEntities.toList().sort{it.id}
    if(entityType != "All Entities"){
      return xlsLayoutsBusinessEntityService.exportListOfBusinessEntities(giveListOfTheEntityType(entityType, businessEntityList))
    }
    else {
      return xlsLayoutsBusinessEntityService.exportListOfBusinessEntities(businessEntityList)
    } 
  }

  List<BusinessEntity> giveListOfTheEntityType(String entityType, List<BusinessEntity> businessEntityList){
    def businessEntityListForType = []
    businessEntityList.each{ data ->
      data.getBusinessEntityType().toString() == entityType?businessEntityListForType << data: " "
    }
    businessEntityListForType
  }

  def processXlsMassiveForEMPLEADO(def file, Company company) {
    log.info "Processing massive registration for Employee"
    File xlsFile = xlsImportService.getFileToProcess(file)
    List data = xlsImportService.parseXlsMassiveEmployee(xlsFile)
    def headers = getKeyForData(data)
    List results = processDataFromXlsEMPLEADO(data, company)
    log.info "Headers: ${headers}"
    log.info "Results: ${results}"
    [results:results, headers:headers, data:data]
  }

  def processXlsMassiveForCLIENTE(def file, Company company) {
    log.info "Processing massive registration for Client"
    File xlsFile = xlsImportService.getFileToProcess(file)
    List data = xlsImportService.parseXlsMassiveClient(xlsFile)
    def headers = getKeyForData(data)
    List results = processDataFromXlsCLIENTE(data, company)
    log.info "Headers: ${headers}"
    log.info "Results: ${results}"
    [results:results, headers:headers, data:data]
  }

  def processXlsMassiveForPROVEEDOR(def file, Company company) {
    log.info "Processing massive registration for Provider"
    File xlsFile = xlsImportService.getFileToProcess(file)
    List data = xlsImportService.parseXlsMassiveProvider(xlsFile)
    def headers = getKeyForData(data)
    List results = processDataFromXlsPROVEEDOR(data, company)
    log.info "Headers: ${headers}"
    log.info "Results: ${results}"
    [results:results, headers:headers, data:data]
  }

  def processXlsMassiveForCLIENTE_PROVEEDOR(def file, Company company) {
    log.info "Processing massive registration for Client_Provider"
    File xlsFile = xlsImportService.getFileToProcess(file)
    List data = xlsImportService.parseXlsMassiveClient_Provider(xlsFile)
    def headers = getKeyForData(data)
    List results = processDataFromXlsCLIENTE_PROVEEDOR(data, company)
    log.info "Headers: ${headers}"
    log.info "Results: ${results}"
    [results:results, headers:headers, data:data]
  }

  def getKeyForData(List data) {
    def headers = data.first().keySet()
    headers
  }


  List processDataFromXlsEMPLEADO(List data, Company company) {
    List results = []
    data.each { employee ->
      Map resultData = saveEmployeeImportData(employee, company)
      results.add(resultData.result)
      if (resultData.result == "Registrado") {
        addBusinessEntityToCompany(resultData.businessEntity, company)
      }
    }
    results
  }
 
  List processDataFromXlsCLIENTE(List data, Company company) {
    List results = []
    data.each { client ->
      Map resultData = saveClientImportData(client, company)
      results.add(resultData.result)
      if (resultData.result == "Registrado") {
        addBusinessEntityToCompany(resultData.businessEntity, company)
      }
    }
    results
  }

  List processDataFromXlsPROVEEDOR(List data, Company company) {
    List results = []
    data.each { provider ->
      Map resultData = saveProviderImportData(provider, company)
      results.add(resultData.result)
      if (resultData.result == "Registrado") {
        addBusinessEntityToCompany(resultData.businessEntity, company)
      }
    }
    results
  }

  List processDataFromXlsCLIENTE_PROVEEDOR(List data, Company company) {
    List results = []
    data.each { clientProvider ->
      Map resultData = saveClientProviderImportData(clientProvider, company)
      results.add(resultData.result)
      if (resultData.result == "Registrado") {
        addBusinessEntityToCompany(resultData.businessEntity, company)
      }
    }
    results
  }

  @Transactional
  def addBusinessEntityToCompany(Long businessEntityId, Company company) {
    BusinessEntity businessEntity = BusinessEntity.get(businessEntityId)
    log.debug "Adding business entity to company: ${businessEntity.dump()}"
    company.addToBusinessEntities(businessEntity)
    company.save()
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  def saveEmployeeImportData(Map rowEmployee, Company company) {
    if (existsBusinessEntityInCompany(rowEmployee.RFC, company)) {
      transactionStatus.setRollbackOnly()
      return [result:"Error: el RFC del empleado ya existe"]
    }

    EmployeeLink employeeLink = employeeService.createEmployeeForRowEmployee(rowEmployee, company)
    if (!employeeLink || employeeLink?.hasErrors()) {
      transactionStatus.setRollbackOnly()
      return [result:"Error: CURP"]
    }

    BusinessEntity businessEntity = createBusinessEntityForRowEmployee(rowEmployee)
    if (businessEntity.hasErrors()) {
      transactionStatus.setRollbackOnly()
      return [result:"Error: RFC"]
    }

    BankAccount bankAccount = bankAccountService.createBankAccountForBusinessEntityFromRowBusinessEntity(businessEntity, rowEmployee)
    if (!bankAccount || bankAccount?.hasErrors()) {
      transactionStatus.setRollbackOnly()
      return [result:"Error: datos bancarios"]
    }

    if (rowEmployee.IMSS == "S") {
      String error = existingErrorsInDataImssEmployee(rowEmployee, company)
      if (error) {
        transactionStatus.setRollbackOnly()
        return [result: error]
      }

      DataImssEmployee dataImssEmployee = dataImssEmployeeService.createDataImssForRowEmployee(rowEmployee, employeeLink)
      log.info "Data imss employee: ${dataImssEmployee.dump()}"
      if (!dataImssEmployee || dataImssEmployee?.hasErrors()) {
        transactionStatus.setRollbackOnly()
        return [result:"Error: datos de IMSS"]
      }
    }
    [result:"Registrado", businessEntity:businessEntity.id]
  }

  String existingErrorsInDataImssEmployee(Map rowEmployee, Company company) {
    if (!rowEmployee.NSS || !rowEmployee.FECHA_ALTA || rowEmployee.SA_BRUTO == null || rowEmployee.NETO == null || rowEmployee.PRIMA_VAC == null || rowEmployee.DIAS_AGUINALDO == null || !rowEmployee.PERIODO_PAGO || !rowEmployee.TIPO_CONTRATO || !rowEmployee.TIPO_REGIMEN || !rowEmployee.TIPO_JORNADA || !rowEmployee.DEPARTAMENTO || !rowEmployee.PUESTO) {
      return "Error: Existen campos vacíos después del campo IMSS, y se usó la opción S"
    }

    if (dataImssEmployeeService.existsNssInCompanyAlready(company, new DataImssEmployee(nss:rowEmployee.NSS))) {
      return "Error: el NSS ya está registrado en la empresa con otro empleado"
    }

    if (rowEmployee.FECHA_ALTA instanceof String) {
      try {
        Date.parse("dd-MM-yyyy", rowEmployee.FECHA_ALTA)
      } catch (Exception e) {
        return "Error: la fecha de alta no es válida"
      }
    }

    if (rowEmployee.SA_BRUTO instanceof String && !rowEmployee.SA_BRUTO.isNumber()) {
      return "Error: el valor de SA_BRUTO no es válido"
    }

    if (rowEmployee.NETO instanceof String && !rowEmployee.NETO.isNumber()) {
      return "Error: el valor de NETO no es válido"
    }

    if (new BigDecimal(rowEmployee.NETO) <= 0) {
      return "Error: el valor de NETO debe ser mayor a 0"
    }

    if (rowEmployee.PRIMA_VAC instanceof String && !rowEmployee.PRIMA_VAC.isNumber()) {
      return "Error: el valor del campo PRIMA_VAC no es un número"
    }

    if (new BigDecimal(rowEmployee.PRIMA_VAC) < 0 || new BigDecimal(rowEmployee.PRIMA_VAC) > 100) {
      return "Error: el valor del campo PRIMA_VAC debe estar entre 0 y 100"
    }

    if (rowEmployee.DIAS_AGUINALDO instanceof String && !rowEmployee.DIAS_AGUINALDO.isNumber()) {
      return "Error: el valor del campo DIAS_AGUINALDO no es un número"
    }

    if (new BigDecimal(rowEmployee.DIAS_AGUINALDO) < 15) {
      return "Error: el valor del campo DIAS_AGUINALDO no puede ser menor de 15"
    }

		if (!PaymentPeriod.find { it.toString() == rowEmployee.PERIODO_PAGO.toUpperCase() }) {
       return "Error: el valor del período de pago no es válido"
    }

    if (!ContractType.find { it.key == rowEmployee.TIPO_CONTRATO }) {
      return "Error: el valor del tipo de contrato no es válido"
    }

    if (!RegimeType.find { it.key == rowEmployee.TIPO_REGIMEN }) {
      return "Error: el valor del tipo de régimen no es válido"
    }

    if (!WorkDayType.find { it.key == rowEmployee.TIPO_JORNADA }) {
      return "Error: el valor del tipo de jornada no es válido"
    }

    ""
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  def saveClientImportData(Map rowClient, Company company) {
    if(checkIfTypeOfBusinessEntityIsCorrect(rowClient.PERSONA)) {
      transactionStatus.setRollbackOnly()
      return [result:"Error: tipo de cliente"]
    }

    if (existsBusinessEntityInCompany(rowClient.RFC, company)){
      transactionStatus.setRollbackOnly()
      return [result:"Error: el RFC del cliente ya existe"]
    }
    
    ClientLink clientLink = clientService.createClientForRowClient(rowClient, company)
    BusinessEntity businessEntity = createBusinessEntityForRowBusinessEntity(rowClient)
    if(businessEntity.hasErrors()){
        transactionStatus.setRollbackOnly()
        return [result:"Error: RFC"]
    }

    BankAccount bankAccount = bankAccountService.createBankAccountForClientFromRowClient(businessEntity, rowClient)
    if(!bankAccount || bankAccount?.hasErrors()){
      transactionStatus.setRollbackOnly()
      return [result:"Error: datos bancarios"]
    }

    Address address = addressService.createAddressForBusinessEntityFromRowBusinessEntity(businessEntity, rowClient)
    if (!address || address?.hasErrors()) {
        transactionStatus.setRollbackOnly()
        return [result:"Error: Datos en la dirección"]
    }

    [result:"Registrado", businessEntity:businessEntity.id]
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  def saveProviderImportData(Map rowProvider, Company company) {
    if(checkIfTypeOfBusinessEntityIsCorrect(rowProvider.PERSONA)) {
      transactionStatus.setRollbackOnly()
      return [result:"Error: tipo de proveedor"]
    }

    if (existsBusinessEntityInCompany(rowProvider.RFC, company)){
      transactionStatus.setRollbackOnly()
      return [result:"Error: el RFC del proveedor ya existe"]
    }

    ProviderLink providerLink = providerService.createProviderForRowProvider(rowProvider, company)
    BusinessEntity businessEntity = createBusinessEntityForRowBusinessEntity(rowProvider)
      if(businessEntity.hasErrors()){
        transactionStatus.setRollbackOnly()
        return [result:"Error: RFC"]
      }

    if (!rowProvider.CLABE.isNumber()) {
      transactionStatus.setRollbackOnly()
      return [result:"Error: La CLABE no es valor válido"]
    }

    BankAccount bankAccount = bankAccountService.createBankAccountForBusinessEntityFromRowBusinessEntity(businessEntity, rowProvider)
    if (!bankAccount || bankAccount?.hasErrors()) {
      transactionStatus.setRollbackOnly()
      return [result:"Error: datos bancarios"]
    }

    Address address = addressService.createAddressForBusinessEntityFromRowBusinessEntity(businessEntity, rowProvider)
    if (!address || address?.hasErrors()) {
        transactionStatus.setRollbackOnly()
        return [result:"Error: Datos de la dirección"]
    }

    [result:"Registrado", businessEntity:businessEntity.id]
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  def saveClientProviderImportData(Map rowClientProvider, Company company){
    if(checkIfTypeOfBusinessEntityIsCorrect(rowClientProvider.PERSONA)) {
      transactionStatus.setRollbackOnly()
      return [result:"Error: tipo de cliente/proveedor"]
    }

    if (existsBusinessEntityInCompany(rowClientProvider.RFC, company)){
      transactionStatus.setRollbackOnly()
      return [result:"Error: el RFC del cliente/proveedor ya existe"]
    }

    ProviderLink providerLink = providerService.createProviderForRowProvider(rowClientProvider, company)
    ClientLink clientLink = clientService.createClientForRowClient(rowClientProvider, company)
    BusinessEntity businessEntity = createBusinessEntityForRowBusinessEntity(rowClientProvider)
      if(businessEntity.hasErrors()){
        transactionStatus.setRollbackOnly()
        return [result:"Error: RFC"]
      }

    if (!rowClientProvider.CLABE.isNumber()) {
      transactionStatus.setRollbackOnly()
      return [result:"Error: La CLABE no es valor válido"]
    }

    BankAccount bankAccount = bankAccountService.createBankAccountForBusinessEntityFromRowBusinessEntity(businessEntity, rowClientProvider)
    if (!bankAccount || bankAccount?.hasErrors()) {
      transactionStatus.setRollbackOnly()
      return [result:"Error: datos bancarios"]
    }

    Address address = addressService.createAddressForBusinessEntityFromRowBusinessEntity(businessEntity, rowClientProvider)
    if (!address || address?.hasErrors()) {
        transactionStatus.setRollbackOnly()
        return [result:"Error: Datos de la dirección"]
    }

    [result:"Registrado", businessEntity:businessEntity.id]
  }

  def checkIfTypeOfBusinessEntityIsCorrect(String persona){
    persona.toUpperCase().replace('Í', 'I') != "FISICA" && persona.toUpperCase() != "MORAL"
  }

  def createBusinessEntityForRowEmployee(Map employeeMap) {
    BusinessEntity businessEntity = new BusinessEntity(
      rfc:employeeMap.RFC,
      type:BusinessEntityType.FISICA,
      status:BusinessEntityStatus.TO_AUTHORIZE
    )

    businessEntity.save()
    createComposeNameForBusinessEntityFromRowBusinessEntity(businessEntity, employeeMap)
  }

  def createBusinessEntityForRowBusinessEntity(Map businessMap) {
    BusinessEntity businessEntity = new BusinessEntity(
      rfc:businessMap.RFC,
      type:BusinessEntityType."${businessMap.PERSONA}",
      status:BusinessEntityStatus.TO_AUTHORIZE
      )

    businessEntity.save()
    if("${businessMap.PERSONA}" == "FISICA")
      return createComposeNameForBusinessEntityFromRowBusinessEntity(businessEntity, businessMap)
    else if("${businessMap.PERSONA}" == "MORAL")
      return appendDataToBusinessEntityFromMap(businessEntity, businessMap)
  }

  def createComposeNameForBusinessEntityFromRowBusinessEntity(BusinessEntity businessEntity, Map businessMap) {
    ComposeName lastName = new ComposeName(value:businessMap.PATERNO, type:NameType.APELLIDO_PATERNO)
    ComposeName motherLastName = new ComposeName(value:businessMap.MATERNO, type:NameType.APELLIDO_MATERNO)
    ComposeName name = new ComposeName(value:businessMap.NOMBRE, type:NameType.NOMBRE)
    businessEntity.addToNames(lastName)
    businessEntity.addToNames(motherLastName)
    businessEntity.addToNames(name)
    businessEntity.save()
    businessEntity
  }

  def appendDataToBusinessEntityFromMap(BusinessEntity businessEntity, Map businessMap) {
    businessEntity.addToNames(new ComposeName(value:businessMap.RAZON_SOCIAL, type:NameType.RAZON_SOCIAL))
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
      (be.status == BusinessEntityStatus.ACTIVE) && (EmployeeLink.findByEmployeeRefAndCompany(be.rfc, company))
    }.sort { it.id }
  }

  boolean existsBusinessEntityInCompany(String rfc, Company company) {
    company.businessEntities.find { it.rfc == rfc } ? true : false
  }

  def changeEnabledRelatedUserIfExists(BusinessEntity businessEntity, boolean status) {
    User user = userService.findByUsername(businessEntity.rfc)
    if (user) {
      user.enabled = status
      user.save()
    }
  }
}
