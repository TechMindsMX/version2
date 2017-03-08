package com.modulus.uno

import grails.transaction.Transactional
import java.text.SimpleDateFormat

@Transactional
class ManagerApplicationService {

  def restService
  def grailsApplication
  def documentService
  def modulusUnoService
  def collaboratorService
  DirectorService directorService

  def acceptingCompanyToIntegrate(Long companyId, String email) {
    Company company = Company.findById(companyId)
    ModulusUnoAccount account = modulusUnoService.generedModulusUnoAccountByCompany(company, email)
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
    accountStatement.startDate = new SimpleDateFormat("dd-MM-yyyy").parse(period.beginDate)
    accountStatement.endDate = new SimpleDateFormat("dd-MM-yyyy").parse(period.endDate)
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

  private def createNewPasswordForLegalRepresentatives(Company company) {
    ArrayList<User> legalRepresentatives = directorService.findUsersOfCompanyByRole(company.id,['ROLE_LEGAL_REPRESENTATIVE'])

    def bussinesName = company.bussinessName
    legalRepresentatives.each{ user ->
      def rfcUser =  user.profile.rfc
      def newPassword = "${bussinesName}${rfcUser}".replaceAll("\\s","")
      createAccessToLegalRepresentative(company,user)
      user.password = newPassword
      user.accountLocked = true
      user.save()
    }

  }

  private def createAccessToLegalRepresentative(company, user) {
    def token = UUID.randomUUID().toString().replaceAll('-','')
    def baseUrl = grailsApplication.config.first.access.register
    def firstAccess = new FirstAccessToLegalRepresentatives()
    firstAccess.urlVerification = "${baseUrl}${token}"
    firstAccess.user = user
    firstAccess.company = company
    firstAccess.token = token
    firstAccess.enabled = true
    firstAccess.save()
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

}
