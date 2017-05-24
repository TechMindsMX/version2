package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock
import spock.lang.Unroll
import java.lang.Void as Should

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ManagerApplicationService)
@Mock([Company,User,Role,UserRoleCompany,Profile,CompanyRejectedLog,FirstAccessToLegalRepresentatives, Commission])
class ManagerApplicationServiceSpec extends Specification {

  def modulusUnoService = Mock(ModulusUnoService)
  def collaboratorService = Mock(CollaboratorService)
  CompanyService companyService = Mock(CompanyService)
  CommissionTransactionService commissionTransactionService = Mock(CommissionTransactionService)

  def setup() {
    service.modulusUnoService = modulusUnoService
    service.collaboratorService = collaboratorService
    service.companyService = companyService
    service.commissionTransactionService = commissionTransactionService
  }

  Should "accept a company to integrate"(){
    given:"the company"
      Company company = new Company(name:"apple").save(validate:false)
      company.save(validate:false)
      Profile profile = new Profile(email:"sergio@makingdevs.con").save(validate:false)
      User user = new User(profile:profile).save(validate:false)
    and:"the role"
      Role role = new Role(authority:"ROLE_CORPORATIVE")
      role.save(validate:false)
    and:"the company's user"
      UserRoleCompany userRoleCompany = new UserRoleCompany(user:user,
                                                            company:company)
      userRoleCompany.addToRoles(role)
      userRoleCompany.save()
    and:
      grailsApplication.config.emailer.notificationIntegrated = "template1"
    when:
      def companyResult = service.acceptingCompanyToIntegrate(company.id, "email")
    then:
      companyResult.status == CompanyStatus.ACCEPTED
  }

  void "create companyRejected for Documents"() {
    given:
      Map params = [:]
      params.put("documents","1,2")
      params.put("legalRepresentatives","4")
      params.put("document-1","por mis ganas")
      params.put("document-2","")
    and:
      Company company = new Company().save(validate:false)
    when:
      service.createCompanyRejectedLogsForDocuments(company,params)
    then:
      def companyRejected = CompanyRejectedLog.getAll()
      companyRejected.size() == 1
      companyRejected.first().companyId == company.id
      companyRejected.first().reason == "por mis ganas"
      companyRejected.first().status == true
  }

  void "find all company rejected by companyId and status"() {
    given:
      def companyRejected1 = new CompanyRejectedLog()
      companyRejected1.companyId = 1
      companyRejected1.typeClass = "document"
      companyRejected1.reason = "por que YOLO"
      companyRejected1.status = true
      companyRejected1.assetId = 1
      companyRejected1.save()
    and:
      def companyRejected2 = new CompanyRejectedLog()
      companyRejected2.companyId = 1
      companyRejected2.typeClass = "document"
      companyRejected2.reason = "esto es verdad"
      companyRejected2.status = false
      companyRejected2.assetId = 3
      companyRejected2.save()
    when:
      def listCompanyRejected = service.findCompanyRejectedLogsByStatus(1,true)
    then:
      def companyRejected = CompanyRejectedLog.getAll()
      companyRejected.size() == 2
      listCompanyRejected.size() == 1
      listCompanyRejected.first().id == 1
      listCompanyRejected.first().reason == "por que YOLO"
  }

  void "change status to companyRejectedLog"() {
    given:
      def companyRejected1 = new CompanyRejectedLog()
      companyRejected1.companyId = 1
      companyRejected1.typeClass = "document"
      companyRejected1.reason = "por que YOLO"
      companyRejected1.status = true
      companyRejected1.assetId = 1
      companyRejected1.save()
    and:
      def companyRejected2 = new CompanyRejectedLog()
      companyRejected2.companyId = 1
      companyRejected2.typeClass = "document"
      companyRejected2.reason = "esto es verdad"
      companyRejected2.status = true
      companyRejected1.assetId = 2
      companyRejected2.save()
    and:
      def companyRejected = CompanyRejectedLog.getAll()
    when:
      service.changeStatusInCompanyRejectrLogRows(companyRejected)
    then:
      def companyRejectedResult = CompanyRejectedLog.getAll()
      companyRejectedResult*.status.contains(true) == false
  }

  void "create a company rejectd log for legal representatives"() {
    given:
      Map params = [:]
      params.put("legalRepresentatives","1,2")
      params.put("legalRepresentativeDocuments-1","1")
      params.put("legalRepresentativeDocuments-2","4,5,6")
      params.put("legalRepresentative-1","")
      params.put("legalRepresentative-4","")
      params.put("legalRepresentative-5","tu mama")
      params.put("legalRepresentative-6","me quiere")
    and:
      def company = new Company().save(validate:false)
    when:
      def listCompanyRejected = service.createCompanyRejectedLogsForLegalRepresentatives(company,params)
    then:
      def companyRejected = CompanyRejectedLog.getAll()
      companyRejected.size() == 2
      companyRejected.first().companyId == company.id
      companyRejected.first().reason == "tu mama"
      companyRejected.first().status == true
  }

  void "Should thrown a BusinessException for period not valid when get account statement any type of integrator"() {
    given:"A not valid period"
      Map period = [:]
      period.beginDate = "30-04-2016"
      period.endDate = "15-04-2016"
      collaboratorService.periodIsValid(period.beginDate, period.endDate) >> false
      String type = "IVA"
    when:"get account statement of integrator"
      service.validatePeriod(period)
    then:"expect business exception"
      thrown BusinessException
  }

  @Unroll
  void "Should get the account statement type #type of integrator"(){
    given:"A valid period"
      Map period = [:]
      period.beginDate = "01-04-2016"
      period.endDate = "15-04-2016"
      collaboratorService.periodIsValid(period.beginDate, period.endDate) >> true
      modulusUnoService.consultBalanceIntegratorOfType(_ as String) >> [100, 0]
    when:"get the account statement"
      AccountStatement accountStatement = service.getAccountStatementOfTypeInPeriod(type, period)
    then:
      calls * modulusUnoService.getTransactionsInPeriodOfIntegrator(_ as AccountStatementCommand)
    where:
      type    | calls
      "ISR"   | 1
      "IVA"   | 1
      "FEE"   | 1
  }

  void "Should apply Fixed commission For All Companies"() {
    given:"The fixed commission"
      Commission commission1 = new Commission(fee:new BigDecimal(1000), type:CommissionType.FIJA).save(validate:false)
      Commission commission2 = new Commission(fee:new BigDecimal(1000), type:CommissionType.FIJA).save(validate:false)
    and:"The companies"
      Company one = new Company().save(validate:false)
      one.addToCommissions(commission1)
      one.save(validate:false)
      Company two = new Company().save(validate:false)
      two.addToCommissions(commission2)
      two.save(validate:false)
      List<Company> companies = [one, two]
    and:""
      companyService.getAllCompaniesAcceptedWithFixedCommissionDefined() >> companies
    when:
      String result = service.applyFixedCommissionForAllCompanies()
    then:
      result == "OK"
  }

}
