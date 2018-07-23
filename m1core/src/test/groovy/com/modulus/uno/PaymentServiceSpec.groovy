package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll
import com.modulus.uno.saleorder.SaleOrder

@TestFor(PaymentService)
@Mock([Payment, SaleOrder, Company])
class PaymentServiceSpec extends Specification {

  def emailSenderService = Mock(EmailSenderService)
  def modulusUnoService = Mock(ModulusUnoService)

  def setup(){
    service.emailSenderService = emailSenderService
    service.modulusUnoService = modulusUnoService
  }

  void "Should conciliate a payment"() {
    given:"A payment"
      Payment payment = new Payment(amount:1000, status:PaymentStatus.PENDING, company: new Company().save(validate:false)).save(validate:false)
    when:
      def result = service.conciliatePayment(payment)
    then:
      result.status == PaymentStatus.CONCILIATED
  }

  void "Should find conciliate payments by Rfc"() {
    given:"The company"
      Company company = new Company(rfc:"CO1").save(validate:false)
    and:"The rfc"
      String rfc = "RFC"
    and:
      Payment payment = new Payment(company:company, rfc: "RFC").save(validate:false)
      Payment payment1 = new Payment(company:company, rfc: "RFC1").save(validate:false)
    when:
      def result = service.findReferencedPaymentsByRfc(company, rfc)
    then:
      result.size() == 1
  }

}
