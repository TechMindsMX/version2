package com.modulus.uno

class ConciliationCommissionsInvoiceService {

  List<ConciliationCommissionsInvoice> getConciliationsToApplyForPayment(PaymentM1Emitter payment) {
    ConciliationCommissionsInvoice.findAllByPaymentAndStatus(payment, ConciliationStatus.TO_APPLY)
  }

  def saveConciliationCommissionsInvoice(PaymentM1Emitter payment, CommissionsInvoice invoice, BigDecimal amount) {
    ConciliationCommissionsInvoice conciliation = new ConciliationCommissionsInvoice(
      payment:payment,
      amount:amount,
      invoice:invoice
    )
    conciliation.save()
    conciliation
  }

}
