package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(ConciliationService)
@Mock([Conciliation, Payment, SaleOrder, SaleOrderPayment, SaleOrderItem])
class ConciliationServiceSpec extends Specification {

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

}
