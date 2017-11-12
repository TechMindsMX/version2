package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import java.lang.Void as Should
import grails.test.mixin.Mock
import java.text.SimpleDateFormat
import java.util.Calendar
import static java.util.Calendar.*
import spock.lang.Unroll
import spock.lang.Ignore

import com.modulus.uno.stp.StpService

@TestFor(CompanyService)
@Mock([Company,Corporate,Address,S3Asset,User,UserRole,Role,UserRoleCompany,Profile, ModulusUnoAccount, Commission, Bank, Transaction, MovimientosBancarios, BankAccount])
class CompanyServiceSpec extends Specification {

  ModulusUnoService modulusUnoService = Mock(ModulusUnoService)
  PurchaseOrderService purchaseOrderService = Mock(PurchaseOrderService)
  CashOutOrderService cashOutOrderService = Mock(CashOutOrderService)
  LoanOrderHelperService loanOrderHelperService = Mock(LoanOrderHelperService)
  FeesReceiptService feesReceiptService = Mock(FeesReceiptService)
  SaleOrderService saleOrderService = Mock(SaleOrderService)
  CollaboratorService collaboratorService = Mock(CollaboratorService)
  DirectorService directorService = Mock(DirectorService)
  RestService restService = Mock(RestService)
  TransactionService transactionService = Mock(TransactionService)
  CommissionTransactionService commissionTransactionService = Mock(CommissionTransactionService)
  StpService stpService = Mock(StpService)
  MovimientosBancariosService movimientosBancariosService = Mock(MovimientosBancariosService)

  def setup(){
    service.modulusUnoService = modulusUnoService
    service.purchaseOrderService = purchaseOrderService
    service.cashOutOrderService = cashOutOrderService
    service.loanOrderHelperService = loanOrderHelperService
    service.feesReceiptService = feesReceiptService
    service.saleOrderService = saleOrderService
    service.collaboratorService = collaboratorService
    service.directorService = directorService
    service.restService = restService
    service.transactionService = transactionService
    service.commissionTransactionService = commissionTransactionService
    service.stpService = stpService
    service.movimientosBancariosService = movimientosBancariosService
  }

  Should "create a direction for a Company"(){
    given:"a company"
      def company = new Company(rfc:"JIGE930831NZ1",
                                bussinessName:"Apple Computers",
                                webSite:"http://www.apple.com",
                                employeeNumbers:40,
                                grossAnnualBilling:4000).save(validate:false)
    and:"the address"
      def address = new Address(street:"Poniente 3",
                                streetNumber:"266",
                                suite:"S/N",
                                zipCode:"57840",
                                colony:"Reforma",
                                town:"Gustavo A.Madero",
                                city:"México",
                                country:"México",
                                federalEntity:"México",
                                company:company)
    when:
      company = service.createAddressForCompany(address, company.id)
    then:
      company.addresses.first().street == address.street
  }

  def "create a s3Asset and create relatiuon with company"() {
    given:
      def asset = new S3Asset().save(validate:false)
    and:
      def company = createCompany()
    when:
      def companyWithDocuments = service.createS3AssetForCompany(asset,company.id)
    then:
      companyWithDocuments.documents.first() instanceof S3Asset
  }

  void "find all companies with status validate"() {
    given:
      def company = createCompany()
      company.status = CompanyStatus.VALIDATE
      company.save()
    when:
      def companyResult = service.allCompaniesByStatus(CompanyStatus.VALIDATE)
    then:
      companyResult.first().status == CompanyStatus.VALIDATE
  }

  Should "get the companies of a corporate and with the validate status"(){
    given:"the corporate"
      Corporate corporate = new Corporate()
    and:"the companies"
      ArrayList<Company> companies = [new Company(status:CompanyStatus.VALIDATE),
                                      new Company(status:CompanyStatus.VALIDATE)]
      companies.each{ company ->
        corporate.addToCompanies(company)
      }

      corporate.save(validate:false)
    when:
      ArrayList<Company> corporateCompanies = service.findCompaniesByCorporateAndStatus(CompanyStatus.VALIDATE,corporate.id)
    then:
      corporateCompanies.size() == 2
  }

