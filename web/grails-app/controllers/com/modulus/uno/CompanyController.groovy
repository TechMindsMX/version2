package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import java.text.SimpleDateFormat
import pl.touk.excel.export.WebXlsxExporter

class CompanyController {

  def springSecurityService
  def companyService
  def userService
  def documentService
  def clientService
  def providerService
  def managerApplicationService
  def modulusUnoService
  def corporateService
  RoleService roleService

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def index(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    def user = springSecurityService.currentUser
    def companies = companyService.allCompaniesByUser(user)
    respond companies, model:[companiesCount: companies?.size()]
  }

  def show(Company company) {
    if(!company){
      redirect  (controller: "dashboard" , action:"index")
    } else {
      company.refresh()
      def balance, usd, documents
      params.sepomexUrl = grails.util.Holders.grailsApplication.config.sepomex.url
      if (company.status == CompanyStatus.ACCEPTED) {
        documents = companyService.isAvailableForGenerateInvoices(company)
        if (company.accounts){
          balance = modulusUnoService.consultBalanceOfAccount(company.accounts.first().stpClabe)
        }
      }
      def isAvailable = companyService.isEnableToSendNotificationIntegrated(company)

      respond company, model:[available:isAvailable, balance:balance, usd:new BigDecimal(0), documents:documents]
    }
  }

  def create() {
    respond new Company(params)
  }

  def uploadDocumentsUser() {
    def company = Company.findById(session.company.toLong())
    render view:"/uploadDocuments/uploadDocumentsUser",model:[company:company]
  }

  def edit(Company company) {
    respond company,model:[edit:true]
  }

  def crearCuentaSTP() {
  User user = springSecurityService.currentUser
  def company = Company.findById(params.id.toLong())
  managerApplicationService.acceptingCompanyToIntegrate(company.id, user.profile.email)
  redirect(action:"show")
  }

