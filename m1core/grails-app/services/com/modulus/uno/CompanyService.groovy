package com.modulus.uno

import grails.transaction.Transactional
import java.text.SimpleDateFormat
import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.springframework.transaction.annotation.Propagation

import com.modulus.uno.stp.StpService
import com.modulus.uno.stp.FinalTransactionResultService
import com.modulus.uno.stp.FinalTransactionResultStatus

@Transactional
class CompanyService {

  def purchaseOrderService
  def cashOutOrderService
  def saleOrderService
  def modulusUnoService
  def loanOrderHelperService
  def feesReceiptService
  def collaboratorService
  def messageSource
  def restService
  def corporateService
  DirectorService directorService
  TransactionService transactionService
  def invoiceService
  CommissionTransactionService commissionTransactionService
  StpService stpService
  MovimientosBancariosService movimientosBancariosService
  FinalTransactionResultService finalTransactionResultService

  def addingActorToCompany(Company company, User user) {
    company.addToActors(user)
    company.save()
  }

  def addingLegalRepresentativeToCompany(Company company, User user){
    company.addToLegalRepresentatives(user)
    company.save()
  }

  def allCompaniesByUser(User user){
    Company.createCriteria().list {
      actors {
        eq 'username', user.username
      }
    }
  }

  // TODO: Se tienen que aceptar los parámetros de limit y offset para que se ejecute la consulta paginada
  def allCompaniesByStatus(status) {
    Company.createCriteria().list {
     eq 'status', status
    }
  }

  def findCompaniesByCorporateAndStatus(CompanyStatus status,Long corporateId){
    Corporate corporate = Corporate.get(corporateId)
    ArrayList<Company> companies = corporate.companies.findAll{ it.status == status }
    companies
  }

  def createAddressForCompany(Address address, Long companyId){
    def company = Company.get(companyId)
    company.addToAddresses(address)
    company.save()
    company
  }

  def createLegalRepresentativeOfCompanyPhysicalRegime(Company company){
    def tks = company.bussinessName.tokenize(' ')
    String username = tks[0]
    if (tks.size()>1)
      username += "_"+tks[1]
    String mail = tks[0]+"@"+tks[0]+"mail.com"
    Profile profile = new Profile(name:tks[0], lastName:tks[1] ?: "SA", motherLastName:tks[2] ?: "SA", email:mail, rfc: company.rfc)

    User user = new User(username:username, password:"Temporal12345", enabled:false, accountExpired:false, accountLocked:false, passwordExpired:false, profile:profile)

    user.save flush:true

    company.addToLegalRepresentatives(user)
    company
  }

  def createS3AssetForCompany(S3Asset asset, def companyId) {
    def company = Company.findById(companyId)
    company.addToDocuments(asset)
    company.save()
    company
  }

  Boolean enoughBalanceCompany(Company company, BigDecimal amount) {
    BigDecimal balance = modulusUnoService.consultBalanceOfAccount(company.accounts.first().stpClabe)
    balance >= amount
  }

  AccountStatement getAccountStatementOfCompany(Company company, String beginDate, String endDate){
    Period period = new Period()
    if (!beginDate && !endDate) {
      period = collaboratorService.getCurrentMonthPeriod()
    } else {
      period.init = new Date().parse("dd-MM-yyyy", beginDate)
      period.end = new Date().parse("dd-MM-yyyy", endDate)
      if (!period.validate()) throw new BusinessException("El período definido no es válido")
    }

    AccountStatement accountStatement = new AccountStatement()
    accountStatement.company = company
    accountStatement.balance = getGlobalBalanceOfCompany(company)
    accountStatement.period = period
    accountStatement.transactions = obtainTransactionsForCompanyInPeriod(company, period)
    accountStatement.commissionsBalance = commissionTransactionService.getCommissionsBalanceInPeriodForCompanyAndStatus(company, CommissionTransactionStatus.PENDING, period)
    accountStatement.balanceSummary = obtainBalanceSummaryForCompany(company)
    accountStatement
  }

  List<Map> obtainBalanceSummaryForCompany(Company company) {
    Map stpBalance = [account:company.accounts.first().stpClabe, bank:"STP", balance:transactionService.getBalanceByKeyAccountPriorToDate(company.accounts.first().stpClabe, new Date())]
    List<Map> summary = [stpBalance]
    company.banksAccounts.sort{it.banco.name}.each { bankAccount ->
      Map bankBalance = [account:bankAccount.clabe, bank:bankAccount.banco.name, balance:movimientosBancariosService.getBalanceByCuentaPriorToDate(bankAccount, new Date())]
      summary.add(bankBalance)
    }
    summary
  }

