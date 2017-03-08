package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class PaymentService {

  def modulusUnoService
  def emailSenderService

  def getPaymentStatus(String status){
    def listPaymentStatus = []
    listPaymentStatus = Arrays.asList(PaymentStatus.values())
    if (status){
      def listStatus = status.tokenize(",")
      listPaymentStatus = listStatus.collect { it as PaymentStatus }
    }

    listPaymentStatus
  }

  def getPaymentsToList(Long company, params){
    def statusOrders = getPaymentStatus(params.status)
    def payments = [:]
    if (company){
      payments.list = Payment.findAllByCompanyAndStatusInList(Company.get(company), statusOrders, params)
      payments.items = Payment.countByCompanyAndStatusInList(Company.get(company), statusOrders)
    } else {
      payments.list = Payment.findAllByStatusInList(statusOrders, params)
      payments.items = Payment.countByStatusInList(statusOrders)
    }
    payments
  }

  Payment concilationForSaleOrderWithPayment(Long saleOrderId, Long paymentId){
    Payment payment = Payment.get(paymentId)
    SaleOrder order = SaleOrder.get(saleOrderId)
    if(order){
      payment.saleOrder = order
      payment.status = PaymentStatus.CONCILIATED
      order.status = SaleOrderStatus.PAGADA
      payment.save()
      order.save()
      emailSenderService.notifySaleOrderChangeStatus(order)
    } else {
      throw new BusinessException("Error al conciliar la orden...")
    }
    payment
  }

}
