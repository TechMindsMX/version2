package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class LoanOrderService {

  DocumentService documentService
  RestService restService
  TransactionHelperService transactionHelperService
  def springSecurityService
  def emailSenderService
  def grailsApplication

  def addDocumentToLoanOrder(def document, LoanOrder loanOrder, String type){
    documentService.uploadDocumentForOrder(document,type,loanOrder)
  }

  // TODO: Refactor: código duplicado
  def createRequestLoanOrder(Company company, BigDecimal amount) {
    LoanOrder loanOrder = new LoanOrder(amount:amount,type:LoanOrderType.DEBTOR)
    loanOrder.status = LoanOrderStatus.VALIDATE
    company.addToLoanOrders(loanOrder)
    company.save()
    emailSenderService.notifyLoanOrderChangeStatus(loanOrder)
    loanOrder
  }

  def createLoanOrder(LoanOrder loanOrder) {
    loanOrder.save flush:true
    company.addToLoanOrders(loanOrder)
    company.save()
    loanOrder
  }

  // TODO: Refactor
  def addAuthorizationToLoanOrder(LoanOrder order, User user){
    def authorization = new Authorization(user:user)
    order.addToAuthorizations(authorization)
    order.save()
    if (isFullAuthorized(order)){
      order.status = LoanOrderStatus.AUTHORIZED
      order.save()
      emailSenderService.notifyLoanOrderChangeStatus(order)
    }
  }

  def approvingLoanOrder(Long loanOrderId){
    LoanOrder order = LoanOrder.get(loanOrderId)
    approvingLoanOrder(order)
  }

  def approvingLoanOrder(LoanOrder loanOrder){
    loanOrder.status = LoanOrderStatus.APPROVED
    loanOrder.save flush:true
      emailSenderService.notifyLoanOrderChangeStatus(loanOrder)
  }

  def executeLoanOrder(LoanOrder loanOrder){
    Commission commission = loanOrder.creditor.commissions.find {
      it.type == CommissionType.PRESTAMO
    }

    if(!commission)
      throw new CommissionException('No existe comisión para la operación')

    TransferFundsCommand command = transactionHelperService.generateCommandFromLoanOrder(loanOrder, commission)
    restService.sendCommandWithAuth(command, grailsApplication.config.modulus.loanCreate)
    loanOrder.status = LoanOrderStatus.EXECUTED
    loanOrder.loanDate = new Date()
    loanOrder.save()
    emailSenderService.notifyLoanOrderChangeStatus(loanOrder)
    loanOrder
  }

  private def isFullAuthorized(order){
    (order.authorizations?.size() ?: 0) >= order.company.numberOfAuthorizations
  }

  def sendToConfirmLoanOrder(LoanOrder loanOrder) {
    loanOrder.status = LoanOrderStatus.VALIDATE
    loanOrder.save flush:true
    emailSenderService.notifyLoanOrderChangeStatus(loanOrder)
  }
}