  List<AccountStatementTransaction> obtainTransactionsForCompanyInPeriod(Company company, Period period) {
    List<AccountStatementTransaction> asTransactions = []
    asTransactions = obtainStpTransactions(company, period)
    List<AccountStatementTransaction> asTransactionsBankAccounts = obtainBankAccountsTransactions(company, period)
    asTransactions.addAll(asTransactionsBankAccounts)
    BigDecimal beforeGlobalBalance = getGlobalBalanceForCompanyPriorToDate(company, period.init)
    recalculateBalancesForTransactions(beforeGlobalBalance, asTransactions)
  }

  BigDecimal getGlobalBalanceForCompanyPriorToDate(Company company, Date date) {
    BigDecimal beforeBalanceStp = transactionService.getBalanceByKeyAccountPriorToDate(company.accounts.first().stpClabe, date)
    BigDecimal beforeBalanceBanks = new BigDecimal(0)
    company.banksAccounts.each { bankAccount ->
      beforeBalanceBanks += movimientosBancariosService.getBalanceByCuentaPriorToDate(bankAccount, date)
    }
    beforeBalanceStp + beforeBalanceBanks
  }

  List<AccountStatementTransaction> recalculateBalancesForTransactions(BigDecimal beforeGlobalBalance, List<AccountStatementTransaction> asTransactions) {
    List<AccountStatementTransaction> asTransactionsRecalculated = asTransactions.sort(false, AccountStatementTransaction.comparatorByDate())
    BigDecimal balance = beforeGlobalBalance
    asTransactionsRecalculated.each { transaction ->
      balance = transaction.type == TransactionType.WITHDRAW ? (balance - transaction.amount) : (balance + transaction.amount)
      transaction.balance = balance
    }
    asTransactionsRecalculated
  }

  List<AccountStatementTransaction> obtainStpTransactions(Company company, Period period) {
    List<AccountStatementTransaction> asTransactions = []
    List<Transaction> stpTransactions = transactionService.getTransactionsAccountForPeriod(company.accounts?.first()?.stpClabe, period)
    if (stpTransactions) {
      asTransactions = parseStpTransactionsToAccountStatementTransactions(stpTransactions)
    }
    asTransactions
  }

  List<AccountStatementTransaction> obtainBankAccountsTransactions(Company company, Period period) {
    List<AccountStatementTransaction> asTransactions = []
    company.banksAccounts.each { bankAccount ->
      List<MovimientosBancarios> bankTransactions = MovimientosBancarios.findAllByCuentaAndDateEventBetween(bankAccount, period.init, period.end)
      if (bankTransactions) {
        asTransactions = parseBankTransactionsToAccountStatementTransactions(bankTransactions)
      }
    }
    asTransactions
  }

  List<AccountStatementTransaction> parseStpTransactionsToAccountStatementTransactions(List<Transaction> stpTransactions) {
    List<AccountStatementTransaction> asTransactions = []
    BankAccount bankAccount = new BankAccount(clabe:stpTransactions?.first()?.keyAccount, banco:Bank.findByName("STP"))
    stpTransactions.each { transaction ->
      asTransactions.add(new AccountStatementTransaction(
          account: bankAccount,
          date: transaction.dateCreated,
          concept:transaction.paymentConcept,
          transactionId:transaction.keyTransaction,
          amount:transaction.amount,
          type:transaction.transactionType,
          balance:transaction.balance
        )
      )
    }
    asTransactions
  }

  List<AccountStatementTransaction> parseBankTransactionsToAccountStatementTransactions(List<MovimientosBancarios> bankTransactions) {
    List<AccountStatementTransaction> asTransactions = []
    BankAccount bankAccount = bankTransactions.first().cuenta
    bankTransactions.each { transaction ->
      asTransactions.add(new AccountStatementTransaction(
          account: bankAccount,
          date: transaction.dateEvent,
          concept:transaction.concept,
          transactionId:transaction.reference,
          amount:transaction.amount,
          type:transaction.type==MovimientoBancarioType.DEBITO ? TransactionType.WITHDRAW : TransactionType.DEPOSIT,
          balance:new BigDecimal(0)
        )
      )
    }
    asTransactions
  }

  ArrayList<User> getAuthorizersByCompany(Company company) {
    directorService.findUsersOfCompanyByRole(company.id,['ROLE_AUTHORIZER_VISOR','ROLE_AUTHORIZER_EJECUTOR'])
  }

  Balance getGlobalBalanceOfCompany(Company company) {
    BigDecimal balance = 0
    BigDecimal usd = 0
    if (company.status == CompanyStatus.ACCEPTED && company.accounts) {
      balance = modulusUnoService.consultBalanceOfAccount(company.accounts.first().stpClabe)
      BigDecimal banksBalance = new BigDecimal(0)
      company.banksAccounts.each { bankAccount ->
        banksBalance += movimientosBancariosService.getBalanceByCuentaPriorToDate(bankAccount, new Date())
      }
      balance += banksBalance
    }
    new Balance(balance:balance, usd:usd)
  }

