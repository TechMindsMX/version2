package com.modulus.uno

import grails.transaction.Transactional

class DashboardController {

  def companyService
  def springSecurityService
  OrganizationService organizationService

  def index() {
    def user = springSecurityService.currentUser
    def companyList

    if (session.corporate)
      companyList = companyService.findCompaniesByCorporateAndStatus(CompanyStatus.ACCEPTED,session.corporate.id)
    else
      companyList = organizationService.findAllCompaniesOfUser(user)

    def corporates= Corporate.list()

    [companies:companyList,
     corporates:corporates,
     companiesCount: companyList.size(),
     user:user]
  }

  def jobs() {
    Company company = Company.get(session.company)
    [
    depositOrderAuthorizedCount : DepositOrder.countByStatusAndCompany(DepositOrderStatus.AUTHORIZED, company),
    cashOutOrderAuthorizedCount : CashOutOrder.countByStatusAndCompany(CashOutOrderStatus.AUTHORIZED, company),
    saleOrderAuthorizedCount : SaleOrder.countByStatusAndCompany(SaleOrderStatus.AUTORIZADA, company),
    saleOrderToCancelBillForExecuteCount : SaleOrder.countByStatusAndCompany(SaleOrderStatus.CANCELACION_AUTORIZADA, company),
    purchaseOrderAuthorizedCount : PurchaseOrder.countByStatusAndCompanyAndIsMoneyBackOrder(PurchaseOrderStatus.AUTORIZADA, company, false),
    moneyBackOrderAuthorizedCount : PurchaseOrder.countByStatusAndCompanyAndIsMoneyBackOrder(PurchaseOrderStatus.AUTORIZADA, company, true),
    loanOrderAuthorizeCount : LoanOrder.countByStatusAndCompany(LoanOrderStatus.AUTHORIZED, company),
    loanOrderToExecuteCount : LoanOrder.countByStatusAndCompany(LoanOrderStatus.ACCEPTED, company),
    feesReceiptCount : FeesReceipt.countByStatusAndCompany(FeesReceiptStatus.AUTORIZADA, company),
    paymentsToConciliateCount: Payment.countByStatusAndCompany(PaymentStatus.PENDING, company),
    loanPaymentOrderAuthorizedCount : LoanPaymentOrder.countByStatusAndCompany(LoanPaymentOrderStatus.AUTHORIZED, company)
    ]
  }

  def authorizations() {
    Company company = Company.get(session.company)
    render view:'jobs', model:[
      depositOrderToAuthorizeCount : DepositOrder.countByStatusAndCompany(DepositOrderStatus.VALIDATE, company),
      cashOutOrderToAuthorizeCount : CashOutOrder.countByStatusAndCompany(CashOutOrderStatus.TO_AUTHORIZED, company),
      saleOrderToAuthorizeCount : SaleOrder.countByStatusAndCompany(SaleOrderStatus.POR_AUTORIZAR, company),
      saleOrderToCancelBillForAuthorizeCount : SaleOrder.countByStatusAndCompany(SaleOrderStatus.CANCELACION_POR_AUTORIZAR, company),
      purchaseOrderToAuthorizeCount : PurchaseOrder.countByStatusAndIsMoneyBackOrderAndCompany(PurchaseOrderStatus.POR_AUTORIZAR, false, company),
      moneyBackOrderToAuthorizeCount : PurchaseOrder.countByStatusAndIsMoneyBackOrderAndCompany(PurchaseOrderStatus.POR_AUTORIZAR, true, company),
      loanOrderToAuthorizeCount : LoanOrder.countByStatusAndCompany(LoanOrderStatus.VALIDATE, company),
      feesReceiptToAuthorizeCount : FeesReceipt.countByStatusAndCompany(FeesReceiptStatus.POR_AUTORIZAR, company),
      loanPaymentOrderToAuthorizeCount : LoanPaymentOrder.countByStatusAndCompany(LoanPaymentOrderStatus.VALIDATE, company)
    ]
  }

  def listCompanies() {
     params.max = (params.max ?: 10)
     [companies:Company.list(params),companiesCount: Company.count()]
  }

  def defineCostCenters() {
     params.max = (params.max ?: 10)
     [companies:Company.list(params), companiesCount:Company.count()]
  }

  @Transactional
  def saveAliasStp() {
    companyService.assignAliasStpToCompany(Company.get(params.company), params.aliasStp)
    redirect action:'defineCostCenters'
  }
}
