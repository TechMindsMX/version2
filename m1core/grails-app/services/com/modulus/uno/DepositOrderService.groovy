package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class DepositOrderService {

  def grailsApplication
  def restService
  def documentService
  def modulusUnoService
  def companyService
  def userService
  def emailSenderService

  def addAuthorizationToDepositOrder(DepositOrder order, User user) {
    def authorization = new Authorization(user:user)
    order.addToAuthorizations(authorization)
    order.save()
  }

  def executeDepositOrder(DepositOrder order){
    def cashinResult = modulusUnoService.generateACashinForIntegrated(order)
    order.status = DepositOrderStatus.EXECUTED
    order.uuidTransaction = cashinResult.str
    order.save()
    emailSenderService.notifyDepositOrderChangeStatus(order)
  }

  def getDepositOrderStatus(String status){
    def listDepositOrderStatus = []
    listDepositOrderStatus = Arrays.asList(DepositOrderStatus.values())
    if (status){
      def listStatus = status.tokenize(",")
      listDepositOrderStatus = listStatus.collect { it as DepositOrderStatus }
    }

    listDepositOrderStatus
  }

  def getDepositOrdersToList(Long company, params){
    def statusOrders = getDepositOrderStatus(params.status)
    def depositOrders = [:]
    if (company){
      depositOrders.list = DepositOrder.findAllByCompanyAndStatusInList(Company.get(company), statusOrders, params)
      depositOrders.items = DepositOrder.countByCompanyAndStatusInList(Company.get(company), statusOrders)
    } else {
      depositOrders.list = DepositOrder.findAllByStatusInList(statusOrders, params)
      depositOrders.items = DepositOrder.countByStatusInList(statusOrders)
    }
    depositOrders
  }

  def getTotalDepositOrderAuthorizedOfCompany(Company company){
    DepositOrder.findAllByCompanyAndStatus(company, DepositOrderStatus.AUTHORIZED).amount.sum()
  }

  def isFullAuthorized(DepositOrder order){
    (order.authorizations?.size() ?: 0) >= order.company.numberOfAuthorizations
  }

    def uploadDepositSlipToOrder(def document,DepositOrder order,String type) {
    documentService.uploadDocumentForOrder(document,type,order)
    order.status = DepositOrderStatus.VALIDATE
    order.save()
    emailSenderService.notifyDepositOrderChangeStatus(order)
  }

}
