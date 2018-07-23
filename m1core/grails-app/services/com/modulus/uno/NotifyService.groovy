package com.modulus.uno

import grails.transaction.Transactional
import java.math.RoundingMode
import com.modulus.uno.saleorder.SaleOrder
import com.modulus.uno.status.SaleOrderStatus

@Transactional
class NotifyService {

  def restService
  def grailsApplication
  def corporateService

  def prepareParametersToSendForSaleOrder(SaleOrder saleOrder, status){
    def paramsMap = [:]
    def paramsFields
    def orderStatus
    switch(status){
      case SaleOrderStatus.CREADA:
      paramsFields=["id", "clientName", "rfc"]
      orderStatus="CREADA"
      break
      case SaleOrderStatus.POR_AUTORIZAR:
      paramsFields=["id", "clientName", "rfc"]
      orderStatus="PUESTA EN ESPERA DE SER AUTORIZADA"
      break
      case SaleOrderStatus.AUTORIZADA:
      paramsFields=["id", "clientName", "rfc"]
      orderStatus="AUTORIZADA"
      break
      case SaleOrderStatus.RECHAZADA:
      paramsFields=["id", "clientName", "rfc", "rejectReason", "comments"]
      orderStatus="RECHAZADA"
      break
      case SaleOrderStatus.PAGADA:
      paramsFields=["id", "clientName", "rfc"]
      orderStatus="PAGADA"
      break
      case SaleOrderStatus.EJECUTADA:
      paramsFields=["id", "clientName", "rfc"]
      orderStatus="EJECUTADA"
      break
      case SaleOrderStatus.CANCELADA:
      paramsFields=["id", "clientName", "rfc", "rejectReason", "comments"]
      orderStatus="CANCELADA"
      break
      case SaleOrderStatus.CANCELACION_POR_AUTORIZAR:
      paramsFields=["id", "clientName", "rfc", "rejectReason", "comments"]
      orderStatus="CANCELADA POR AUTORIZAR"
      break
      case SaleOrderStatus.CANCELACION_AUTORIZADA:
      paramsFields=["id", "clientName", "rfc", "rejectReason", "comments"]
      orderStatus="CANCELACION AUTORIZADA"
      break
      case SaleOrderStatus.CANCELACION_EJECUTADA:
      paramsFields=["id", "clientName", "rfc", "rejectReason", "comments"]
      orderStatus="CANCELACION EJECUTADA"
      break
    }
    paramsMap = buildParamsEmailMap(saleOrder, paramsFields)
    paramsMap.status = orderStatus
    paramsMap.url=corporateService.findCorporateByCompanyId(saleOrder.company.id)
    paramsMap
  }

  def parametersForPurchaseOrder(PurchaseOrder purchaseOrder, def status){
    def paramsMap = [:]
    def paramsFields
    def orderStatus
    switch(status){
      case PurchaseOrderStatus.CREADA:
      paramsFields=["id", "providerName"]
      orderStatus="CREADA"
      break;
      case PurchaseOrderStatus.POR_AUTORIZAR:
      paramsFields=["id", "providerName"]
      orderStatus="PUESTA EN ESPERA DE SER AUTORIZADA"
      break;
      case PurchaseOrderStatus.AUTORIZADA:
      paramsFields=["id", "providerName"]
      orderStatus="AUTORIZADA"
      break;
      case PurchaseOrderStatus.RECHAZADA:
      paramsFields=["id", "providerName", "rejectReason", "rejectComment"]
      orderStatus="RECHAZADA"
      break;
      case PurchaseOrderStatus.PAGADA:
      paramsFields=["id", "providerName", "rejectReason", "rejectComment"]
      orderStatus="PAGADA"
      break;
      case PurchaseOrderStatus.CANCELADA:
      paramsFields=["id", "providerName", "rejectReason", "rejectComment"]
      orderStatus="CANCELADA"
      break;
    }
    paramsMap = buildParamsEmailMap(purchaseOrder, paramsFields)
    paramsMap.status = orderStatus
    paramsMap.url=corporateService.findCorporateByCompanyId(purchaseOrder.company.id)
    paramsMap
  }