  def getNumberOfAuthorizersMissingForThisCompany(Company company) {
    def authorizers = getAuthorizersByCompany(company)
    def authorizerMissing = company.numberOfAuthorizations - authorizers.size()
    if (authorizerMissing < 0) {
      authorizerMissing = 0
    }
    authorizerMissing
  }

  boolean isEnableToSendNotificationIntegrated(Company company) {
    /*int docsMin = 4
    if (company.taxRegime == CompanyTaxRegime.FISICA_EMPRESARIAL)
      docsMin = 5*/
    if (company.banksAccounts && company.addresses && (company.status == CompanyStatus.CREATED || company.status == CompanyStatus.REJECTED ))
      return true
    false
  }

  def listCompanyByFilters(def queryFilters) {
    def companies = Company.where {
        status != CompanyStatus.CREATED
    }.list()

    if (queryFilters.status)
        companies = companies.findAll { it.status == CompanyStatus."${queryFilters.status}" }
    if (queryFilters.bussinessName)
        companies = companies.findAll { it.bussinessName == queryFilters.bussinessName }
    if (queryFilters.rfc)
        companies = companies.findAll { it.rfc == queryFilters.rfc }

    companies
  }

  def formattingTransactionsForXls(def transactions) {
    List formattedTransactions = []
    transactions.each { mov ->
      Map transaction = [:]
      transaction.date = mov.date
      transaction.account = mov.account
      transaction.concept = mov.concept
      transaction.id = mov.transactionId ?: ""
      transaction.credit = mov.type == TransactionType.DEPOSIT ? mov.amount : ""
      transaction.debit = mov.type == TransactionType.WITHDRAW ? mov.amount : ""
      transaction.balance = mov.balance
      formattedTransactions << transaction
    }
    formattedTransactions
  }

  def sendDocumentsPerInvoice(def params, Company company) {
    def documents = [rfc:company.rfc, id:company.id.toString(), key:params.key,cer:params.cer,logo:params.logo,,password:params.password, certNumber:params.numCert, serie:params.serie]
    def result = restService.sendFilesForInvoiceM1(documents)
    result
  }

  def updateDocumentsToStamp(def params, def rfc) {
    def documents = [key:params.key,cer:params.cer,logo:params.logo,,password:params.password, rfc:rfc, certNumber:params.numCert, serie:params.serie]
    log.info "Updating documents to stamp: ${documents}"
    def result = restService.updateFilesForInvoice(documents)
    result
  }

  def isAvailableForGenerateInvoices(Company company) {
    def response = restService.existEmisorForGenerateInvoice(company.rfc, company.id.toString())
  }

