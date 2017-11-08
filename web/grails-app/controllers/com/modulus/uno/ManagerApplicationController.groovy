package com.modulus.uno

import java.text.SimpleDateFormat

class ManagerApplicationController {

  def companyService
  def managerApplicationService
  def documentService
  def depositOrderService
  def springSecurityService
  CorporateService corporateService

  static String ISR = "ISR"
  static String IVA = "IVA"
  static String FEE = "FEE"

  def accepted() {
    managerApplicationService.acceptingCompanyToIntegrate(params.long('companyId'))
    redirect action:"list"
  }

  def accountStatementISR() {
    Map period = getPeriodToConsultAccountStatement(params)
    AccountStatement accountStatement = managerApplicationService.getAccountStatementOfTypeInPeriod(ISR, period)
    render view:'accountStatementIntegrator', model:[accountStatement:accountStatement, accountType:ISR]
  }

  def accountStatementIVA() {
    Map period = getPeriodToConsultAccountStatement(params)
    AccountStatement accountStatement = managerApplicationService.getAccountStatementOfTypeInPeriod(IVA, period)
    render view:'accountStatementIntegrator', model:[accountStatement:accountStatement, accountType:IVA]
  }

  def accountStatementFEE() {
    Map period = getPeriodToConsultAccountStatement(params)
    AccountStatement accountStatement = managerApplicationService.getAccountStatementOfTypeInPeriod(FEE, period)
    render view:'accountStatementIntegrator', model:[accountStatement:accountStatement, accountType:FEE]
  }

  private Map getPeriodToConsultAccountStatement(def params){
    Map period = [:]
    period.beginDate = params.startDate ? new SimpleDateFormat("dd-MM-yyyy").format(params.startDate) : ""
    period.endDate = params.endDate ? new SimpleDateFormat("dd-MM-yyyy").format(params.endDate) : ""
    period
  }

  def genereDocumentOfAccount(Company company) {
    def listOfFirstAccess = FirstAccessToLegalRepresentatives.findAllByCompany(company)
    renderPdf(template: "/documentTemplates/accountActivation", model: [accountsLegalReporesentatives: listOfFirstAccess])
  }

  // TODO: Refactor, se está obteniendo todas las compañias sin paginación, se tienen que hacer conteos por consulta
  // no por tamaño de lista obtenida
  def index(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    User user = springSecurityService.currentUser
    Corporate corporate = corporateService.findCorporateOfUser(user)

    def companiesForValidation = companyService.findCompaniesByCorporateAndStatus(CompanyStatus.VALIDATE,corporate.id)
    def companiesRejected = companyService.findCompaniesByCorporateAndStatus(CompanyStatus.REJECTED,corporate.id)
    def companiesAccepted = companyService.findCompaniesByCorporateAndStatus(CompanyStatus.ACCEPTED,corporate.id)
    [
    companiesForValidation:companiesForValidation,
    companiesRejected:companiesRejected,
    companiesAccepted:companiesAccepted
    ]
  }

  def invalid() {
    [company:params.companyId]
  }

  def list() {
    def companies = companyService.allCompaniesByStatus(CompanyStatus.ACCEPTED)
    render view:"/company/index", model:[companyList:companies,companyCout:companies.size()]
  }

  def obtainCompanyByFilters() {
    def companies = companyService.listCompanyByFilters(params)
    render view:"search", model:[companies:companies]
  }

  def rejected() {
    def company = Company.findById(params.companyId)
    managerApplicationService.rejectedCompanyToIntegrate(company,params)
    redirect action:'index'
  }

  def search() {
    def companyList = Company.findAllByStatusNotEquals(CompanyStatus.CREATED)
    [companies: companyList]
  }

  def show(Company company) {
    def documentsByUser = managerApplicationService.getMapOfUsersWithDocuments(company.legalRepresentatives,company.id)
    [company:company,legalRepresentatives:documentsByUser, baseUrlDocuments:grailsApplication.config.grails.url.base.images]
  }

  def conditionsAndTerms() {
    [conditionsAndTerms:managerApplicationService.getConditionsAndTerms()]
  }

  def privacyNotice() {
    [privacyNotice:managerApplicationService.getPrivacyNotice()]
  }

  def executeFinalTransaction() {
  }

  def applyFinalTransactionForAllCompanies() {
    Date date = Date.parse("dd-MM-yyyy", params.date)
    managerApplicationService.applyFinalTransactionForAllCompaniesInDate(date)
    redirect action:"executeFinalTransaction"
  }

}