  void "search company by filters"() {
    given:
      createSevenCompanies()
      def params = mapDetails
    when:
      def companies = service.listCompanyByFilters(params)
    then:
      companies.size() == sizeResult
    where:
            mapDetails                                    | sizeResult
      [status:CompanyStatus.VALIDATE]                     | 2
      [rfc:"RED861224KJD"]                                | 1
      [bussinessName:"apple3"]                            | 1
      [status:CompanyStatus.ACCEPTED,rfc:"ROW861224LDD"]   | 1
  }

  def "verify that not missing autorizers for this company"() {
    given:
      def userRole = new Role(authority:'ROLE_AUTHORIZER_EJECUTOR').save(validate:false)
      def userRole1 = new Role(authority:'ROLE_LEGAL_REPRESENTATIVE_VISOR').save(validate:false)
    and:"the company"
      def company = new Company()
      company.numberOfAuthorizations = 2
      company.save(validate:false)
    and:
      ArrayList<User> users = [createUserWithRole([username:'autorizador1',
                                                   password:'autorizador1',
                                                   email:'autorizador1@email.com'], userRole, company),
                               createUserWithRole([username:'autorizador2',
                                                   password:'autorizador2',
                                                   email:'autorizador2@email.com'], userRole, company),
                               createUserWithRole([username:'representante',
                                                   password:'representante',
                                                   email:'representante@email.com'],userRole1,company)]
    and:
      directorService.findUsersOfCompanyByRole(_,_) >> [users[0],users[1]]
    when:
      def result = service.getAuthorizersByCompany(company)
    then:
      result.size() == 2
  }

  def "verify that lack an autorizer for this company"() {
    given:
      def userRole = new Role(authority:'ROLE_AUTHORIZER_EJECUTOR').save(validate:false)
      def userRole1 = new Role(authority:'ROLE_LEGAL_REPRESENTATIVE_VISOR').save(validate:false)
    and:
      Company company = new Company()
      company.numberOfAuthorizations = 2
      company.save(validate:false)
    and:
      ArrayList<User> users = [createUserWithRole([username:'autorizador1',
                                                  password:'autorizador1',
                                                  email:'autorizador1@email.com'], userRole, company),
                              createUserWithRole([username:'representante',
                                                  password:'representante',,
                                                  email:'representante@email.com'], userRole1, company)]
    and:
      directorService.findUsersOfCompanyByRole(_,_) >> [users[0]]
    when:
      def result = service.getAuthorizersByCompany(company)
    then:
      result.size() == 1
  }

  def "verify that lack autorizers for this company,when exist 2 authorizers and one legal Representavice"() {
    given:
      Role userRole = new Role(authority:'ROLE_AUTHORIZER_EJECUTOR').save(validate:false)
      Role userRole1 = new Role(authority:'ROLE_LEGAL_REPRESENTATIVE_VISOR').save(validate:false)
    and:
      Company company = new Company()
      company.numberOfAuthorizations = 1
      company.save(validate:false)
    and:
      ArrayList<User> users = [createUserWithRole([username:'autorizador1',
                                                   password:'autorizador1',
                                                   email:'autorizador1@email.com'], userRole, company),
                               createUserWithRole([username:'autorizador2',
                                                   password:'autorizador2',
                                                   email:'autorizador2@email.com'], userRole, company),
                               createUserWithRole([username:'representante',
                                                   password:'representante',
                                                   email:'representante@email.com'], userRole1, company)]
    and:
      directorService.findUsersOfCompanyByRole(_,_) >> [users[0],users[1]]
    when:
      def result = service.getAuthorizersByCompany(company)
    then:
      result.size() == 2
  }