  def parametersForLoanOrder(LoanOrder loanOrder, status){
    def paramsMap = [:]
    def paramsFields
    def orderStatus
    switch(status){
      case LoanOrderStatus.CREATED:
      paramsFields = ["id","amount"]
      orderStatus = "CREADA"
      break
      case LoanOrderStatus.VALIDATE:
      paramsFields = ["id","amount"]
      orderStatus = "PUESTA EN ESPERA DE SER AUTORIZADA"
      break
      case LoanOrderStatus.AUTHORIZED:
      paramsFields = ["id","amount"]
      orderStatus = "AUTORIZADA"
      break
      case LoanOrderStatus.EXECUTED:
      paramsFields = ["id","amount"]
      orderStatus = "EJECUTADA"
      break
      case LoanOrderStatus.APPROVED:
      paramsFields = ["id","amount"]
      orderStatus = "APROBADA"
      break
      case LoanOrderStatus.ACCEPTED:
      paramsFields = ["id","amount"]
      orderStatus = "ACEPTADA"
      break
      case LoanOrderStatus.REJECTED:
      paramsFields = ["id","amount","rejectComment", "rejectReason"]
      orderStatus = "RECHAZADA"
      break
      case LoanOrderStatus.CANCELED:
      paramsFields = ["id","amount","rejectComment", "rejectReason"]
      orderStatus = "CANCELADA"
      break
    }
    paramsMap = buildParamsEmailMap(loanOrder, paramsFields)
    paramsMap.status = orderStatus
    paramsMap.url=corporateService.findCorporateByCompanyId(loanOrder.company.id)
    paramsMap
  }

  def parametersForCashOutOrder(CashOutOrder cashOutOrder, status){
    def paramsMap = [:]
    def paramsFields
    def orderStatus
    switch(status){
      case CashOutOrderStatus.CREATED:
      paramsFields = ["id","amount"]
      orderStatus = "CREADA"
      break
      case CashOutOrderStatus.IN_PROCESS:
      paramsFields = ["id","amount"]
      orderStatus = "EN PROCESO"
      break
      case CashOutOrderStatus.TO_AUTHORIZED:
      paramsFields = ["id","amount"]
      orderStatus = "PUESTA EN ESPERA DE SER AUTORIZADA"
      break
      case CashOutOrderStatus.AUTHORIZED:
      paramsFields = ["id","amount"]
      orderStatus = "AUTORIZADA"
      break
      case CashOutOrderStatus.EXECUTED:
      paramsFields = ["id","amount"]
      orderStatus = "EJECUTADA"
      break
      case CashOutOrderStatus.REJECTED:
      paramsFields = ["id","amount", "comments", "rejectReason"]
      orderStatus = "RECHAZADA"
      break
      case CashOutOrderStatus.CANCELED:
      paramsFields = ["id","amount", "comments", "rejectReason"]
      orderStatus = "CANCELADA"
      break
    }
    paramsMap = buildParamsEmailMap(cashOutOrder, paramsFields)
    paramsMap.status = orderStatus
    paramsMap.url=corporateService.findCorporateByCompanyId(cashOutOrder.company.id)
    if (status == CashOutOrderStatus.EXECUTED) {
      paramsMap = defineExtraParamsForPayedCashoutOrderTemplate(cashOutOrder, paramsMap)
    }
    paramsMap
  }

  def defineExtraParamsForPayedCashoutOrderTemplate(CashOutOrder cashOutOrder, def paramsMap) {
    paramsMap.paymentConcept = cashOutOrder.transaction.paymentConcept
    paramsMap.trackingKey = cashOutOrder.transaction.trackingKey
    paramsMap.referenceNumber = cashOutOrder.transaction.referenceNumber
    paramsMap.dateCreated = cashOutOrder.transaction.dateCreated.format("dd-MM-yyyy hh:mm:ss")
    paramsMap.destinyBank = cashOutOrder.account.banco.name
    paramsMap.destinyBankAccount = cashOutOrder.account.clabe
    paramsMap.aliasStp = cashOutOrder.company.accounts.first().aliasStp
    paramsMap
  }

