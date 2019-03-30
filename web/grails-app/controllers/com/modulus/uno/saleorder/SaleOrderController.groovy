package com.modulus.uno.saleorder

import static org.springframework.http.HttpStatus.*
import grails.converters.JSON
import wslite.rest.*
import grails.transaction.Transactional
import com.modulus.uno.catalogs.UnitType

import com.modulus.uno.ClientService
import com.modulus.uno.BusinessEntityService
import com.modulus.uno.CompanyService
import com.modulus.uno.DocumentService
import com.modulus.uno.S3AssetService
import com.modulus.uno.EmailSenderService
import com.modulus.uno.CollaboratorService

import com.modulus.uno.BusinessEntity
import com.modulus.uno.Company
import com.modulus.uno.Product
import com.modulus.uno.Corporate

import com.modulus.uno.Period
import com.modulus.uno.status.SaleOrderStatus

@Transactional(readOnly = true)
class SaleOrderController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  ClientService clientService
  BusinessEntityService businessEntityService
  SaleOrderService saleOrderService
  def springSecurityService
  CompanyService companyService
  DocumentService documentService
  BusinessEntity businessEntity
  S3AssetService s3AssetService
  EmailSenderService emailSenderService
  CollaboratorService collaboratorService

  @Transactional
  def authorizeSaleOrder(SaleOrder saleOrder){
    saleOrder = saleOrderService.addAuthorizationToSaleOrder(saleOrder, springSecurityService.currentUser)
    if (saleOrderService.isFullAuthorized(saleOrder)) {
      saleOrderService.authorizeSaleOrder(saleOrder)
    }

    redirect action:'list', params:[status:'POR_AUTORIZAR']
  }

  @Transactional
  def authorizeCancelBill(SaleOrder saleOrder) {
    saleOrder.status = SaleOrderStatus.CANCELACION_AUTORIZADA
    saleOrder.save()
    emailSenderService.notifySaleOrderChangeStatus(saleOrder)
    redirect action:'list', params:[status:'CANCELACION_POR_AUTORIZAR']
  }


  def create() {
    def company = Company.get(session.company ? session.company.toLong() : params.companyId)
    respond new SaleOrder(params),model: [company:company]
  }

  def edit(SaleOrder saleOrder) {
    respond saleOrder
  }

  @Transactional
  def cancelSaleOrder(SaleOrder saleOrder){
    flash.message = message(code: 'saleOrder.cancel', args: [:])
    saleOrderService.cancelOrRejectSaleOrder(saleOrder, SaleOrderStatus.CANCELADA)
    redirect action:'list', params:[companyId:saleOrder.company.id, status:"${SaleOrderStatus.POR_AUTORIZAR}"]
  }

  def chooseClientForSale(){
    def company = Company.get(session.company.toLong())
    List<BusinessEntity> clients = saleOrderService.searchClientsForCompany(company, params.q)
    def client = BusinessEntity.get(params.id)
    render view:'create',model:([company:company, clients:clients, client:client] + params)
  }

  @Transactional
  def deleteOrder(SaleOrder saleOrder) {
    saleOrderService.deleteSaleOrder(saleOrder)
    redirect action:'list'
  }

  @Transactional
  def executeSaleOrder(SaleOrder saleOrder){
    log.info "Execute saleOrder ${saleOrder.id} with pdf template ${saleOrder.pdfTemplate}"
    String messageSuccess = message(code:"saleOrder.already.executed")
    if (saleOrderIsInStatus(saleOrder, SaleOrderStatus.AUTORIZADA)) {
      saleOrderService.executeSaleOrder(saleOrder)
      emailSenderService.notifySaleOrderChangeStatus(saleOrder)
      messageSuccess = message(code:"saleOrder.executed.message")
    }

    redirect action:'show', id:saleOrder.id
  }

  @Transactional
  def generatePdf(SaleOrder saleOrder) {
    saleOrderService.generatePdfForStampedInvoice(saleOrder)
    redirect action:'show', id:saleOrder.id
  }

  private Boolean saleOrderIsInStatus(SaleOrder saleOrder, def statusExpected) {
    saleOrder.status == statusExpected
  }

  @Transactional
  def executeCancelBill(SaleOrder saleOrder) {
    saleOrderService.executeCancelBill(saleOrder)
    redirect action:'list', params:[status:"${SaleOrderStatus.CANCELACION_AUTORIZADA}"]
  }

  def getProduct(){
    def company = Company.get(session.company)
    def product = Product.findByNameAndCompany(params.nombre, company)
    // TODO: Aqui podría ir un marshaller
    Map model = [:]
    model.sku = product.sku
    model.price = product.price
    model.ieps = product.ieps
    model.iva = product.iva
    model.unit = product.unitType?.name
    model.currency = product.currencyType.name()
    model.satKey = product.satKey
    render model as JSON
  }

  def getProductBySku(){
    def company = Company.get(session.company)
    def product = Product.findBySkuAndCompany(params.sku, company)
    // TODO: Aqui podría ir un marshaller
    Map model = [:]
    model.productName = product.name
    model.price = product.price
    model.ieps = product.ieps
    model.iva = product.iva
    model.unit = product.unitType?.name
    model.currency = product.currencyType.name()
    model.satKey = product.satKey
    render model as JSON
  }

  def index() {
    def roles = springSecurityService.getPrincipal().getAuthorities()
    def company = Company.get(session.company ?: params.companyId)
    respond SaleOrder.findAllByCompany(company), model:[company:company]
  }

  def list() {
    params.max = params.max ?: 25
    params.sort = "dateCreated"
    params.order = "desc"
    def saleOrders = [:]
    saleOrders = saleOrderService.getSaleOrdersToList(session.company?session.company.toLong():session.company, params)

    [saleOrders: saleOrders.list, saleOrderCount: saleOrders.items, messageSuccess:params.messageSuccess]
  }

  def listProducts(){
    def company = Company.get(session.company)
    def products = []
    if (params.pname)
      products = Product.findAllByNameIlikeAndCompany("%${params.pname}%", company)
    else if (params.psku)
      products = Product.findAllBySkuIlikeAndCompany("%${params.psku}%", company)
      render products as JSON
  }

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'saleOrder.label', default: 'SaleOrder'), params.id])
        redirect action: "index", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }

  def previewInvoicePdf(SaleOrder saleOrder) {
    log.info "Showing preview pdf for SaleOrder: ${saleOrder.id} with template: ${saleOrder.pdfTemplate}"
    def file = saleOrderService.generatePreviewInvoiceForSaleOrderWithTemplate(saleOrder)
    response.setContentType("application/pdf")
    response.outputStream << file
  }

  @Transactional
  def save(SaleOrderCommand saleOrderCommand) {
    log.info "Creating a sale order: ${saleOrderCommand.dump()}"
    if (!saleOrderCommand) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    SaleOrder saleOrder = saleOrderCommand.createOrUpdateSaleOrder()
    if (saleOrder.hasErrors()) {
      transactionStatus.setRollbackOnly()
      flash.message = "No se pudo crear la orden de venta"
      redirect action:'create'
    }

    saleOrder.save()

    if (saleOrder.id) {
      redirect action:'show', id:saleOrder.id
    } else {
      flash.message = "No se pudo crear la orden de venta"
      redirect action:'create'
    }
  }

  def searchClientForSale(){
    def company = Company.get(session.company ? session.company.toLong() : params.companyId)
    List<BusinessEntity> clients = saleOrderService.searchClientsForCompany(company, params.q)
    if(clients.isEmpty()){
      flash.message = "No se encontraron coincidencias"
    }
    render view:'create',model:([company:company, clients:clients] + params)
  }

  @Transactional
  def sendOrderToConfirmation(SaleOrder saleOrder){
    saleOrderService.sendOrderToConfirmation(saleOrder)
    flash.message = message(code: 'saleOrder.validation', args: [:])
    redirect action:'list'
  }

  def show(SaleOrder saleOrder) {
    Company company = Company.get(session.company)
    respond saleOrder, model:[saleOrderItem: new SaleOrderItem(), user:springSecurityService.currentUser, isEnabledToStamp:companyService.isCompanyEnabledToStamp(saleOrder.company), unitTypes:UnitType.findAllByCompany(company, [sort:"name"])]
  }

  def showFactura(SaleOrder saleOrder){
    saleOrderService.getFactura(saleOrder, 'xml')
    redirect action:'index', params:[companyId:saleOrder.company.id]
  }

  def showSaleOrdersByAuthorize() {
    def user = springSecurityService.currentUser
    def companies = companyService.findCompaniesForThisUser(user)
    [companies:companies]
  }

  @Transactional
  def rejectSaleOrder(SaleOrder saleOrder){
    saleOrderService.cancelOrRejectSaleOrder(saleOrder, SaleOrderStatus.RECHAZADA)
    flash.message = message(code: 'saleOrder.execute', args: [:])
    redirect action:'list'
  }

  @Transactional
  def update(SaleOrder saleOrder) {
    if (saleOrder == null) {
      notFound()
      return
    }

    if (saleOrder.hasErrors()) {
      respond saleOrder.errors, view:'edit'
      return
    }

    saleOrder.save()

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.updated.message', args: [message(code: 'saleOrder.label', default: 'SaleOrder'), saleOrder.id])
        redirect saleOrder
      }
      '*'{ respond saleOrder, [status: OK] }
    }
  }

  @Transactional
  def requestCancelBill(SaleOrder saleOrder) {
    saleOrder.status = SaleOrderStatus.CANCELACION_POR_AUTORIZAR
    saleOrder.save()
    emailSenderService.notifySaleOrderChangeStatus(saleOrder)
    redirect action:'list'
  }

  @Transactional
  def deleteItem(SaleOrderItem item) {
    Long idSaleOrder = item.saleOrder.id
    saleOrderService.deleteItemFromSaleOrder(item.saleOrder, item)
    redirect action:'show', id:idSaleOrder
  }

  def conciliationSaleOrderPerClients() {
    def company = Company.get(session.company)
    def clients = businessEntityService.findBusinessEntityByKeyword(params.q, "CLIENT", company)
    [clients: clients,params:params]
  }

  def searchSaleOrderExcecuteByClient() {
    Company company = Company.get(session.company)
    List saleOrders = SaleOrder.findAllByRfcAndStatus(params.rfc, SaleOrderStatus.EJECUTADA)
    List payments = Payment.findAllByRfcAndStatus(params.rfc, PaymentStatus.PENDING)
    render view:"/payment/reconcile", model:[payments: payments, saleOrders:saleOrders, company: company]
  }

  def applyDiscount(SaleOrder saleOrder) {
    redirect action:"show", id:saleOrder.id
  }

  def getCurrencyOfSaleOrder(){
    log.info "Get the currency of sale order ${params.saleOrderId}"
    SaleOrder saleOrder = SaleOrder.get(params.saleOrderId)
    Map model = [currency:saleOrder.currency]
    render model as JSON
  }

  @Transactional
  def createCommissionsInvoice(Company company) {
    log.info "Create commissions invoice for period: ${params.startDate} / ${params.endDate}"
    Corporate corporate = Corporate.get(params.corporateId)
    Period period = collaboratorService.createPeriod(params.startDate, params.endDate)
    SaleOrder saleOrder = saleOrderService.createCommissionsInvoiceForCompanyAndPeriod(company, period)
    redirect controller:"corporate", action:"commissions", id:corporate.id
  }

	@Transactional
	def abortBillCancellation(SaleOrder saleOrder) {
		log.info "Aborting bill cancellation from sale order: ${saleOrder.id}"
		saleOrder.status = SaleOrderStatus.EJECUTADA
		saleOrder.save()
		redirect action:"list"
	}

  def search() {
    log.info "Search sale orders with params: ${params}"
    if (!params.rfc && !params.clientName && !params.stampedDateInit && !params.stampedDateEnd && !params.status) {
      redirect action:"list"
      return
    }

    def saleOrders = saleOrderService.searchSaleOrders(session.company.toLong(), params)

    render view:"list", model:[saleOrders: saleOrders, filterValues:[rfc:params.rfc, clientName:params.clientName, stampedDateInit:params.stampedDateInit, stampedDateEnd:params.stampedDateEnd, status:params.status]]
  }

  def listOrdersWithAmountToPayForClient(BusinessEntity businessEntity) {
    Company company = Company.get(session.company)
    List<SaleOrder> list = saleOrderService.getAllSaleOrdersWithAmountToPayForRfc(company, businessEntity.rfc)
    render view:"list", model:[saleOrders: list, filterValues:[rfc:businessEntity.rfc, clientName:businessEntity]]
  }

  def listTotalAmountExecutedAndAuthorized(BusinessEntity businessEntity) {
    Company company = Company.get(session.company)
    List<SaleOrder> list = saleOrderService.getAllSaleOrdersExecutedAndAuthorizedForRfc(company, businessEntity.rfc)
    render view:"list", model:[saleOrders: list, filterValues:[rfc:businessEntity.rfc, clientName:businessEntity]]
  }

  def listOrdersAlreadyConciliate(BusinessEntity businessEntity) {
    Company company = Company.get(session.company)
    List<SaleOrder> list = saleOrderService.getAllSaleOrdersAlreadyConciliate(company, businessEntity.rfc)
    render view:"list", model:[saleOrders: list, filterValues:[rfc:businessEntity.rfc, clientName:businessEntity]]
  }

  @Transactional
  def loadSerieFromInvoice(SaleOrder saleOrder) {
    saleOrderService.getSerieForSaleOrderFromInvoice(saleOrder)
    redirect action:"list"
  }

  @Transactional
  def loadFolioFromInvoice(SaleOrder saleOrder) {
    saleOrderService.getFolioForSaleOrderFromInvoice(saleOrder)
    redirect action:"list"
  }

  @Transactional
  def stampedDateForSaleOrders(SaleOrder saleOrder) {
    saleOrderService.updateStampDateAlreadyUpdate(saleOrder)
    redirect action:"list"
  }

}