  def "verify that lack an autorizer for this company,when only exist legal a representative"() {
    given:
      def userRole1 = new Role(authority:'ROLE_LEGAL_REPRESENTATIVE_VISOR').save(validate:false)
and:
      def company = new Company()
      company.numberOfAuthorizations = 1
      company.save(validate:false)
    and:
      User user = createUserWithRole([username:'representante',
                                      password:'representante',
                                      email:'representante@email.com'], userRole1, company)
    and:
      directorService.findUsersOfCompanyByRole(_,_) >> []
    when:
      def result = service.getAuthorizersByCompany(company)
    then:
      result.size() == 0
  }

  def "get all actors with role INTEGRADO_AUTORIZADOR for this company"() {
    given:
      def userRole = new Role(authority:'ROLE_AUTHORIZER_EJECUTOR').save(validate:false)
      def userRole1 = new Role(authority:'ROLE_LEGAL_REPRESENTATIVE_VISOR').save(validate:false)
    and:
      def company = new Company()
      company.save(validate:false)

    and:
      ArrayList<User> users = [createUserWithRole([username:'autorizador1',
                                                   password:'autorizador1',
                                                   email:'autorizador1@email.com'], userRole,company),
                               createUserWithRole([username:'autorizador2',
                                                   password:'autorizador2',
                                                   email:'autorizador2@email.com'], userRole,company),
                               createUserWithRole([username:'representante',
                                                   password:'representante',
                                                   email:'representante@email.com'], userRole1,company)]

    and:"the director service mock"
      directorService.findUsersOfCompanyByRole(_,_) >> [users[0],users[1]]
    when:
      def count = service.getAuthorizersByCompany(company)
    then:
      count.size() == 2

  }

  def "get the number of authorizers missing from is Company"() {
    given:
      def userRole = new Role(authority:'ROLE_AUTHORIZER_EJECUTOR').save(validate:false)
      def userRole1 = new Role(authority:'ROLE_LEGAL_REPRESENTATIVE_VISOR').save(validate:false)
    and:
      def company = new Company()
      company.numberOfAuthorizations = count
      company.save(validate:false)
    and:
      ArrayList<User> users = [createUserWithRole([username:'autorizador1',
                                                   password:'autorizador1',
                                                   email:'autorizador1@email.com'], userRole, company),
                               createUserWithRole([username:'autorizador2',
                                                   password:'autorizador2',
                                                   email:'autorizador2@email.com'], userRole, company),
                               createUserWithRole([username:'representante',
                                                   password:'representante',
                                                   email:'representante@email.com'], userRole1, company)]
    and:
      directorService.findUsersOfCompanyByRole(_,_) >> [users[0],users[1]]
    when:
      def numberOfAuthorizers = service.getNumberOfAuthorizersMissingForThisCompany(company)
    then:
      (numberOfAuthorizers == result) == comparation
    where:
      count || result | comparation
       3    ||  2     | false
       1    ||  0     | true
  }

  void "Should get balances of company"() {
    given:"A company"
      Company company = createCompany()
      company.status = CompanyStatus.ACCEPTED
    and:"An account"
      ModulusUnoAccount accountM1 = new ModulusUnoAccount(stpClabe:"1234567890").save(validate:false)
      company.addToAccounts(accountM1)
      BankAccount bankAccount = new BankAccount().save(validate:false)
      company.addToBanksAccounts(bankAccount)
      company.save(validate:false)
    and:
      modulusUnoService.consultBalanceOfAccount(company.accounts.first().stpClabe) >> 100
      movimientosBancariosService.getBalanceByCuentaPriorToDate(_,_) >> 150
    when:"Get Balance of company"
      Balance balances = service.getGlobalBalanceOfCompany(company)
    then:"Expect a balance and usd amount"
      balances.balance == 250
      balances.usd == 0
  }

  void "Should throw a BusinessException when account statement period is not valid"(){
    given:"A company"
      Company company = createCompany()
    and:"A not valid period"
      String beginDate = "30-04-2016"
      String endDate = "01-03-2016"
    when:"get the account statement"
      service.getAccountStatementOfCompany(company, beginDate, endDate)
    then:"expect a business exception"
      thrown BusinessException
  }

