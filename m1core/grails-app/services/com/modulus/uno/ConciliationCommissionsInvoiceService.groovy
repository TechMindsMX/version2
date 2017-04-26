package com.modulus.uno

class ConciliationCommissionsInvoiceService {

  def commissionsInvoiceService

  List<ConciliationCommissionsInvoice> getConciliationsToApplyForPayment(PaymentM1Emitter payment) {
    ConciliationCommissionsInvoice.findAllByPaymentAndStatus(payment, ConciliationStatus.TO_APPLY)
  }

  ConciliationCommissionsInvoice saveConciliation(ConciliationCommissionsInvoice conciliation) {
    if (conciliation.amount > conciliation.invoice.amountToPay) {
      throw new BusinessException("El monto a conciliar no puede ser mayor al monto por pagar de la factura")
    }

    conciliation.save()
    conciliation
  }

  void deleteConciliation(ConciliationCommissionsInvoice conciliation) {
    conciliation.delete()
  }

  void applyConciliationsForPayment(PaymentM1Emitter payment) {
    List<ConciliationCommissionsInvoice> conciliations = ConciliationCommissionsInvoice.findAllByPaymentAndStatus(payment, ConciliationStatus.TO_APPLY)
    conciliations.each { conciliation ->
      applyConciliation(conciliation)
    }
    payment.status = PaymentStatus.CONCILIATED
    payment.save()
  }

  private void applyConciliation(ConciliationCommissionsInvoice conciliation) {
    commissionsInvoiceService.createPaymentToCommissionsInvoiceWithAmount(conciliation.invoice, conciliation.amount)
    conciliation.status = ConciliationStatus.APPLIED
    conciliation.save()
  }

}
