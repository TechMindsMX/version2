package com.modulus.uno

import grails.transaction.Transactional

import com.modulus.uno.saleorder.SaleOrder
import com.modulus.uno.status.SaleOrderStatus

@Transactional(readOnly=true)
class EmailSenderService {

  def grailsApplication
  def notifyService
  DirectorService directorService

  //RecoveryService
  def sendEmailForConfirmAccount(def message, String email){
    def idEmailer=grailsApplication.config.emailer.recoveryConfirm
    def paramsEmailer=notifyService.parametersForRecoveryToken(message)
    notifyService.sendEmailNotifications([email], idEmailer, paramsEmailer)
  }

  def sendEmailForConfirmAccountForToken(User user){
    def idEmailer=grailsApplication.config.emailer.recoveryToken
    def paramsEmailer=notifyService.parametersForConfirmUser(user)
    notifyService.sendEmailNotifications([user.profile.email], idEmailer, paramsEmailer)
  }

  def sendEmailForRegistrationCode(def message, String email){
    def idEmailer=grailsApplication.config.emailer.recoveryRegistrationCode
    def paramsEmailer=notifyService.parametersForRecoveryToken(message)
    notifyService.sendEmailNotifications([email], idEmailer, paramsEmailer)
  }

  //EmployeeServ
  def sendEmailForNewEmployee(Company company, def employee){
    def paramsEmailer=notifyService.parametersForBusinessEntity(employee, company)
    def emailList = getEmailList(company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
    notifyService.sendEmailNotifications(emailList, grailsApplication.config.emailer.newEmployee, paramsEmailer)
  }

  //Provider
  def sendEmailForNewProvider(Company company, def provider){
    def paramsEmailer=notifyService.parametersForBusinessEntity(provider, company)
    def emailList = getEmailList(company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
    notifyService.sendEmailNotifications(emailList, grailsApplication.config.emailer.newProvider, paramsEmailer)
  }

  //Client
  def sendEmailForNewClient(Company company, def client){
    def paramsEmailer=notifyService.parametersForBusinessEntity(client, company)
    def emailList = getEmailList(company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
    notifyService.sendEmailNotifications(emailList, grailsApplication.config.emailer.newClient, paramsEmailer)
  }

  //SaleOrder, CREADA:Sin correo
  def notifySaleOrderChangeStatus(SaleOrder order){
    def emailList
    def idEmailer
    def paramsEmailer=notifyService.prepareParametersToSendForSaleOrder(order, order.status)
    switch(order.status){
      //case SaleOrderStatus.CREADA:
      //break
      case SaleOrderStatus.POR_AUTORIZAR:
      idEmailer = grailsApplication.config.emailer.saleOrderAcceptStatus
      emailList = getEmailList(order.company,["ROLE_AUTHORIZER_VISOR", "ROLE_AUTHORIZER_EJECUTOR"])
      break
      case SaleOrderStatus.AUTORIZADA:
      idEmailer = grailsApplication.config.emailer.saleOrderAcceptStatus
      emailList = getEmailList(order.company,["ROLE_FICO_VISOR", "ROLE_FICO_EJECUTOR"])
      break
      case SaleOrderStatus.RECHAZADA:
      idEmailer = grailsApplication.config.emailer.saleOrderCancelStatus
      emailList = getEmailList(order.company,["ROLE_OPERATOR_VISOR", "ROLE_OPERATOR_EJECUTOR"])
      break
      case SaleOrderStatus.PAGADA:
      idEmailer = grailsApplication.config.emailer.saleOrderAcceptStatus
      emailList = getEmailList(order.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
      break
      case SaleOrderStatus.EJECUTADA:
      idEmailer = grailsApplication.config.emailer.saleOrderAcceptStatus
      emailList = getEmailList(order.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
      break
      case SaleOrderStatus.CANCELADA:
      idEmailer = grailsApplication.config.emailer.saleOrderCancelStatus
      emailList = getEmailList(order.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
      break
      case SaleOrderStatus.CANCELACION_POR_AUTORIZAR:
      idEmailer = grailsApplication.config.emailer.saleOrderCancelStatus
      emailList = getEmailList(order.company,["ROLE_AUTHORIZER_VISOR", "ROLE_AUTHORIZER_EJECUTOR"])
      break
      case SaleOrderStatus.CANCELACION_AUTORIZADA:
      idEmailer = grailsApplication.config.emailer.saleOrderCancelStatus
      emailList = getEmailList(order.company,["ROLE_FICO_VISOR", "ROLE_FICO_EJECUTOR"])
      break
      case SaleOrderStatus.CANCELACION_EJECUTADA:
      idEmailer = grailsApplication.config.emailer.saleOrderCancelStatus
      emailList = getEmailList(order.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
      break
    }
    notifyService.sendEmailNotifications(emailList, idEmailer, paramsEmailer)
  }

  //CashOut
  def notifyCashOutOrderChangeStatus(CashOutOrder order){
    def idEmailer
    def emailList
    def paramsEmailer=notifyService.parametersForCashOutOrder(order, order.status)
    switch(order.status){
      //case CashOutOrderStatus.CREATED:
      //break
      //case CashOutOrderStatus.IN_PROCESS:
      //break
      case CashOutOrderStatus.TO_AUTHORIZED:
      idEmailer = grailsApplication.config.emailer.cashOutOrderAcceptStatus
      emailList = getEmailList(order.company,["ROLE_AUTHORIZER_VISOR", "ROLE_AUTHORIZER_EJECUTOR"])
      break
      case CashOutOrderStatus.AUTHORIZED:
      idEmailer = grailsApplication.config.emailer.cashOutOrderAcceptStatus
      emailList = getEmailList(order.company,["ROLE_FICO_VISOR", "ROLE_FICO_EJECUTOR"])
      break
      case CashOutOrderStatus.EXECUTED:
      idEmailer = grailsApplication.config.emailer.cashOutOrderPayed
      emailList = getEmailList(order.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
      break
      case CashOutOrderStatus.REJECTED:
      idEmailer = grailsApplication.config.emailer.cashOutOrderCancelStatus
      emailList = getEmailList(order.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
      break
      case CashOutOrderStatus.CANCELED:
      idEmailer = grailsApplication.config.emailer.cashOutOrderCancelStatus
      emailList = getEmailList(order.company,["ROLE_OPERATOR_VISOR", "ROLE_OPERATOR_EJECUTOR"])
      break
    }
    notifyService.sendEmailNotifications(emailList, idEmailer, paramsEmailer)
  }

  //FeesReceipt
  def notifyFeesReceiptChangeStatus(FeesReceipt feesReceipt){
    def paramsEmailer=notifyService.parametersForFeesReceipt(feesReceipt, feesReceipt.status, feesReceipt.company)
    def idEmailer
    def emailList
    switch(feesReceipt.status){
      //case FeesReceiptStatus.CREADA:
      //break
      case FeesReceiptStatus.POR_AUTORIZAR:
      idEmailer = grailsApplication.config.emailer.feesReceiptAcceptStatus
      emailList = getEmailList(feesReceipt.company,["ROLE_AUTHORIZER_VISOR", "ROLE_AUTHORIZER_EJECUTOR"])
      break
      case FeesReceiptStatus.AUTORIZADA:
      idEmailer = grailsApplication.config.emailer.feesReceiptAcceptStatus
      emailList = getEmailList(feesReceipt.company,["ROLE_FICO_VISOR", "ROLE_FICO_EJECUTOR"])
      break
      case FeesReceiptStatus.EJECUTADA:
      idEmailer = grailsApplication.config.emailer.feesReceiptPayed
      emailList = getEmailList(feesReceipt.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
      break
      case FeesReceiptStatus.CANCELADA:
      idEmailer = grailsApplication.config.emailer.feesReceiptCancelStatus
      emailList = getEmailList(feesReceipt.company,["ROLE_OPERATOR_VISOR", "ROLE_OPERATOR_EJECUTOR"])
      break
      case FeesReceiptStatus.RECHAZADA:
      idEmailer = grailsApplication.config.emailer.feesReceiptCancelStatus
      emailList = getEmailList(feesReceipt.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
      break
    }
    notifyService.sendEmailNotifications(emailList, idEmailer, paramsEmailer)
  }

  //LoanOrderPayment
  def notifyLoanPaymentOrderChangeStatus(LoanPaymentOrder order){
    def idEmailer
    def emailList
    def paramsEmailer=notifyService.parametersForLoanPaymentOrder(order, order.status)
    switch(order.status){
      //case LoanPaymentOrderStatus.CREATED:
      //break
      case LoanPaymentOrderStatus.VALIDATE:
      idEmailer = grailsApplication.config.emailer.LoanPaymentOrderAcceptStatus
      emailList = getEmailList(order.company,["ROLE_AUTHORIZER_VISOR", "ROLE_AUTHORIZER_EJECUTOR"])
      break
      case LoanPaymentOrderStatus.AUTHORIZED:
      idEmailer = grailsApplication.config.emailer.LoanPaymentOrderAcceptStatus
      emailList = getEmailList(order.company,["ROLE_FICO_VISOR", "ROLE_FICO_EJECUTOR"])
      break
      case LoanPaymentOrderStatus.REJECTED:
      idEmailer = grailsApplication.config.emailer.LoanPaymentOrderCancelStatus
      emailList = getEmailList(order.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
      break
      case LoanPaymentOrderStatus.EXECUTED:
      idEmailer = grailsApplication.config.emailer.LoanPaymentOrderCancelStatus
      emailList = getEmailList(order.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
      break
      case LoanPaymentOrderStatus.CANCELED:
      idEmailer = grailsApplication.config.emailer.LoanPaymentOrderCancelStatus
      emailList = getEmailList(order.company,["ROLE_OPERATOR_VISOR", "ROLE_OPERATOR_EJECUTOR"])
      break
    }
    notifyService.sendEmailNotifications(emailList, idEmailer, paramsEmailer)
  }

  //LoanOrder
  //TODO:
  //Approved: Legal Representatives de company creditor
  //Accepted: Fico de compañia a la que pertenece la orden
  //Execute: Legal Rep. de ambas compañías
  def notifyLoanOrderChangeStatus(LoanOrder order){
  def idEmailer
  def emailList
  def paramsEmailer=notifyService.parametersForLoanOrder(order, order.status)
  switch(order.status){
    //case LoanOrderStatus.CREATED:
    //break
    case LoanOrderStatus.VALIDATE:
    idEmailer = grailsApplication.config.emailer.loanOrderAcceptStatus
    emailList = getEmailList(order.company,["ROLE_AUTHORIZER_VISOR", "ROLE_AUTHORIZER_EJECUTOR"])
    break
    case LoanOrderStatus.AUTHORIZED:
    idEmailer = grailsApplication.config.emailer.loanOrderAcceptStatus
    emailList = getEmailList(order.company,["ROLE_FICO_VISOR", "ROLE_FICO_EJECUTOR"])
    break
    case LoanOrderStatus.EXECUTED:
    idEmailer = grailsApplication.config.emailer.loanOrderAcceptStatus
    emailList = getEmailList(order.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
    break
    case LoanOrderStatus.APPROVED:
    idEmailer = grailsApplication.config.emailer.loanOrderAcceptStatus
    emailList = getEmailList(order.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
    break
    case LoanOrderStatus.ACCEPTED:
    idEmailer = grailsApplication.config.emailer.loanOrderAcceptStatus
    emailList = getEmailList(order.company,["ROLE_FICO_VISOR", "ROLE_FICO_EJECUTOR"])
    break
    case LoanOrderStatus.REJECTED:
    idEmailer = grailsApplication.config.emailer.loanOrderCancelStatus
    emailList = getEmailList(order.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
    break
    case LoanOrderStatus.CANCELED:
    idEmailer = grailsApplication.config.emailer.loanOrderCancelStatus
    emailList = getEmailList(order.company,["ROLE_OPERATOR_VISOR", "ROLE_OPERATOR_EJECUTOR"])
    break
  }
  notifyService.sendEmailNotifications(emailList, idEmailer, paramsEmailer)
}

//PurchaseOrder
def notifyPurchaseOrderChangeStatus(PurchaseOrder order){
  def emailList
  def idEmailer
  def paramsEmailer=notifyService.parametersForPurchaseOrder(order, order.status)
  switch(order.status){
    case PurchaseOrderStatus.CREADA:
    idEmailer = grailsApplication.config.emailer.purchaseOrderAcceptStatus
    emailList = getEmailList(order.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
    break;
    case PurchaseOrderStatus.POR_AUTORIZAR:
    idEmailer = grailsApplication.config.emailer.purchaseOrderAcceptStatus
    emailList = getEmailList(order.company,["ROLE_AUTHORIZER_VISOR", "ROLE_AUTHORIZER_EJECUTOR"])
    break;
    case PurchaseOrderStatus.AUTORIZADA:
    idEmailer = grailsApplication.config.emailer.purchaseOrderAcceptStatus
    emailList = getEmailList(order.company,["ROLE_FICO_VISOR", "ROLE_FICO_EJECUTOR"])
    break;
    case PurchaseOrderStatus.RECHAZADA:
    idEmailer = grailsApplication.config.emailer.purchaseOrderCancelStatus
    emailList = getEmailList(order.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
    break;
    case PurchaseOrderStatus.PAGADA:
    idEmailer = grailsApplication.config.emailer.purchaseOrderCancelStatus
    emailList = getEmailList(order.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR"])
    break;
    case PurchaseOrderStatus.CANCELADA:
    idEmailer = grailsApplication.config.emailer.purchaseOrderCancelStatus
    emailList = getEmailList(order.company,["ROLE_OPERATOR_VISOR", "ROLE_OPERATOR_EJECUTOR"])
    break;
  }
  notifyService.sendEmailNotifications(emailList, idEmailer, paramsEmailer)
}

  private def getEmailList (Company company, def roleList){
    def emailList = []
    def usersList = directorService.findUsersOfCompanyByRole(company.id, roleList)
    usersList.each{ user ->
      emailList.add(user.profile.email)
    }
    emailList.unique()
  }

  def notifyStpDepositReceived(Payment payment) {
    def paramsEmailer=notifyService.parametersForStpDeposit(payment)
    def idEmailer = grailsApplication.config.emailer.stpDepositReceived
    def emailList = getEmailList(payment.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR", "ROLE_FICO_VISOR", "ROLE_FICO_EJECUTOR"])
    log.info "Parameters: ${paramsEmailer}"
    log.info "Id Template: ${idEmailer}"
    log.info "Emails list: ${emailList}"
    notifyService.sendEmailNotifications(emailList, idEmailer, paramsEmailer)
  }

  def notifyPaymentToPurchaseOrder(PurchaseOrder purchaseOrder) {
    def paramsEmailer=notifyService.parametersForPaymentToPurchase(purchaseOrder)
    def idEmailer = grailsApplication.config.emailer.paymentToPurchase
    def emailList = getEmailList(purchaseOrder.company,["ROLE_LEGAL_REPRESENTATIVE_VISOR", "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR", "ROLE_FICO_VISOR", "ROLE_FICO_EJECUTOR"])
    log.info "Parameters: ${paramsEmailer}"
    log.info "Id Template: ${idEmailer}"
    log.info "Emails list: ${emailList}"
    notifyService.sendEmailNotifications(emailList, idEmailer, paramsEmailer)

  }
}