  void "Should get the account statement of a company"(){
    given:"A company"
      Company company = createCompany()
    and:"An account"
      ModulusUnoAccount accountM1 = new ModulusUnoAccount(stpClabe:"1234567890").save(validate:false)
      company.addToAccounts(accountM1)
      BankAccount bankAccount = new BankAccount(banco:new Bank(name:"BANCO")).save(validate:false)
      company.addToBanksAccounts(bankAccount)
      company.save(validate:false)
    and:"A valid period"
      String beginDate = "01-04-2016"
      String endDate = "30-04-2016"
    and:
      Bank.metaClass.static.findByName = { new Bank(name:"STP") }
    and:
      transactionService.getTransactionsAccountForPeriod(_,_) >> []
      transactionService.getBalanceByKeyAccountPriorToDate(_,_) >> new BigDecimal(0)
      movimientosBancariosService.getBalanceByCuentaPriorToDate(_,_) >> new BigDecimal(0)
    when:"get the account statement"
      AccountStatement accountStatement = service.getAccountStatementOfCompany(company, beginDate, endDate)
    then:
      accountStatement.balance.balance == 0
      1 * transactionService.getTransactionsAccountForPeriod(_,_)
      1 * commissionTransactionService.getCommissionsBalanceInPeriodForCompanyAndStatus(_, _, _)
  }

  @Unroll
  void "Should get #expectResult when a company has total balance #totalBalance to solve amount #amount"() {
    given:"A company"
      Company company = createCompany()
    and:"An account"
      ModulusUnoAccount account = new ModulusUnoAccount()
      account.stpClabe   = "1234567890"
      account.save(validate:false)
      company.accounts = [account]
      company.status = CompanyStatus.ACCEPTED
      company.save(validate:false)
    and:
      modulusUnoService.consultBalanceOfAccount(company.accounts.first().stpClabe) >> totalBalance
    when:"we check balance to solve amount"
      Boolean result = service.enoughBalanceCompany(company, amount)
    then:
      result == expectResult
    where:
      amount  | totalBalance  | expectResult
      500     | 1000          | true
      1000    | 1000          | true
      1001    | 1000          | false
      1001    | 500           | false
      1001    | 1000          | false
      1001    | 1001          | true
  }

  void "create simple company and add it to one corporate"(){
    given:
      Company company = new Company(rfc:"ROD861224KJD",
                                    bussinessName:"apple1",
                                    webSite:"http://url.com",
                                    employeeNumbers:2,
                                    grossAnnualBilling:1_000_000)
      Corporate corporate = new Corporate(nameCorporate:"nombre",corporateUrl:"url")
      corporate.save(validate:false)
      User corporateUser = new User().save(validate:false)
    and:
      def corporateServiceMock = Mock(CorporateService) {
        1 * addCompanyToCorporate(corporate,company) >> {
          corporate.addToCompanies(company)
          corporate.save()
          company
        }
      }
      service.corporateService = corporateServiceMock
    when:
      Company expectedCompany = service.saveInsideAndAssingCorporate(company,corporate.id)
    then:
      expectedCompany.id
  }

  private def createUserWithRole(Map userInfo,Role userRole,Company company) {
    def user = User.findByUsername(userInfo.username) ?: new User(username:userInfo.username,
                                                                  password:userInfo.password,
                                                                  enabled:true,
                                                                  profile:new Profile(name:userInfo.username,
                                                                                      lastName:'lastName',
                                                                                      motherLastName:'motherLastName',
                                                                                      email:userInfo.email)).save(validate:false)

    if(!UserRole.get(user.id,userRole.id))
      UserRole.create user, userRole

    UserRoleCompany userRoleCompany = new UserRoleCompany(user:user,
                                                          company:company)
    userRoleCompany.addToRoles(userRole)
    userRoleCompany.save()

    user
  }

