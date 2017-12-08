package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import wslite.rest.*

import com.modulus.uno.purchaseOrder.PurchaseOrderItemService
import com.modulus.uno.catalogs.UnitType


class PurchaseOrderController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  BusinessEntityService businessEntityService
  PurchaseOrderService purchaseOrderService
  PurchaseOrderItemService purchaseOrderItemService
  def springSecurityService
  def companyService
  def emailSenderService

  // TODO: Refactor, código posiblemente repetido
  def addDocumentToPurchaseOrder(PurchaseOrder purchaseOrder){
    def document = params.invoiceDocument
    if (purchaseOrderService.validateDocument(purchaseOrder, document)){
      purchaseOrderService.addInvoiceToPurchaseOrder(document, purchaseOrder.id, "invoiceForPO")
    } else {
      params.badfile = "La extensión del archivo no es válida, o ya existe"
    }
    redirect action:"show", id:purchaseOrder.id, params:params
  }

  def cancelPurchaseOrder(PurchaseOrder order){
    order.status = PurchaseOrderStatus.CANCELADA
    order.save flush:true
    emailSenderService.notifyPurchaseOrderChangeStatus(order)

    if (order.isMoneyBackOrder) {
      redirect action:'listMoneyBackOrders', params:[status:'POR_AUTORIZAR']
    } else {
      redirect action:'list', params:[status:'POR_AUTORIZAR']
    }
  }

  def chooseProviderForPurchase(){
    Company company = Company.get(session.company)
    def providers = getBusinessEntitiesToChoose(params, company)
    def provider = BusinessEntity.get(params.id)
    params.company = company
    render view:'create',model:([purchaseOrder:new PurchaseOrder(isMoneyBackOrder:new Boolean(params.isMoneyBackOrder)), company:company, providers:providers, provider:provider] + params)
  }

  def confirmThePurchaseOrder(PurchaseOrder order){
    def user = springSecurityService.currentUser
    purchaseOrderService.addAuthorizationToPurchaseOrder(order, user)
    if (purchaseOrderService.isFullAuthorized(order)){
      purchaseOrderService.authorizePurchaseOrder(order)
    }
    if (order.isMoneyBackOrder) {
      redirect action:'listMoneyBackOrders', params:[status:'POR_AUTORIZAR']
    } else {
      redirect action:'list', params:[status:'POR_AUTORIZAR']
    }
  }

  def create() {
    Company company = Company.get(session.company.toLong())
    respond new PurchaseOrder(params), model:[company:company]
  }

  def createMoneyBackOrder(){
    Company company = Company.get(session.company)
    PurchaseOrder order = new PurchaseOrder()
    order.isMoneyBackOrder = true
    render view:'create', model:[purchaseOrder: order, company:company]
  }

  def createWithInvoice() {

  }

  @Transactional
  def delete(PurchaseOrder purchaseOrder) {

    if (purchaseOrder == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    purchaseOrder.delete flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'purchaseOrder.label', default: 'PurchaseOrder'), purchaseOrder.id])
        redirect action:"index", method:"GET"
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  def deleteOrder(PurchaseOrder purchaseOrder) {
    Boolean isMoneyBackOrder = purchaseOrder.isMoneyBackOrder
    purchaseOrder.delete flush:true
    if (isMoneyBackOrder) {
      redirect action:'listMoneyBackOrders'
    } else {
      redirect action:'list'
    }
  }

  def edit(PurchaseOrder purchaseOrder) {
    respond purchaseOrder
  }

  def executePurchaseOrder(PurchaseOrder order) {
    BigDecimal amount = new BigDecimal(params.amount ?: 0) ?: order.total
		SourcePayment sourcePayment = SourcePayment."${params.source}"
    Boolean enabledToPay = companyService.companyIsEnabledToPay(order.company)
    if (purchaseOrderService.amountExceedsTotal(amount, order)) {
      render view:'show', model:[purchaseOrder:order, amountExcceds: "El monto excede el total del pago de la order de compra", enabledToPay:enabledToPay]
      return
    }
    String messageSuccess = message(code:"purchaseOrder.already.executed")
    if (sourcePayment == SourcePayment.BANKING || companyService.enoughBalanceCompany(order.company, amount)){
      if (purchaseOrderIsInStatus(order, PurchaseOrderStatus.AUTORIZADA)) {
				Map paymentData = [amount:amount, sourcePayment:sourcePayment]
        purchaseOrderService.payPurchaseOrder(order, paymentData)
        emailSenderService.notifyPaymentToPurchaseOrder(order)
        messageSuccess = message(code:"purchaseOrder.executed.message")
      }
      if (order.isMoneyBackOrder)
        redirect action:'listMoneyBackOrders', params:[status:PurchaseOrderStatus.AUTORIZADA, messageSuccess:messageSuccess]
      else
        redirect action:'list', params:[status:PurchaseOrderStatus.AUTORIZADA, messageSuccess:messageSuccess]

    } else {
      String insufficientBalance = message(code:'company.insufficient.balance')
      render view:'show', model:[purchaseOrder:order, insufficientBalance:insufficientBalance, enabledToPay:enabledToPay]
    }

  }

  private Boolean purchaseOrderIsInStatus(PurchaseOrder order, def statusExpected) {
    order.status == statusExpected
  }

  List<BusinessEntity> getBusinessEntitiesToChoose(def params, Company company){
    List<BusinessEntity> providers = businessEntityService.findBusinessEntityByKeyword(params.q, "PROVIDER", company)
    List<BusinessEntity> employees = businessEntityService.findBusinessEntityByKeyword(params.q, "EMPLOYEE", company)
    if (new Boolean(params.isMoneyBackOrder)){
      return employees
    }
    providers+=employees
  }

  def index() {
    def roles = springSecurityService.getPrincipal().getAuthorities()
    def company = Company.findById(session.company.toLong())
    respond PurchaseOrder.findAllByCompany(company), model:[company:company]
  }

  def list() {
    params.max = params.max ?: 25
    params.sort = "dateCreated"
    params.order = "desc"
    def purchaseOrders = purchaseOrderService.getPurchaseOrdersToList(session.company ? session.company.toLong() : session.company, params)

    [purchaseOrder: purchaseOrders.list, purchaseOrderCount: purchaseOrders.items, messageSuccess:params.messageSuccess]
  }

  def listMoneyBackOrders(){
    params.max = params.max ?: 25
    params.sort = "dateCreated"
    params.order = "desc"
    def purchaseOrders = purchaseOrderService.getMoneyBackOrdersToList(session.company ? session.company.toLong() : session.company, params)

    render view:'list', model:[purchaseOrder: purchaseOrders.list, purchaseOrderCount: purchaseOrders.items, isMoneyBackOrder:true, messageSuccess:params.messageSuccess]
  }

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'purchaseOrder.label', default: 'PurchaseOrder'), params.id])
        redirect action: "index", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }

  def rejectPurchaseOrder(PurchaseOrder order){
    order.status = PurchaseOrderStatus.RECHAZADA
    order.save flush:true
    emailSenderService.notifyPurchaseOrderChangeStatus(order)

    if (order.isMoneyBackOrder) {
      redirect action:'listMoneyBackOrders', params:[status:'AUTORIZADA']
    } else {
      redirect action:'list', params:[status:'AUTORIZADA']
    }
  }

  def save() {
    if(params.orderType == "1"){
      redirect controller:"feesReceipt", action:"create", params: [businessEntity: params.providerId, bankAccount: params.bankAccountId, company:params.company]
    } else {
      def purchaseOrder = purchaseOrderService.createPurchaseOrder(session.company.toLong(), params)

      request.withFormat {
        form multipartForm {
          flash.message = purchaseOrder.isMoneyBackOrder ? message(code: 'moneyBackOrder.created', args: [:]) : message(code: 'purchaseOrder.created', args: [:])
          redirect action:"show", id: purchaseOrder.id, params:params
        }
        '*' { respond purchaseOrder, [status: CREATED] }
      }
    }
  }

  def saveInvoiceTmp(){
    def purchaseOrderInvoice = purchaseOrderService.createPurchaseOrderByInvoice(flash.dataInvoice,session.company.toLong())
    purchaseOrderService.createPurchaseOrderItemsByInvoice(flash.dataInvoice, purchaseOrderInvoice)
    redirect action:"list"
  }

  def searchProviderForPurchase(){
    Company company = Company.get(session.company)
    String notFoundBusinessEntity = new Boolean(params.isMoneyBackOrder)?"No se encontró el emp/colaborador":"No se encontró el proveedor"
    List<BusinessEntity> providers = getBusinessEntitiesToChoose(params, company)
    if(providers.isEmpty()){
      flash.message = notFoundBusinessEntity
    }
    PurchaseOrder order = new PurchaseOrder(isMoneyBackOrder:new Boolean(params.isMoneyBackOrder))
    render view:'create',model:( [purchaseOrder:order, company:company, providers:providers] + params)
  }

  def sendOrderToConfirmation(PurchaseOrder purchaseOrder){
    purchaseOrderService.requestAuthorizationForTheOrder(purchaseOrder)
    if (purchaseOrder.isMoneyBackOrder) {
      redirect action:'listMoneyBackOrders'
    } else {
      redirect action:'list'
    }
  }

  def sendInvoice(){
    def invoice = request.getFile('file') ?: params."file[0]"
    flash.invoiceFile = invoice
    def responsePOST = purchaseOrderService.callExternalServiceForInvoice(invoice)

    params.remove("file[0]")
    flash.dataInvoice = responsePOST
    //TODO Esto se soluciona con un wrapper o parse a una entidad
    responsePOST[0].descuento = responsePOST[0].isNull("descuento") ? "" : responsePOST[0].descuento
    responsePOST[0].serie = responsePOST[0].isNull("serie") ? "" : responsePOST[0].serie
    def isAExistentProvirder = businessEntityService.findBusinessEntityAndProviderLinkByRFC(responsePOST.emisor.rfc)
    def providerExistent
    def providerBankAccountAndAddress
    if (isAExistentProvirder == null)
      providerExistent = message(code:"businessEntity.provider.notExist")
    if (isAExistentProvirder)
      providerBankAccountAndAddress = validateBankAccountsAndAddressOfProvider(responsePOST.emisor.rfc)
    def invoiceInformationAndErrors = ["inf":responsePOST,"errors": providerExistent,"bank": providerBankAccountAndAddress]
    render invoiceInformationAndErrors as JSON
  }

  def show(PurchaseOrder purchaseOrder) {
    Boolean enableAddDocuments = purchaseOrderService.enableAddDocuments(purchaseOrder)
    respond purchaseOrder, model:[enableAddDocuments:enableAddDocuments, user:springSecurityService.currentUser, baseUrlDocuments:grailsApplication.config.grails.url.base.images, enabledToPay:companyService.companyIsEnabledToPay(purchaseOrder.company), unitTypes:UnitType.findAllByCompany(purchaseOrder.company)]
  }

  @Transactional
  def update(PurchaseOrder purchaseOrder) {
    if (purchaseOrder == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    if (purchaseOrder.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond purchaseOrder.errors, view:'edit'
      return
    }

    purchaseOrder.save flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.updated.message', args: [message(code: 'purchaseOrder.label', default: 'PurchaseOrder'), purchaseOrder.id])
        redirect purchaseOrder
      }
      '*'{ respond purchaseOrder, [status: OK] }
    }
  }

  def deleteItem(PurchaseOrderItem item) {
    Long idPurchaseOrder = item.purchaseOrder.id
    purchaseOrderItemService.deleteItemFromPurchaseOrder(item)
    redirect action:'show', id:idPurchaseOrder
  }

  private def validateBankAccountsAndAddressOfProvider(String rfc) {
    def thisBusinessEntityHaveAAccount = businessEntityService.knowIfBusinessEntityHaveABankAccountOrAddress(rfc)
    if (thisBusinessEntityHaveAAccount.flatten().size() < 2)
      message(code:"businessEntity.provider.not.bank.account.or.not.address")
  }

}