  def parametersForLoanPaymentOrder(LoanPaymentOrder loanPaymentOrder, status){
    def paramsMap = [:]
    def paramsFields
    def orderStatus
    switch(status){
      case LoanPaymentOrderStatus.CREATED:
      paramsFields = ["id","amountInterest", "amountIvaInterest", "amountToCapital"]
      orderStatus = "CREADA"
      break
      case LoanPaymentOrderStatus.VALIDATE:
      paramsFields = ["id","amountInterest", "amountIvaInterest", "amountToCapital"]
      orderStatus = "PUESTA EN ESPERA DE SER AUTORIZADA"
      break
      case LoanPaymentOrderStatus.AUTHORIZED:
      paramsFields = ["id","amountInterest", "amountIvaInterest", "amountToCapital"]
      orderStatus = "AUTORIZADA"
      break
      case LoanPaymentOrderStatus.REJECTED:
      paramsFields = ["id","amountInterest", "amountIvaInterest", "amountToCapital", "rejectComment", "rejectReason"]
      orderStatus = "RECHAZADA"
      break
      case LoanPaymentOrderStatus.EXECUTED:
      paramsFields = ["id","amountInterest", "amountIvaInterest", "amountToCapital"]
      orderStatus = "EJECUTADA"
      break
      case LoanPaymentOrderStatus.CANCELED:
      paramsFields = ["id","amountInterest", "amountIvaInterest", "amountToCapital", "rejectComment", "rejectReason"]
      orderStatus = "CANCELADA"
      break
    }
    paramsMap = buildParamsEmailMap(loanPaymentOrder, paramsFields)
    paramsMap.status = orderStatus
    paramsMap.url=corporateService.findCorporateByCompanyId(loanPaymentOrder.company.id)
    paramsMap

  }

  def parametersForFeesReceipt(FeesReceipt feesReceipt, status, Company company){
    def paramsMap = [:]
    def paramsFields
    def orderStatus
    switch(status){
      case FeesReceiptStatus.CREADA:
      paramsFields=['id']
      orderStatus= "CREADA"
      break
      case FeesReceiptStatus.POR_AUTORIZAR:
      paramsFields=['id']
      orderStatus= "PUESTA EN ESPERA DE SER AUTORIZADA"
      break
      case FeesReceiptStatus.AUTORIZADA:
      paramsFields=['id']
      orderStatus= "AUTORIZADA"
      break
      case FeesReceiptStatus.EJECUTADA:
      paramsFields=['id', 'collaboratorName']
      orderStatus= "EJECUTADA"
      break
      case FeesReceiptStatus.CANCELADA:
      paramsFields=['id', 'rejectReason', 'comments']
      orderStatus= "CANCELADA"
      break
      case FeesReceiptStatus.RECHAZADA:
      paramsFields=['id', 'rejectReason', 'comments']
      orderStatus= "RECHAZADA"
      break
    }
    paramsMap = buildParamsEmailMap(feesReceipt, paramsFields)
    paramsMap.status=orderStatus
    paramsMap.company=company.toString()
    paramsMap.url=corporateService.findCorporateByCompanyId(feesReceipt.company.id)
    if (status == FeesReceiptStatus.EJECUTADA) {
      paramsMap = defineExtraParamsForPayedFeesReceiptTemplate(feesReceipt, paramsMap)
    }
    paramsMap
  }

  def defineExtraParamsForPayedFeesReceiptTemplate(FeesReceipt feesReceipt, def paramsMap) {
    paramsMap.amount = feesReceipt.netAmount.setScale(2, RoundingMode.HALF_UP).toString()
    paramsMap.paymentConcept = feesReceipt.transaction.paymentConcept
    paramsMap.trackingKey = feesReceipt.transaction.trackingKey
    paramsMap.referenceNumber = feesReceipt.transaction.referenceNumber
    paramsMap.dateCreated = feesReceipt.transaction.dateCreated.format("dd-MM-yyyy hh:mm:ss")
    paramsMap.destinyBank = feesReceipt.bankAccount.banco.name
    paramsMap.destinyBankAccount = feesReceipt.bankAccount.clabe
    paramsMap.aliasStp = feesReceipt.company.accounts.first().aliasStp
    paramsMap
  }