  private createSevenCompanies() {
    new Company(rfc:"ROD861224KJD",bussinessName:"apple1",status:CompanyStatus.CREATED).save(validate:false)
    new Company(rfc:"RED861224KJD",bussinessName:"apple2",status:CompanyStatus.VALIDATE).save(validate:false)
    new Company(rfc:"ROW861224LDD",bussinessName:"apple3",status:CompanyStatus.ACCEPTED).save(validate:false)
    new Company(rfc:"ROQ861224AJD",bussinessName:"apple4",status:CompanyStatus.REJECTED).save(validate:false)
    new Company(rfc:"ROV861224HJD",bussinessName:"apple5",status:CompanyStatus.CREATED).save(validate:false)
    new Company(rfc:"RIY861224GJD",bussinessName:"apple6",status:CompanyStatus.ACCEPTED).save(validate:false)
    new Company(rfc:"RCS861224FJD",bussinessName:"apple7",status:CompanyStatus.VALIDATE).save(validate:false)
  }

  private Company createCompany() {
    def company = new Company(rfc:"JIG930831NZ1",
                                bussinessName:"Apple Computers",
                                webSite:"http://www.apple.com",
                                employeeNumbers:40,
                                grossAnnualBilling:4000)
    def address = new Address(street:"Poniente 3",
                                streetNumber:"266",
                                suite:"S/N",
                                zipCode:"57840",
                                colony:"Reforma",
                                town:"Gustavo A.Madero",
                                city:"México",
                                country:"México",
                                federalEntity:"México",
                                addressType:AddressType.FISCAL,
                                company:company)

    company.addToAddresses(address)
    company.save()
    company
  }

  void "Should return is not enabled to stamp when company is missing documents to stamp"() {
    given: "A company"
      Company company = createCompany()
    and: "documents to stamp"
      restService.existEmisorForGenerateInvoice(_) >> [validCer:true, validKey:false]
    when: "we verify status"
      Boolean result = service.isCompanyEnabledToStamp(company)
    then:
      result == false
  }

  void "Should return is not enabled to stamp when company is missing fiscal address"() {
    given: "A company"
      Company company = new Company(rfc:"XYZ010203ABC").save(validate:false)
    and: "documents to stamp"
      restService.existEmisorForGenerateInvoice(_) >> [status:true]
    when: "we verify status"
      Boolean result = service.isCompanyEnabledToStamp(company)
    then:
      result == false
  }

  void "Should assign alias stp to company account m1"() {
    given:"A company"
      Company company = createCompany()
    and:"An account"
      ModulusUnoAccount account = new ModulusUnoAccount()
      account.timoneUuid   = "1234567890"
      account.save(validate:false)
      company.accounts = [account]
      company.status = CompanyStatus.ACCEPTED
      company.save(validate:false)
    and:"An alias"
      String alias = "AliasStp"
    when:
      service.assignAliasStpToCompany(company, alias)
    then:
      account.aliasStp
  }

  @Unroll
  void "Should return #expected when company has alias stp #aliasStp and type commission #typeCommission"() {
    given:"A company without data needed for pay"
      Company company = new Company(rfc:"AAA010101AAA").save(validate:false)
    and:"A modulus uno account"
      ModulusUnoAccount account = new ModulusUnoAccount(stpClabe:"646180191900100010", aliasStp:aliasStp).save(validate:false)
      company.addToAccounts(account)
      company.save(validate:false)
    and:"Commission for pay"
      Commission commission = new Commission(type:typeCommission, fee:new BigDecimal(10)).save(validate:false)
      company.addToCommissions(commission)
      company.save(validate:false)
    when:
      Boolean result = service.companyIsEnabledToPay(company)
    then:
      result == expected
    where:
    aliasStp         | typeCommission          || expected
    null             | CommissionType.FACTURA   || false
    "alias"          | CommissionType.FACTURA   || false
    ""               | CommissionType.PAGO      || false
    "alias"          | CommissionType.PAGO      || true
  }

