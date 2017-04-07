package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(PaymentService)
@Mock([Payment, SaleOrder, Company])
class PaymentServiceSpec extends Specification {

  def emailSenderService = Mock(EmailSenderService)
  def modulusUnoService = Mock(ModulusUnoService)

  def setup(){
    service.emailSenderService = emailSenderService
    service.modulusUnoService = modulusUnoService
  }

  void "Should conciliate a sale order with payment"() {
    given:"A Sale Order"
      SaleOrder saleOrder = new SaleOrder(status:SaleOrderStatus.EJECUTADA).save(validate:false)
    and:"A payment"
      Payment payment = new Payment(status:PaymentStatus.PENDING).save(validate:false)
    when:"We conciliate the payment"
      def result = service.concilationForSaleOrderWithPayment(saleOrder.id, payment.id)
    then:"We expect payment conciliated"
      result.status == PaymentStatus.CONCILIATED
      1 * emailSenderService.notifySaleOrderChangeStatus(_)
  }

  void "Should conciliate a payment"() {
    given:"A payment"
      Payment payment = new Payment(amount:1000, status:PaymentStatus.PENDING, company: new Company().save(validate:false)).save(validate:false)
    when:
      def result = service.conciliatePayment(payment)
    then:
      result.status == PaymentStatus.CONCILIATED
  }
}
