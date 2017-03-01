package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class CashOutOrderService {

	def restService
  def grailsApplication
  def modulusUnoService
	def companyService

	def addAutorizationToCashoutOrder(CashOutOrder cashOutOrder, User user) {
		def authorization = new Authorization(user: user)
		cashOutOrder.addToAuthorizations(authorization)
		cashOutOrder.save()
	}

	def authorizeAndDoCashOutOrder(CashOutOrder cashOutOrder) {
		modulusUnoService.approveCashOutOrder(cashOutOrder)
		cashOutOrder
	}

  BigDecimal getTotalOrdersAuthorizedOfCompany(Company company){
    CashOutOrder.findAllByCompanyAndStatus(company, CashOutOrderStatus.AUTHORIZED).amount.sum()
  }

	def isAvailableForAuthorize(CashOutOrder cashOutOrder) {
		(cashOutOrder.authorizations?.size() ?: 0) == cashOutOrder.company.numberOfAuthorizations
	}

  def getCashoutOrderStatus(String status){
    def cashoutOrderStatuses = []
    cashoutOrderStatuses = Arrays.asList(CashOutOrderStatus.values())
    if (status){
      def listStatus = status.tokenize(",")
      cashoutOrderStatuses = listStatus.collect { it as CashOutOrderStatus }
    }

    cashoutOrderStatuses
  }


  def getCashoutOrdersToList(Long company, params){
    def statusOrders = getCashoutOrderStatus(params.status)
    def cashoutOrders = [:]
    if(company){
      cashoutOrders.list = CashOutOrder.findAllByCompanyAndStatusInList(Company.get(company), statusOrders)
      cashoutOrders.items = CashOutOrder.countByCompanyAndStatusInList(Company.get(company), statusOrders)
    } else{
      cashoutOrders.list = CashOutOrder.findAllByStatusInList(statusOrders)
      cashoutOrders.items = CashOutOrder.countByStatusInList(statusOrders)
    }
    cashoutOrders
  }

}
