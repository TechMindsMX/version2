package com.modulus.uno

import grails.transaction.Transactional
import java.math.RoundingMode

@Transactional
class PurchaseOrderService {

  def documentService
  def emailSenderService
  def modulusUnoService
  def restService
  def purchaseOrderDocumentsService

  def addAuthorizationToPurchaseOrder(PurchaseOrder order, User user) {
    def authorization = new Authorization(user:user)
    order.addToAuthorizations(authorization)
    order.save()
  }

  def addInvoiceToPurchaseOrder(def invoice, Long purchaseOrderId, String type){
    PurchaseOrder order = PurchaseOrder.get(purchaseOrderId)
    purchaseOrderDocumentsService.validateDocumentXmlForOrder(order, invoice)
    purchaseOrderDocumentsService.loadItemsToOrderFromDocumentXml(order, invoice)
    documentService.uploadDocumentForOrder(invoice,type,order)
  }

  def authorizePurchaseOrder(PurchaseOrder purchaseOrder){
    purchaseOrder.status = PurchaseOrderStatus.AUTORIZADA
    purchaseOrder.save()
    emailSenderService.notifyPurchaseOrderChangeStatus(purchaseOrder)
    purchaseOrder
  }

  def callExternalServiceForInvoice(def invoice) {
    File tmp = File.createTempFile("${new File(".").getCanonicalPath()}/${invoice.originalFilename}","")
    invoice.transferTo(tmp)
    def responsePOST = restService.getInvoiceData(tmp.bytes)
    tmp.deleteOnExit()
    responsePOST
  }

  def createPurchaseOrder(Long companyId, Map params) {
    Company company = Company.get(companyId)
    BusinessEntity businessEntity = BusinessEntity.get(params.providerId)
    BankAccount bankAccount = BankAccount.get(params.bankAccountId)
    def purchaseOrder = new PurchaseOrder(rfc:businessEntity.rfc, providerName: businessEntity.toString(), company:company, isMoneyBackOrder:params.isMoneyBackOrder, fechaPago:Date.parse("dd/MM/yyyy", params.fechaPago), externalId:params.externalId,note:params.note)
    if (params.orderType == "0")
      purchaseOrder.isAnticipated = true

    purchaseOrder.bankAccount = bankAccount
    purchaseOrder.status = PurchaseOrderStatus.CREADA
    purchaseOrder.save()
    purchaseOrder
  }

  private Boolean statusIsValidToAddDocuments(PurchaseOrder purchaseOrder) {
    Boolean isValid = purchaseOrder.status != PurchaseOrderStatus.CANCELADA &&
    purchaseOrder.status != PurchaseOrderStatus.RECHAZADA

    if (purchaseOrder.isAnticipated)
      isValid = isValid && purchaseOrder.status != PurchaseOrderStatus.CREADA

    isValid
  }

  private Boolean missingDocumentsByTypePurchaseOrder(PurchaseOrder purchaseOrder) {
    Integer maxDocuments = purchaseOrder.isMoneyBackOrder ? 3 : 2
    Integer currentDocuments = purchaseOrder.documents ? purchaseOrder.documents.size() : 0
    currentDocuments < maxDocuments
  }

  Boolean enableAddDocuments(PurchaseOrder purchaseOrder) {
    statusIsValidToAddDocuments(purchaseOrder) && missingDocumentsByTypePurchaseOrder(purchaseOrder) && purchaseOrder.items
  }

  def createPurchaseOrderByInvoice(def dataOfInvoice,def companyId) {
    def businessEntity = BusinessEntity.findByRfc(dataOfInvoice.emisor.rfc)
    def params= ["providerId":businessEntity.id,"bankAccountId":businessEntity.banksAccounts.first().id,"isMoneyBackOrder":false]
    createPurchaseOrder(companyId,params)
  }