  void "Should apply final transfer for company and obtain status OK"() {
    given:"A company"
      Company company = new Company(rfc:"AAA010101AAA").save(validate:false)
      ModulusUnoAccount account = new ModulusUnoAccount(stpClabe:"646180191900100010", aliasStp:"aliasStp").save(validate:false)
      company.addToAccounts(account)
      company.save(validate:false)
    and:"The period"
      Period period = new Period(
        init:new Date().parse("dd-MM-yyyy HH:mm:ss", "13-05-2017 00:00:00"),
        end:new Date().parse("dd-MM-yyyy HH:mm:ss", "13-05-2017 17:35:00")
        )
      collaboratorService.getPeriodForConciliationStp() >> period
    and:"The transactions from stp"
      String dateTransaction = new SimpleDateFormat("yyyyMMdd").format(new Date().parse("dd-MM-yyyy", "13-05-2017"))
      List listMovs = [
      [id:"idmov1", credit:new BigDecimal(100), debit:new BigDecimal(0), clabe:"646180191900100010", bankCode:"072", settlementDate:new Date(), bankName:"BANORTE", tracing:"tracingCredit", reference:"referenceCredit"],
      [id:"idmov2", credit:new BigDecimal(0), debit:new BigDecimal(200), clabe:"646180191900100010", bankCode:"072", settlementDate:new Date(), bankName:"BANORTE", tracing:"tracingDebit", reference:"referenceDebit"],
      [id:"idmov3", credit:new BigDecimal(0), debit:new BigDecimal(200), clabe:"646180191900100010", bankCode:"072", settlementDate:new Date(), bankName:"BANORTE", tracing:"${dateTransaction}aliasStpidmov3", reference:"referenceFinal"]
      ]
      Map transactions = [balance:[:], transactions:listMovs, period:period]
      stpService.getTransactionsForCompanyInPeriod(_, _) >> transactions
    when:
      def response = service.executeOperationsCloseForCompany(company)
    then:
      response == "OK"
      1 * transactionService.createFinalTransferTransaction(_)
  }

  @Unroll
  void "Should obtain status NOT FOUND when don't exists the final transfer transactions from stp"() {
    given:"A company"
      Company company = new Company(rfc:"AAA010101AAA").save(validate:false)
      ModulusUnoAccount account = new ModulusUnoAccount(stpClabe:"646180191900100010", aliasStp:"aliasStp").save(validate:false)
      company.addToAccounts(account)
      company.save(validate:false)
    and:"The period"
      Period period = new Period(
        init:new Date().parse("dd-MM-yyyy HH:mm:ss", "13-05-2017 00:00:00"),
        end:new Date().parse("dd-MM-yyyy HH:mm:ss", "13-05-2017 17:35:00")
        )
      collaboratorService.getPeriodForConciliationStp() >> period
    and:"The transactions from stp"
      String dateTransaction = new SimpleDateFormat("yyyyMMdd").format(new Date().parse("dd-MM-yyyy", "13-05-2017"))
      List listMovs = listTransactions
      Map transactions = [balance:[:], transactions:listMovs, period:period]
      stpService.getTransactionsForCompanyInPeriod(_, _) >> transactions
    when:
      def response = service.executeOperationsCloseForCompany(company)
    then:
      response == expectedResponse
    where:
      listTransactions || expectedResponse
      [[id:"idmov1", credit:new BigDecimal(100), debit:new BigDecimal(0), clabe:"646180191900100010", bankCode:"072", settlementDate:new Date(), bankName:"BANORTE", tracing:"tracingCredit", reference:"referenceCredit"], [id:"idmov2", credit:new BigDecimal(0), debit:new BigDecimal(200), clabe:"646180191900100010", bankCode:"072", settlementDate:new Date(), bankName:"BANORTE", tracing:"tracingDebit", reference:"referenceDebit"], [id:"idmov3", credit:new BigDecimal(0), debit:new BigDecimal(200), clabe:"646180191900100010", bankCode:"072", settlementDate:new Date(), bankName:"BANORTE", tracing:"anotherTracing", reference:"referenceFinal"]] || "NOT FOUND"
      [] || "NOT FOUND"
      null || "NOT FOUND"
  }