  def parametersForBusinessEntity(def businessEntity, Company company){
    def paramsFields = ['id', 'rfc']
    def paramsMap = buildParamsEmailMap(businessEntity, paramsFields)
    paramsMap.company=company.toString()
    paramsMap.url=corporateService.findCorporateByCompanyId(company.id)
    paramsMap
  }

  def parametersForRecoveryToken(def message){
    def paramsMap = ['token': message.token]
    paramsMap
  }

  def parametersForConfirmUser(User user){
    def paramsMap = ['user': user.username]
  }

  def parametersForStpDeposit(Payment payment){
    def paramsMap = [:]
    def paramsFields = ["paymentConcept", "trackingKey", "referenceNumber"]
    paramsMap = buildParamsEmailMap(payment.transaction, paramsFields)
    paramsMap.amount = payment.amount.toString()
    paramsMap.dateCreated = payment.dateCreated.format("dd-MM-yyyy hh:mm:ss")
    paramsMap.company = payment.rfc ? BusinessEntity.findByRfc(payment.rfc).toString() : "NO IDENTIFICADO"
    paramsMap.url=corporateService.findCorporateByCompanyId(payment.company.id)
    paramsMap
  }

  def parametersForPaymentToPurchase(PurchaseOrder purchaseOrder){
    PaymentToPurchase payment = purchaseOrder.payments.sort{it.id}.last()
    def paramsMap = [:]
		if (payment.transaction) {
    	def paramsFields = ["paymentConcept", "trackingKey", "referenceNumber"]
    	paramsMap = buildParamsEmailMap(payment.transaction, paramsFields)
		} else {
			paramsMap.paymentConcept = "Pago Bancario"
			paramsMap.trackingKey = ""
			paramsMap.referenceNumber = ""
		}
    paramsMap.amount = payment.amount.toString()
    paramsMap.dateCreated = payment.dateCreated.format("dd-MM-yyyy hh:mm:ss")
    paramsMap.providerName = purchaseOrder.providerName
    paramsMap.id = purchaseOrder.id
    paramsMap.destinyBank = purchaseOrder.bankAccount.banco.name
    paramsMap.destinyBankAccount = purchaseOrder.bankAccount.clabe
    paramsMap.aliasStp = purchaseOrder.company.accounts.first().aliasStp
    paramsMap.url=corporateService.findCorporateByCompanyId(purchaseOrder.company.id)
    paramsMap
  }

  def sendEmailToGroup(ArrayList<NotificationForState> notifys, def emailerParams){
    notifys.each{ notify ->
      GroupNotification group = GroupNotification.findById(notify.groupNotification)
        group.users.each{ user ->
          log.info "Sending email to ${user.profile.email}"
          sendNotify(buildEmailerMap(group.notificationId, user.profile.email, emailerParams))
        }
    }
  }

  //Send a email notification
  def sendEmailNotifications(def emailsToNotify, String idTemplate, def paramsMap){
    emailsToNotify.each{ email ->
      sendNotify(buildEmailerMap(idTemplate, email, paramsMap))
    }
  }

  def buildEmailerMap(String idEmailer, String toSend, def params){
    def emailerMap = [
    'id': idEmailer,
    'to': toSend,
    'subject': 'Mensaje de Modulus Uno',
    'params': params
    ]
    emailerMap
  }

  private buildParamsEmailMap(def order, def fieldsEmail){
    def emailParamsMap=[:]
    fieldsEmail.each{ p -> emailParamsMap."$p" = order."$p" ? order."$p".toString() : ""}
    emailParamsMap
  }

  private def sendNotify(def arguments){
    restService.sendEmailToEmailer(arguments)
  }
}
