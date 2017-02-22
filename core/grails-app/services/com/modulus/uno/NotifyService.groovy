package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class NotifyService {

  def restService
  def grailsApplication

  def parametersForDepositOrder(DepositOrder depositOrder, def status){
    def paramsMap = [:]
    def paramsFields
    def orderStatus
    switch(status){
      case DepositOrderStatus.CREATED:
      paramsFields=["id", "amount"]
      orderStatus="CREADA"
      break
      case DepositOrderStatus.VALIDATE:
      paramsFields=["id", "amount"]
      orderStatus="PUESTA EN ESPERA DE SER AUTORIZADA"
      break
      case DepositOrderStatus.AUTHORIZED:
      paramsFields=["id", "amount"]
      orderStatus="AUTORIZADA"
      break
      case DepositOrderStatus.REJECTED:
      paramsFields=["id", "amount", "rejectComment", "rejectReason"]
      orderStatus="RECHAZADA"
      break
      case DepositOrderStatus.EXECUTED:
      paramsFields=["id", "amount"]
      orderStatus="EJECUTADA"
      break
      case DepositOrderStatus.CANCELED:
      paramsFields=["id", "amount", "rejectComment", "rejectReason"]
      orderStatus="CANCELADA"
      break
      case DepositOrderStatus.CONCILIATED:
      paramsFields=["id", "amount"]
      orderStatus="CONCILIADA"
      break
    }
    paramsMap = buildParamsEmailMap(depositOrder, paramsFields)
    paramsMap.status = orderStatus
    paramsMap.url=grailsApplication.config.grails.server.url
    paramsMap
  }

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
    paramsMap.url=grailsApplication.config.grails.server.url
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
    paramsMap.url=grailsApplication.config.grails.server.url
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
    paramsMap.url=grailsApplication.config.grails.server.url
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
    paramsMap.url=grailsApplication.config.grails.server.url
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
    paramsMap.url=grailsApplication.config.grails.server.url
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
      paramsFields=['id']
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
    paramsMap.url=grailsApplication.config.grails.server.url
    paramsMap
  }

  def parametersForBusinessEntity(def businessEntity, Company company){
    def paramsFields = ['id', 'rfc']
    def paramsMap = buildParamsEmailMap(businessEntity, paramsFields)
    paramsMap.company=company.toString()
    paramsMap.url=grailsApplication.config.grails.server.url
    paramsMap
  }

  def parametersForRecoveryToken(def message){
    def paramsMap = ['token': message.token]
    paramsMap
  }

  def parametersForConfirmUser(User user){
    def paramsMap = ['user': user.username]
  }


  def sendEmailNotifications(def usersToNotify, String idTemplate, def paramsMap){
    usersToNotify.each{ user ->
      sendNotify(buildEmailerMap(idTemplate, user, paramsMap))
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
    fieldsEmail.each{ p -> emailParamsMap."$p" = order."$p"}
    emailParamsMap
  }

  private def sendNotify(def arguments){
    restService.sendEmailToEmailer(arguments)
  }
}
