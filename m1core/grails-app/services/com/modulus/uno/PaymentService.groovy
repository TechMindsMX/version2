package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class PaymentService {

  def modulusUnoService
  def emailSenderService
  def businessEntityService

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

  Map findReferencedPaymentsForCompany(Company company) {
    Map payments = [:]
    List<Payment> paymentsList = Payment.findAllByCompanyAndStatusAndRfcIsNotNull(company, PaymentStatus.PENDING)
    if (paymentsList) {
      List<BusinessEntity> companyClients = businessEntityService.findBusinessEntityByKeyword("", "CLIENT", company)
      List<BusinessEntity> businessEntities = paymentsList.collect { pay ->
        companyClients.find { client -> pay.rfc == client.rfc }
      }
      payments.list = paymentsList
      payments.clients = businessEntities
    }
    payments
  }

  Map findNotReferencedPaymentsForCompany(Company company) {
    Map payments = [:]
    List<Payment> paymentsList = Payment.findAllByCompanyAndStatusAndRfcIsNull(company, PaymentStatus.PENDING)
    payments.list = paymentsList
    payments
  }

  Payment conciliatePayment(Payment payment) {
    payment.status = PaymentStatus.CONCILIATED
    payment.save()
    payment
  }

  BigDecimal getPaymentsFromClientToPay(Company company, String rfc){
    List<Payment> paymentToConciliated = Payment.createCriteria().list{
      eq("rfc", rfc)
      eq("company", company)
      'in'("status", [PaymentStatus.PENDING])
    }
     paymentToConciliated.amount.sum()
  }

  Map findReferencedPaymentsByRfc(Company company, String rfc) {
   Map payments = [:]
   List<Payment> paymentsList = Payment.findAllByCompanyAndRfc(company, rfc)
   payments.list = paymentsList
   payments
  }
}