  @Transactional
  def update(Company company) {
    if (company == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    if (company.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond company.errors, view:'edit'
      return
    }

    company.save flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.updated.message', args: [message(code: 'company.label', default: 'Company'), company.id])
        redirect company
      }
      '*'{ respond company, [status: OK] }
    }
  }

  def setCompanyInSession() {
    def company = params.company
    session['company'] = company
    def currentUser = springSecurityService.currentUser
    def companyInfo = Company.findById(company.toLong())
    roleService.updateTheUserRolesOfUserAtThisCompany(currentUser,companyInfo)
    if (companyInfo.status != CompanyStatus.ACCEPTED) {
      redirect(action:"show",id:"${company}")
      return
    }
    redirectToViewAccordingToRole()
  }

  def redirectToViewAccordingToRole() {
    def user = springSecurityService.currentUser
    def autoriti = user.getAuthorities()
    if (autoriti.contains(Role.findByAuthorityInList(['ROLE_AUTHORIZER_VISOR','ROLE_AUTHORIZER_EJECUTOR'])))
      redirect(action:'authorizations', controller:'dashboard')
    else
      redirect(action:"accountStatement")
  }

  def rejected(Company company) {
    def rowsOfReasons = managerApplicationService.obtainReasonOfRejectedCompanyRequestByStatus(company,true)
    def rowDocuments = rowsOfReasons.findAll {row -> row.typeClass == "document"}
    def rowRepresentatives = rowsOfReasons.findAll {row -> row.typeClass == "legalRepresentative"}
    def rowCompany = rowsOfReasons.find {row -> row.typeClass == "company"}
    def documentsByUser = managerApplicationService.getMapOfUsersWithDocuments(company.legalRepresentatives,company.id)
    [company:company,legalRepresentatives:documentsByUser,reasonDocuments:rowDocuments,reasonRepresentatives:rowRepresentatives,reasonCompany:rowCompany, baseUrlDocuments:grailsApplication.config.grails.url.base.images]
  }

  @Transactional
  def delete(Company company) {

    if (company == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    company.delete flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'company.label', default: 'Company'), company.id])
        redirect action:"index", method:"GET"
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'company.label', default: 'Company'), params.id])
        redirect action: "index", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }

  def accountStatement(){
    def company = Company.get(session.company.toLong())
    String startDate = params.startDate ? new SimpleDateFormat("dd-MM-yyyy").format(params.startDate) : ""
    String endDate = params.endDate ? new SimpleDateFormat("dd-MM-yyyy").format(params.endDate) : ""
    log.info "StartDate: ${startDate}"
    log.info "EndDate: ${endDate}"
    AccountStatement accountStatement = companyService.getAccountStatementOfCompany(company, startDate, endDate)

    respond accountStatement
  }

  def generatePdfForAccountStatement() {
    Company company = Company.get(params.company)
    String startDate = params.startDate ? new SimpleDateFormat("dd-MM-yyyy").format(new Date(params.startDate)) : ""
    String endDate = params.endDate ? new SimpleDateFormat("dd-MM-yyyy").format(new Date(params.endDate)) : ""
    AccountStatement accountStatement = companyService.getAccountStatementOfCompany(company, startDate, endDate)
    renderPdf(template: "/documentTemplates/accountStatementCompany", model: [accountStatement:accountStatement, startDateFormatted:startDate, endDateFormatted:endDate])
  }

  def generateXlsForAccountStatement() {
    Company company = Company.get(params.company)
    String startDate = params.startDate ? new SimpleDateFormat("dd-MM-yyyy").format(new Date(params.startDate)) : ""
    String endDate = params.endDate ? new SimpleDateFormat("dd-MM-yyyy").format(new Date(params.endDate)) : ""
    AccountStatement accountStatement = companyService.getAccountStatementOfCompany(company, startDate, endDate)

    def headers = ['Fecha', 'Cuenta','Concepto', 'Id. Transacción', 'Abono', 'Cargo', 'Saldo']
    def withProperties = ['date', 'account', 'concept', 'id', 'credit', 'debit', 'balance']
    def formattedTransactions = companyService.formattingTransactionsForXls(accountStatement.transactions)
    println "Transacciones: ${formattedTransactions}"
    new WebXlsxExporter().with {
      setResponseHeaders(response)
      fillRow(["Estado de Cuenta - ${accountStatement.company.bussinessName}"], 0)
      fillRow(["Desde el: ${startDate}, Hasta el: ${endDate}"], 2)
      fillRow(headers, 4)
      add(formattedTransactions, withProperties, 5)
      save(response.outputStream)
    }

  }

  def sendFilesToCreateInvoice() {
    Company company = Company.get(session.company)
    def responseStatus = companyService.sendDocumentsPerInvoice(params, company)
    flash.responseStatus = responseStatus
    redirect(action:"show",id:"${session.company.toLong()}")
  }

  def pendingAccounts() {
    Company company = Company.get(session.company)
    def sdf = new SimpleDateFormat("dd/MM/yyyy")
    Date startDate = params.startDate ? sdf.parse(params.startDate) : sdf.parse(sdf.format(new Date()))
    Date endDate = params.endDate ? new SimpleDateFormat("dd/MM/yyyy").parse(params.endDate) : sdf.parse(sdf.format(new Date()))

    [pendingAccounts: companyService.obtainPendingAccountsOfPeriod(startDate, endDate, company), mainAccount:company.banksAccounts.find {it.concentradora}]
  }

  def generateXlsForPendingAccounts() {
    Company company = Company.get(session.company)
    Date startDate = new Date(params.startDate)
    Date endDate = new Date(params.endDate)
    PendingAccounts pendingAccounts = companyService.obtainPendingAccountsOfPeriod(startDate, endDate, company)

    def headersCharges = ['Cliente', 'Fecha Cobro', 'Monto']
    def propertiesCharges = ['clientName', 'fechaCobro', 'total']
    def headersPayments = ['Proveedor', 'Fecha Pago', 'Monto']
    def propertiesPayments = ['providerName', 'fechaPago', 'total']

    new WebXlsxExporter().with {
      setResponseHeaders(response)
      fillRow(["${company.bussinessName} - Cuentas por Cobrar/Pagar"], 0)
      fillRow(["Desde el: ${new SimpleDateFormat("dd-MM-yyyy").format(startDate)}, Hasta el: ${new SimpleDateFormat("dd-MM-yyyy").format(endDate)}"], 2)
      fillRow(["Cuentas por Cobrar"], 4)
      fillRow(["Total:", pendingAccounts.totalCharges,"","","Total vencido:", pendingAccounts.totalExpiredCharges], 5)
      fillRow(headersCharges, 7)
      def listChargesFull = pendingAccounts.listExpiredCharges + pendingAccounts.listCharges
      add(listChargesFull.sort{it.fechaCobro}, propertiesCharges, 8)
      int iniRowPays = 10+listChargesFull.size()
      fillRow(["Cuentas por Pagar"], iniRowPays)
      fillRow(["Total:", pendingAccounts.totalPayments,"","","Total vencido:", pendingAccounts.totalExpiredPayments], iniRowPays+1)
      fillRow(headersPayments, iniRowPays+3)
      def listPaymentsFull = pendingAccounts.listExpiredPayments + pendingAccounts.listPayments
      add(listPaymentsFull.sort{it.fechaPago}, propertiesPayments, iniRowPays+4)
      save(response.outputStream)
    }
  }

  @Transactional
  def updateDateCharge() {
    companyService.updateDateChargeForSaleOrder(params.chargeId, params.fechaCobro)
    Company company = Company.get(session.company)
    PendingAccounts pendingAccounts = companyService.obtainPendingAccountsOfPeriod(new SimpleDateFormat("dd/MM/yyyy").parse(params.startDate), new SimpleDateFormat("dd/MM/yyyy").parse(params.endDate), company)

    render view:'pendingAccounts', model:[pendingAccounts:pendingAccounts, mainAccount:company.banksAccounts.find {it.concentradora}]
  }

  @Transactional
  def updateDatePayment() {
    companyService.updateDatePaymentForPurchaseOrder(params.paymentId, params.fechaPago)
    Company company = Company.get(session.company)
    PendingAccounts pendingAccounts = companyService.obtainPendingAccountsOfPeriod(new SimpleDateFormat("dd/MM/yyyy").parse(params.startDate), new SimpleDateFormat("dd/MM/yyyy").parse(params.endDate), company)

    render view:'pendingAccounts', model:[pendingAccounts:pendingAccounts, mainAccount:company.banksAccounts.find {it.concentradora}]
  }

  def pastDuePortfolio() {
  }

  def pastDuePortfolioForDays() {
    Integer days = params.days.toInteger()
    String alert = getAlertColor(days)
    List<SaleOrder> detailPastDuePortfolio = companyService.getDetailPastDuePortfolio(session.company.toLong(), days)
    render view:"pastDuePortfolio", model:[detail:detailPastDuePortfolio, days:days, alert:alert]
  }

  def pdfForPastDuePortfolio() {
    Integer days = params.days.toInteger()
    Company company = Company.get(session.company)
    List<SaleOrder> detailPastDuePortfolio = companyService.getDetailPastDuePortfolio(session.company.toLong(), days)
    renderPdf(template: "/documentTemplates/pastDuePortfolioForCompany", model: [company:company, detail:detailPastDuePortfolio, days:days])
  }

  def changeStampDocuments(Company company) {
    respond company
  }

  def updateDocumentsToStamp() {
    def company = Company.get(session.company)
    def responseStatus = companyService.updateDocumentsToStamp(params, company)
    flash.responseStatus = "Archivos de Facturación Actualizados"//responseStatus
    redirect action:"show", id:company.id

  }

  def changeSerieForInvoices(Company company) {
    companyService.changeSerieForInvoicesOfCompany(company, params.serie, params.folio)
    redirect action:"show", id:company.id
  }

  private String getAlertColor(Integer days) {
    switch(days) {
      case 30: "info"
        break
      case 60: "warning"
        break
      case 90: "warning"
        break
      case 120: "danger"
        break
    }
  }

}
