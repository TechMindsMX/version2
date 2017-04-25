package com.modulus.uno

import grails.transaction.Transactional
import java.math.RoundingMode

class ConciliationCommissionsInvoiceController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def conciliationCommissionsInvoiceService

  def chooseInvoiceToConciliate(PaymentM1Emitter payment) {
    log.info "Payment to conciliate: ${payment.dump()}"
    List<ConciliationCommissionsInvoice> conciliations = conciliationCommissionsInvoiceService.getConciliationsToApplyForPayment(payment)
    BigDecimal toApply = payment.amount - (conciliations ? conciliations*.amount.sum() : new BigDecimal(0))
    List<CommissionsInvoice> invoices = getInvoicesToListForPayment()

    [payment:payment, invoices:invoices, toApply:toApply, conciliations:conciliations]
  }

  private List<CommissionsInvoice> getInvoicesToListForPayment() {
    List <CommissionsInvoice> allInvoices = CommissionsInvoice.findAllByStatus(CommissionsInvoiceStatus.STAMPED)
    List <ConciliationCommissionsInvoice> allConciliations = ConciliationCommissionsInvoice.findAllByStatus(ConciliationStatus.TO_APPLY)
    List <CommissionsInvoice> invoicesFiltered = allInvoices.findAll { invoice ->
      if (!allConciliations.find { conciliation -> conciliation.invoice.id == invoice.id }) {
        invoice
      }
    }
    invoicesFiltered
  }

}
