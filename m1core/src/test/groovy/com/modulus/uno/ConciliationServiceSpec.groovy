package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(ConciliationService)
@Mock([Conciliation, Payment, SaleOrder, SaleOrderPayment, SaleOrderItem, Company])
class ConciliationServiceSpec extends Specification {

  SaleOrderService saleOrderService = Mock(SaleOrderService)
  PaymentService paymentService = Mock(PaymentService)
  def springSecurityService = [currentUser:Mock(User)]

  def setup() {
    service.saleOrderService = saleOrderService
    service.paymentService = paymentService
    service.springSecurityService = springSecurityService
  }

  void "Should get 1000 to apply for payment with amount 1000 and conciliations is empty"() {
    given:"A payment"
      Payment payment = new Payment(amount:1000)
      payment.save(validate:false)
    and:"The existing conciliations to apply for payment"
      service.getConciliationsToApplyForPayment(_) >> []
    when:
      def totalToApply = service.getTotalToApplyForPayment(payment)
    then:
      totalToApply == 1000
  }

  void "Should get 0 to apply for payment with amount 1000 and conciliations sum is 1000"() {
    given:"A payment"
      Payment payment = new Payment(amount:1000)
      payment.save(validate:false)
    and:"The existing conciliations to apply for payment"
      Conciliation conciliation = new Conciliation(amount:1000, payment:payment).save(validate:false)
      def conciliations = [conciliation]
      service.getConciliationsToApplyForPayment(_) >> conciliations
    when:
      def totalToApply = service.getTotalToApplyForPayment(payment)
    then:
      totalToApply == 0
  }

  void "Should get 500 to apply for payment with amount 1000 and conciliations sum is 500"() {
    given:"A payment"
      Payment payment = new Payment(amount:1000)
      payment.save(validate:false)
    and:"The existing conciliations to apply for payment"
      Conciliation conciliation1 = new Conciliation(amount:300, payment:payment).save(validate:false)
      Conciliation conciliation2 = new Conciliation(amount:200, payment:payment).save(validate:false)
      def conciliations = [conciliation1, conciliation2]
      service.getConciliationsToApplyForPayment(_) >> conciliations
    when:
      def totalToApply = service.getTotalToApplyForPayment(payment)
    then:
      totalToApply == 500
  }

  void "Should thrwon exception when amount to conciliate is greatest than amount to pay of sale order"() {
    given:"A sale order to conciliate"
      SaleOrder saleOrder = new SaleOrder()
      SaleOrderItem item = new SaleOrderItem(price:100, quantity:1, ieps:0, iva:16, discount:0).save(validate:false)
      saleOrder.addToItems(item)
      SaleOrderPayment salePay = new SaleOrderPayment(amount:50).save(validate:false)
      saleOrder.addToPayments(salePay)
      saleOrder.save(validate:false)
    and:"The payment to conciliate"
      Payment payment = new Payment(amount:100).save(validate:false)
    and:"The company"
      Company company = Mock(Company)
    and:"The conciliation"
      Conciliation conciliation = new Conciliation(amount:80, payment:payment, saleOrder:saleOrder)
    when:
      service.saveConciliationForCompany(conciliation, company)
    then:
      thrown BusinessException
  }

  void "Should delete all conciliations for a payment"() {
    given:"A payment"
      Payment payment = new Payment(amount:5000).save(validate:false)
    and:"The existing conciliations to apply for payment"
      Conciliation conciliation1 = new Conciliation(amount:300, payment:payment).save(validate:false)
      Conciliation conciliation2 = new Conciliation(amount:200, payment:payment).save(validate:false)
      def conciliations = [conciliation1, conciliation2]
      service.getConciliationsToApplyForPayment(payment) >> conciliations
    when:
      service.cancelConciliationsForPayment(payment)
    then:
      service.getConciliationsToApplyForPayment(payment) == []
  }

  void "Should apply conciliations for a payment"() {
   given:"A company"
      Company company = new Company().save(validate:false)
   and:"A payment"
      Payment payment = new Payment(amount:5000, company:company).save(validate:false)
    and:"The sale orders"
      SaleOrder saleOrder1 = new SaleOrder()
      SaleOrderItem item1 = new SaleOrderItem(price:3000, quantity:1, ieps:0, iva:0, discount:0).save(validate:false)
      saleOrder1.addToItems(item1)
      saleOrder1.save(validate:false)
      SaleOrder saleOrder2 = new SaleOrder()
      SaleOrderItem item2 = new SaleOrderItem(price:2000, quantity:1, ieps:0, iva:0, discount:0).save(validate:false)
      saleOrder2.addToItems(item2)
      saleOrder2.save(validate:false)
    and:"A user"
      User user = Mock(User)
    and:"The existing conciliations to apply for payment"
      Conciliation conciliation1 = new Conciliation(amount:3000, payment:payment, status:ConciliationStatus.TO_APPLY, saleOrder:saleOrder1, company:company, user:user, changeType:0).save(validate:false)
      Conciliation conciliation2 = new Conciliation(amount:2000, payment:payment, status:ConciliationStatus.TO_APPLY, saleOrder:saleOrder2, company:company, user:user, changeType:0).save(validate:false)
      def conciliations = [conciliation1, conciliation2]
      service.getConciliationsToApplyForPayment(payment) >> conciliations
    when:
      service.applyConciliationsForPayment(payment)
    then:
      service.getConciliationsToApplyForPayment(payment) == []
      service.getConciliationsAppliedForPayment(payment) == conciliations
      2 * saleOrderService.addPaymentToSaleOrder(_, _, _)
      1 * paymentService.conciliatePayment(_)
  }

  void "Should apply conciliation without invoice for payment"() {
    given:"A company"
      Company company = new Company().save(validate:false)
    and:"A payment"
      Payment payment = new Payment(amount:5000, company:company).save(validate:false)
    and:"The conciliation"
      Conciliation conciliation = new Conciliation(amount:5000, comment:"Conciliation without invoice test", payment:payment)
    and:
      User user = Mock(User)
      springSecurityService.currentUser >> user
    when:
      service.applyConciliationWithoutInvoice(conciliation)
    then:
      conciliation.status == ConciliationStatus.APPLIED
      1 * paymentService.conciliatePayment(_)
  }

}
