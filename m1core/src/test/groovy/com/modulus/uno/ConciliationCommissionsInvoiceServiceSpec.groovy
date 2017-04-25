package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(ConciliationCommissionsInvoiceService)
@Mock([ConciliationCommissionsInvoice, PaymentM1Emitter, CommissionsInvoice, CommissionTransaction])
class ConciliationCommissionsInvoiceServiceSpec extends Specification {

  void "Should return null when payment hasn't conciliations to apply"() {
    given:"A payment m1 emitter"
      PaymentM1Emitter payment =  new PaymentM1Emitter(amount:1000).save(validate:false)
    when:
      def result = service.getConciliationsToApplyForPayment(payment)
    then:
      !result
  }

  void "Should get two elements when payment has two conciliations to apply"() {
    given:"A payment m1 emitter"
      PaymentM1Emitter payment =  new PaymentM1Emitter(amount:1000).save(validate:false)
    and:"The conciliations for payment"
      ConciliationCommissionsInvoice conciliation1 = new ConciliationCommissionsInvoice(payment:payment, amount:100).save(validate:false)
      ConciliationCommissionsInvoice conciliation2 = new ConciliationCommissionsInvoice(payment:payment, amount:200).save(validate:false)
    when:
      def result = service.getConciliationsToApplyForPayment(payment)
    then:
      result.size() == 2
  }

  void "Should save a conciliation for payment m1 emitter with commissions invoice"() {
    given:"The commissions invoice"
      CommissionsInvoice invoice = new CommissionsInvoice(status:CommissionsInvoiceStatus.STAMPED, payments:[]).save(validate:false)
      CommissionTransaction commission = new CommissionTransaction(type:CommissionType.FIJA, amount:1000, invoice:invoice).save(validate:false)
      invoice.addToCommissions(commission)
      invoice.save(validate:false)
    and:"The payment m1 emitter"
      PaymentM1Emitter payment = new PaymentM1Emitter(amount:2000).save(validate:false)
    and:"The conciliation commissions invoice"
      ConciliationCommissionsInvoice conciliation = new ConciliationCommissionsInvoice(payment:payment, amount:500, invoice:invoice)
    when:
      def result = service.saveConciliation(conciliation)
    then:
      result
  }

  void "Should thrown a business exception when save a conciliation for payment m1 emitter with commissions invoice when amount is greatest than amount to pay"() {
    given:"The commissions invoice"
      CommissionsInvoice invoice = new CommissionsInvoice(status:CommissionsInvoiceStatus.STAMPED, payments:[]).save(validate:false)
      CommissionTransaction commission = new CommissionTransaction(type:CommissionType.FIJA, amount:1000, invoice:invoice).save(validate:false)
      invoice.addToCommissions(commission)
      invoice.save(validate:false)
    and:"The payment m1 emitter"
      PaymentM1Emitter payment = new PaymentM1Emitter(amount:2000).save(validate:false)
    and:"The conciliation commissions invoice"
      ConciliationCommissionsInvoice conciliation = new ConciliationCommissionsInvoice(payment:payment, amount:1500, invoice:invoice)
    when:
      service.saveConciliation(conciliation)
    then:
      thrown BusinessException
  }

}
