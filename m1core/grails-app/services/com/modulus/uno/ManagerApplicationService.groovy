package com.modulus.uno

import grails.transaction.Transactional
import java.text.SimpleDateFormat

@Transactional
class ManagerApplicationService {

  def documentService
  def modulusUnoService
  def collaboratorService
  CompanyService companyService
  CommissionTransactionService commissionTransactionService
  def grailsApplication

  def acceptingCompanyToIntegrate(Long companyId, String email) {
    Company company = Company.findById(companyId)
    ModulusUnoAccount account = modulusUnoService.generedModulusUnoAccountByCompany(company)
    company.status = CompanyStatus.ACCEPTED
    company.save()
    company
  }

  AccountStatement getAccountStatementOfTypeInPeriod(String type, Map period){
    validatePeriod(period)
    AccountStatementCommand command = new AccountStatementCommand(type:type, begin:period.beginDate, end:period.endDate)
    AccountStatement accountStatement = new AccountStatement()
    accountStatement.balance = getBalanceIntegratorOfType(type)
    accountStatement.balanceTransiting = 0
    accountStatement.balanceSubjectToCollection = 0
    accountStatement.period = new Period(init:new SimpleDateFormat("dd-MM-yyyy").parse(period.beginDate), end:new SimpleDateFormat("dd-MM-yyyy").parse(period.endDate))
    accountStatement.transactions = modulusUnoService.getTransactionsInPeriodOfIntegrator(command)
    accountStatement
  }

  Balance getBalanceIntegratorOfType(String type) {
    BigDecimal balance = 0
    BigDecimal usd = 0
    (balance, usd) = modulusUnoService.consultBalanceIntegratorOfType("INTEGRADORA_${type}")
    new Balance(balance:balance, usd:usd)
  }

  def getMapOfUsersWithDocuments(def listOfLegalRepresentatives,def companyId) {
    def mapUserWithDocuments =  [:]
    listOfLegalRepresentatives.each { user ->
      def documents = user.profile.documents
      def documentsOfUserByCompany = documentService.getDocumentsByCompanyForLegalRepresentative(documents,companyId)
      mapUserWithDocuments.put(user,documentsOfUserByCompany)
    }
    mapUserWithDocuments
  }

  def obtainReasonOfRejectedCompanyRequestByStatus(Company company,Boolean status) {
    def rowOfReasonsOfRejected = CompanyRejectedLog.withCriteria {
      eq "companyId", company.id
      eq "status", status
    }
    rowOfReasonsOfRejected
  }

  def rejectedCompanyToIntegrate(Company company, Map params) {
    def listOfRowsRejected = findCompanyRejectedLogsByStatus(company.id,true)
    changeStatusInCompanyRejectrLogRows(listOfRowsRejected)
    createCompanyRejectedLogsForDocuments(company,params)
    createCompanyRejectedLogsForLegalRepresentatives(company,params)
    createCompanyRejectedLogsForOtherReason(company,params.reason)
    company.status = CompanyStatus.REJECTED
    company.save()
    company
  }

  //TODO:ajustar falta de ortografia
  private def changeStatusInCompanyRejectrLogRows(listOfRows) {
    listOfRows.each { companyRejected ->
      companyRejected.status = false
      companyRejected.save()
    }
  }

  //TODO:realizar refactor de la cracion de log
  private def createCompanyRejectedLogsForLegalRepresentatives(Company company,def params) {
    def idsLegalRepresentatives = params.legalRepresentatives.tokenize(",")
    idsLegalRepresentatives.each{ idUser ->
      def idsDocumentsLegalRepresentative = params."legalRepresentativeDocuments-${idUser}".tokenize(",")
      idsDocumentsLegalRepresentative.each{ idDoc ->
        if (params?."legalRepresentative-${idDoc}") {
          def companyRejectedLog = new CompanyRejectedLog()
          companyRejectedLog.companyId = company.id
          companyRejectedLog.reason = params?."legalRepresentative-${idDoc}"
          companyRejectedLog.typeClass = "legalRepresentative"
          companyRejectedLog.status = true
          companyRejectedLog.assetId = idDoc.toLong()
          companyRejectedLog.save()
        }
      }
    }
  }

  private def createCompanyRejectedLogsForDocuments(Company company, def params) {
    def listOfIdDocuments = params.documents.tokenize(",")
    listOfIdDocuments.each{ id ->
      if (params?."document-${id}") {
        def companyRejectedLog = new CompanyRejectedLog()
        companyRejectedLog.companyId = company.id
        companyRejectedLog.reason = params?."document-${id}"
        companyRejectedLog.typeClass = "document"
        companyRejectedLog.status = true
        companyRejectedLog.assetId = id.toLong()
        companyRejectedLog.save()
      }
    }
  }

  private def createCompanyRejectedLogsForOtherReason(Company company,String reason) {
    if(reason) {
      def companyRejectedLog = new CompanyRejectedLog()
      companyRejectedLog.companyId = company.id
      companyRejectedLog.reason = reason
      companyRejectedLog.typeClass = "company"
      companyRejectedLog.status = true
      companyRejectedLog.save()
    }
  }

  private def findCompanyRejectedLogsByStatus(companyId,status) {
    CompanyRejectedLog.withCriteria {
      eq 'companyId',companyId
      eq 'status',status
    }
  }

  private void validatePeriod(Map period){
    if (!period.beginDate && !period.endDate){
      period.beginDate = collaboratorService.getBeginDateCurrentMonth()
      period.endDate = collaboratorService.getEndDateCurrentMonth()
    } else if (!collaboratorService.periodIsValid(period.beginDate, period.endDate))
      throw new BusinessException ("La fecha inicial debe ser anterior o igual a la fecha final")
  }

  String applyFinalTransferForAllCompanies() {
    String result = "OK"
    List<Company> companies = companyService.getAllCompaniesAcceptedAndWithAliasStp()
    companies.each { company ->
      companyService.executeOperationsCloseForCompany(company)
    }
    result
  }

  String applyFixedCommissionForAllCompanies() {
    log.info "Executing job for apply fixed commission to companies"
    String result = "OK"
    List<Company> companies = companyService.getAllCompaniesAcceptedWithFixedCommissionDefined()
    companies.each { company ->
      if (!commissionTransactionService.companyHasFixedCommissionAppliedInCurrentMonth(company)) {
        commissionTransactionService.applyFixedCommissionToCompany(company)
      }
    }
    result
  }

  String getConditionsAndTerms() {
    File conditions = new File(grailsApplication.config.conditionsAndTerms)
    String textConditions = "Términos y Condiciones"
    if (conditions.exists())
      textConditions = conditions.text
    textConditions
  }

  String getPrivacyNotice() {
    File privacy = new File(grailsApplication.config.privacyNotice)
    String textPrivacy = "Aviso de Privacidad"
    if (privacy.exists())
      textPrivacy = privacy.text
    textPrivacy
  }

  String conciliateStpTransactionsForAllCompanies(Date date) {
    String result = "OK"
    List<Company> companies = companyService.getAllCompaniesAcceptedAndWithAliasStp()
    companies.each { company ->
      companyService.conciliateStpTransactionsForCompany(company)
    }
    result
  }

}