  def createPurchaseOrderItemsByInvoice(def dataOfInvoice,PurchaseOrder purchaseOrder) {
    def itemList = dataOfInvoice.conceptos.first()
    itemList.each { item ->
      def command = new PurchaseOrderItemCommand()
      command.name = item.descripcion
      command.quantity = item.cantidad
      command.price = item.importe
      command.unitType = getUnitTypeByUnitOfConcept(item.unidad)
      def purchaseOrderItem = command.createPurchaseOrderItem()
      purchaseOrder.addToItems(purchaseOrderItem)
    }
    purchaseOrder.save()
  }

  def getPurchaseOrderStatus(String status){
    def listPurchaseOrderStatus = []
    listPurchaseOrderStatus = Arrays.asList(PurchaseOrderStatus.values())
    if (status){
      def listStatus = status.tokenize(",")
      listPurchaseOrderStatus = listStatus.collect { it as PurchaseOrderStatus }
    }

    listPurchaseOrderStatus
  }

  def getPurchaseOrdersToList(Long company, params){
    def statusOrders = getPurchaseOrderStatus(params.status)
    def purchaseOrders = [:]
    if (company){
      purchaseOrders.list = PurchaseOrder.findAllNotIsMoneyBackOrderByCompanyAndStatusInList(Company.get(company), statusOrders, params)
      purchaseOrders.items = PurchaseOrder.countByCompanyAndStatusInListAndIsMoneyBackOrder(Company.get(company), statusOrders, false)
    } else {
      purchaseOrders.list = PurchaseOrder.findAllNotIsMoneyBackOrderByStatusInList(statusOrders, params)
      purchaseOrders.items = PurchaseOrder.countByStatusInListAndIsMoneyBackOrder(statusOrders, false)
    }
    purchaseOrders
  }

  def getMoneyBackOrdersToList(Long company, params){
    def statusOrders = getPurchaseOrderStatus(params.status)
    def purchaseOrders = [:]
    if (company){
      purchaseOrders.list = PurchaseOrder.findAllIsMoneyBackOrderByCompanyAndStatusInList(Company.get(company), statusOrders, params)
      purchaseOrders.items = PurchaseOrder.countByCompanyAndStatusInListAndIsMoneyBackOrder(Company.get(company), statusOrders, true)
    } else {
      purchaseOrders.list = PurchaseOrder.findAllIsMoneyBackOrderByStatusInList(statusOrders, params)
      purchaseOrders.items = PurchaseOrder.countByStatusInListAndIsMoneyBackOrder(statusOrders, true)
    }
    purchaseOrders
  }


  def getTotalPurchaseOrderAuthorizedOfCompany(Company company){
    PurchaseOrder.findAllByCompanyAndStatus(company, PurchaseOrderStatus.AUTORIZADA).total.sum()
  }

  private def getUnitTypeByUnitOfConcept(String unit) {
    switch (unit){
      case {it =~ /[pzPZ]/}:
        return UnitType.UNIDADES
      break
      case {it =~/[KGkg]/}:
        return UnitType.KILOGRAMOS
      break
      case {it =~/Mm/}:
        return UnitType.METROS
      break
      case {it =~/Ll/}:
        return UnitType.LITROS
      break
      case {it =~/HRShrs/}:
        return UnitType.HORAS
      break
      default:
        return UnitType.UNIDADES
      break
    }
  }

  def isFullAuthorized(PurchaseOrder order){
    (order.authorizations?.size() ?: 0) >= order.company.numberOfAuthorizations
  }

  def payPurchaseOrder(PurchaseOrder order, Map paymentData){
		Transaction transaction = null
		if (paymentData.sourcePayment==SourcePayment.MODULUS_UNO){
    	transaction = modulusUnoService.payPurchaseOrder(order, paymentData.amount)
		}
		paymentData.transaction = transaction
    addingPaymentToPurchaseOrder(order, paymentData)
		
    if (order.total <= order.totalPayments) {
      order.status = PurchaseOrderStatus.PAGADA
      order.save()
      emailSenderService.notifyPurchaseOrderChangeStatus(order)
    }
    order
  }