  void "Should parse stp transactions to account statement transactions"() {
    given:"The stp transactions"
      List<Transaction> stpTransactions = [
        new Transaction(keyAccount:"ClabeStp", dateCreated:new Date(), paymentConcept:"Concepto", keyTransaction:"Clave Trans", amount:new BigDecimal(100), transactionType:TransactionType.DEPOSIT, balance:new BigDecimal(100))
      ]
    and:
      Bank.metaClass.static.findByName = { new Bank(name:"STP") }
    when:
      List<AccountStatementTransaction> asTransactions = service.parseStpTransactionsToAccountStatementTransactions(stpTransactions)
    then:
      asTransactions.size() == stpTransactions.size()
      asTransactions.first().amount == stpTransactions.first().amount
      asTransactions.first().account.clabe == stpTransactions.first().keyAccount
      asTransactions.first().account.banco.name == "STP"
  }

  void "Should parse bank transactions to account statement transactions"() {
    given:"The bank transactions"
      List<MovimientosBancarios> bankTransactions = [
        new MovimientosBancarios(cuenta:new BankAccount(banco:new Bank(name:"BANAMEX", bankingCode:"002"), clabe:"bankClabe").save(validate:false), dateEvent:new Date(), concept:"Concepto", reference:"Clave Trans", amount:new BigDecimal(100), type:MovimientoBancarioType.DEBITO)
      ]
    when:
      List<AccountStatementTransaction> asTransactions = service.parseBankTransactionsToAccountStatementTransactions(bankTransactions)
    then:
      asTransactions.size() == bankTransactions.size()
      asTransactions.first().amount == bankTransactions.first().amount
      asTransactions.first().account.clabe == bankTransactions.first().cuenta.clabe
      asTransactions.first().account.banco.name == bankTransactions.first().cuenta.banco.name
  }

  void "Should recalculate balances for account statement transactions"() {
    given:"The account statement transactions"
      List<AccountStatementTransaction> asTransactions = [
        new AccountStatementTransaction(amount:new BigDecimal(500), type:TransactionType.DEPOSIT, date:new Date()-1),
        new AccountStatementTransaction(amount:new BigDecimal(500), type:TransactionType.DEPOSIT, date:new Date()-2),
        new AccountStatementTransaction(amount:new BigDecimal(100), type:TransactionType.WITHDRAW, date:new Date()-3)
      ]
    and:"The before global balance"
      BigDecimal beforeGlobalBalance = new BigDecimal(3000)
    when:
      List<AccountStatementTransaction> recalculate = service.recalculateBalancesForTransactions(beforeGlobalBalance, asTransactions)
    then:
      recalculate.first().balance == new BigDecimal(2900)
      recalculate[1].balance == new BigDecimal(3400)
      recalculate.last().balance == new BigDecimal(3900)
  }

  void "Should get global balance prior to date for a company"() {
    given:"A company"
      Company company = new Company().save(validate:false)
      BankAccount bankAccount = new BankAccount().save(validate:false)
      company.addToBanksAccounts(bankAccount)
      ModulusUnoAccount accountM1 = new ModulusUnoAccount(stpClabe:"ClabeSTP").save(validate:false)
      company.addToAccounts(accountM1)
      company.save(validate:false)
    and:"The date"
      Date date = new Date().parse("dd-MM-yyyy","01-05-2017")
    and:
      transactionService.getBalanceByKeyAccountPriorToDate(_,_) >> new BigDecimal(1200)
      movimientosBancariosService.getBalanceByCuentaPriorToDate(_,_) >> new BigDecimal(800)
    when:
      def result = service.getGlobalBalanceForCompanyPriorToDate(company, date)
    then:
      result == new BigDecimal(2000)
  }

}
