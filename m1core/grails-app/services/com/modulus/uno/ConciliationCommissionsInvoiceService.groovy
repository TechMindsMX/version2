package com.modulus.uno

class ConciliationCommissionsInvoiceService {

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

}