  PendingAccounts obtainPendingAccountsOfPeriod(Date startDate, Date endDate, Company company) {
    log.info "Pending Accounts between ${startDate} and ${endDate} for ${company}"
    PendingAccounts pendingAccounts = new PendingAccounts(startDate:startDate, endDate:endDate)
    pendingAccounts.listPayments = PurchaseOrder.findAllByFechaPagoBetweenAndStatusAndCompany(startDate, endDate, PurchaseOrderStatus.AUTORIZADA, company)
    def listExpiredPayments = PurchaseOrder.findAllByFechaPagoLessThanAndStatusAndCompany(startDate, PurchaseOrderStatus.AUTORIZADA, company)
    pendingAccounts.listExpiredPayments = listExpiredPayments
    pendingAccounts.totalExpiredPayments = listExpiredPayments ? listExpiredPayments.sum {it.total} : new BigDecimal(0)

    pendingAccounts.listCharges = SaleOrder.findAllByFechaCobroBetweenAndStatusInListAndCompany(startDate, endDate, [SaleOrderStatus.EJECUTADA, SaleOrderStatus.AUTORIZADA], company)
    def listExpiredCharges = SaleOrder.findAllByFechaCobroLessThanAndStatusInListAndCompany(startDate, [SaleOrderStatus.EJECUTADA, SaleOrderStatus.AUTORIZADA], company)
    pendingAccounts.listExpiredCharges = listExpiredCharges
    pendingAccounts.totalExpiredCharges = listExpiredCharges ? listExpiredCharges.sum {it.total} : new BigDecimal(0)

    pendingAccounts.totalPayments = pendingAccounts.listPayments ? pendingAccounts.listPayments.sum { it.total } : new BigDecimal(0)
    pendingAccounts.totalCharges = pendingAccounts.listCharges ? pendingAccounts.listCharges.sum { it.total } : new BigDecimal(0)
    pendingAccounts
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  Company saveInsideAndAssingCorporate(Company company, Long corporateId){
    // TODO: Se podría revalidar que el usuario sea corporativo
    if(company.validate()){
      Corporate corporate = Corporate.get(corporateId)
      corporateService.addCompanyToCorporate(corporate, company)
      company
    }
    else {
      throw new RuntimeException(company.errors*.toString().join(","))
    }
  }

  private def isAvailableForInvoices(def response) {
    response.find { it.value == false}
  }

  def updateDateChargeForSaleOrder(String orderId, String chargeDate) {
    Date newChargeDate = new SimpleDateFormat("dd/MM/yyyy").parse(chargeDate)
    saleOrderService.updateDateChargeForOrder(orderId.toLong(), newChargeDate)
  }

  def updateDatePaymentForPurchaseOrder(String orderId, String paymentDate) {
    Date newPaymentDate = new SimpleDateFormat("dd/MM/yyyy").parse(paymentDate)
    purchaseOrderService.updateDatePaymentForOrder(orderId.toLong(), newPaymentDate)
  }

  Boolean isCompanyEnabledToStamp(Company company) {
    Address fiscalAddress = company.addresses.find {it.addressType == AddressType.FISCAL}
    def documents = isAvailableForGenerateInvoices(company)
    documents.status && fiscalAddress
  }

  List<SaleOrder> getDetailPastDuePortfolio(Long idCompany, Integer days) {
    saleOrderService.obtainListPastDuePortfolio(idCompany, days)
  }

  def assignAliasStpToCompany(Company company, String alias) {
    ModulusUnoAccount m1 = company.accounts.first()
    m1.aliasStp = alias
    m1.save()
  }

  Boolean companyIsEnabledToPay(Company company) {
    company.accounts.first().aliasStp && company.commissions.find { it.type == CommissionType.PAGO }
  }

  void changeSerieForInvoicesOfCompany(Company company, String serie, String folio) {
    Map newSerie = [rfc:company.rfc, serie:serie, folio:folio]
    invoiceService.changeSerieAndInitialFolioToStampInvoiceForEmitter(newSerie)
  }

  String executeOperationsCloseForCompany(Company company) {
    log.info "Init operations close for company ${company}"
    Period period = collaboratorService.getPeriodForConciliationStp()
    Map transactions = stpService.getTransactionsForCompanyInPeriod(company, period)
    applyOperationsCloseTransaction(company, transactions)
  }

  @Transactional
  private String applyOperationsCloseTransaction(Company company, Map transactions) {
    log.info "Applying Operations Close for ${company} with transactions ${transactions}"
    String status = "OK"
    String dateTransaction = new SimpleDateFormat("yyyyMMdd").format(transactions.period.init)
    String tracingFinal = "${dateTransaction}${company.accounts.first().aliasStp}"
    Map movFinal = transactions.transactions.find { it.tracing.contains(tracingFinal) }
    if (movFinal) {
      log.info "Recording final transfer for ${company} of the ${dateTransaction}"
      transactionService.createFinalTransferTransaction(movFinal)
      finalTransactionResultService.createFinalTransactionResult([company:company, dateTransaction:Date.parse("yyyyMMdd",dateTransaction), status:FinalTransactionResultStatus.SUCCESSFUL, comment:"Final Transfer Transaction executed"])
    } else {
      status = "NOT FOUND"
      log.error "No se encontró registro del traspaso final para la empresa ${company} del día ${dateTransaction}"
      finalTransactionResultService.createFinalTransactionResult([company:company, dateTransaction:Date.parse("yyyyMMdd",dateTransaction), status:FinalTransactionResultStatus.FAILED, comment:"Final Transfer NOT FOUND"])
    }
    status
  }

  List<Company> getAllCompaniesAcceptedAndWithAliasStp() {
    def c = Company.createCriteria()
    def list = c.list {
      eq("status", CompanyStatus.ACCEPTED)
      accounts {
        and {
          isNotNull("aliasStp")
          ne("aliasStp", "")
          isNotNull("stpClabe")
          ne("stpClabe", "")
        }
      }
    }
    list
  }

  List<Company> getAllCompaniesAcceptedWithFixedCommissionDefined() {
    def c = Company.createCriteria()
    def list = c.list {
      eq("status", CompanyStatus.ACCEPTED)
      commissions {
        eq("type", CommissionType.FIJA)
      }
    }
    list
  }

  List<User> getUsersWithRoleForCompany(String role, Company company) {
    def crit = UserRoleCompany.createCriteria()
    def urc = crit.list {
      roles {
        eq("authority", role)
      }
      eq("company", company)
    }
    List<User> users = []
    urc.each {
      users.add(it.user)
    }
    users
  }

  String executeOperationsCloseForCompanyInDate(Company company, Date date) {
    log.info "Init operation close for company ${company} in date ${date}"
    Period period = collaboratorService.getPeriodStpConciliationInDate(date)
    Map transactions = stpService.getTransactionsForCompanyInPeriod(company, period)
    applyOperationsCloseTransaction(company, transactions)
  }

}