  def requestAuthorizationForTheOrder(PurchaseOrder purchaseOrder){
    purchaseOrder.status = PurchaseOrderStatus.POR_AUTORIZAR
    purchaseOrder.save()
    emailSenderService.notifyPurchaseOrderChangeStatus(purchaseOrder)
    purchaseOrder
 }

  def validateDocument(PurchaseOrder order, def document){
    String ext = document.filename.tokenize('.').last().toLowerCase()
    boolean valid = true
    if ((!order.isMoneyBackOrder && !ext.equals("pdf") && !ext.equals("xml"))
        || (order.isMoneyBackOrder && !ext.equals("pdf") && !ext.equals("xml") && !ext.equals("png") && !ext.equals("jpg")))
      valid = false
    else if (order.documents){
      order.documents.each(){
        if ((it.mimeType.equals(ext) && !order.isMoneyBackOrder) ||
            (order.isMoneyBackOrder && ((it.mimeType.equals("png") && ext.equals("jpg"))
            || (it.mimeType.equals("jpg") && ext.equals("png"))
            || (!ext.equals("pdf") && it.mimeType.equals(ext)))
            )
        )
          valid = false
      }
    }
    valid
  }

  def updateDatePaymentForOrder(Long id, Date paymentDate) {
    PurchaseOrder purchaseOrder = PurchaseOrder.get(id)
    if (!purchaseOrder.originalDate)
      purchaseOrder.originalDate = purchaseOrder.fechaPago
    purchaseOrder.fechaPago = paymentDate
    purchaseOrder.save()
    purchaseOrder
  }

  def addingPaymentToPurchaseOrder(PurchaseOrder purchaseOrder, Map paymentData) {
    PaymentToPurchase payment = new PaymentToPurchase(amount:paymentData.amount, transaction:paymentData.transaction, source:paymentData.sourcePayment)
    purchaseOrder.addToPayments(payment)
    purchaseOrder.save()
    purchaseOrder
  }

  Boolean amountExceedsTotal(def amount, PurchaseOrder order) {
    amount.setScale(2, RoundingMode.HALF_UP) > order.amountToPay.setScale(2, RoundingMode.HALF_UP)
  }

  def reversePaymentPurchaseForTransaction(Transaction transaction) {
    PaymentToPurchase payment = PaymentToPurchase.findByTransaction(transaction)
    if (payment) {
      payment.status = PaymentToPurchaseStatus.REFUND
      payment.save()
      checkStatusForPurchaseOrderOfPayment(payment)
    }
  }

  def checkStatusForPurchaseOrderOfPayment(PaymentToPurchase payment) {
    def c = PurchaseOrder.createCriteria()
    def order = c.get {
      payments {
        eq("id", payment.id)
      }
    }
    if (order.status == PurchaseOrderStatus.PAGADA) {
      order.status = PurchaseOrderStatus.AUTORIZADA
      order.save()
    }
  }

  List<PaymentToPurchase> findBankingPaymentsToPurchaseToConciliateForCompany(Company company) {
		def purchaseOrdersCompany = PurchaseOrder.findAllByCompanyAndStatusInList(company, [PurchaseOrderStatus.AUTORIZADA, PurchaseOrderStatus.PAGADA])
		List<PaymentToPurchase> payments = []
		purchaseOrdersCompany.each { purchase ->
			def bankingPayments = purchase.payments.findAll {it.source==SourcePayment.BANKING && it.status==PaymentToPurchaseStatus.APPLIED}
			payments.addAll(bankingPayments)
		}
		payments
  }

	PurchaseOrder getPurchaseOrderOfPaymentToPurchase(PaymentToPurchase payment){
		def poc = PurchaseOrder.createCriteria()
		def purchase = poc.get {
			payments {
				eq('id', payment.id)
			}
		}
	}

	def conciliatePaymentToPurchase(PaymentToPurchase payment) {
		payment.status = PaymentToPurchaseStatus.CONCILIATED
		payment.save()
	} 
}
