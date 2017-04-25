package com.modulus.uno

import grails.transaction.Transactional
import java.math.RoundingMode

class ConciliationCommissionsInvoiceController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def conciliationCommissionsInvoiceService

  def chooseInvoiceToConciliate(PaymentM1Emitter payment) {
    log.info "Payment to conciliate: ${payment.dump()}"
    Corporate corporate = Corporate.get(params.corporateId)
    Company company = Company.get(params.companyId)

    List<ConciliationCommissionsInvoice> conciliations = conciliationCommissionsInvoiceService.getConciliationsToApplyForPayment(payment)
    BigDecimal toApply = payment.amount - (conciliations ? conciliations*.amount.sum() : new BigDecimal(0))
    List<CommissionsInvoice> invoices = getInvoicesToListForPayment()

    [payment:payment, invoices:invoices, toApply:toApply, conciliations:conciliations, corporate:corporate, company:company]
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

  @Transactional
  def addInvoiceToConciliate(ConciliationCommissionsInvoiceCommand command) {
    log.info "Adding conciliation to apply: ${command.dump()}"

    if (command.hasErrors()){
      transactionStatus.setRollbackOnly()
      redirect action:"chooseInvoiceToConciliate", id:command.paymentId, params:[corporateId:params.corporateId, companyId:params.companyId]
      return
    }

    ConciliationCommissionsInvoice conciliation = command.createConciliation()
    try {
      conciliationCommissionsInvoiceService.saveConciliation(conciliation)
    } catch (BusinessException ex) {
      flash.message = ex.message
    }

    redirect action:"chooseInvoiceToConciliate", id:command.paymentId, params:[corporateId:params.corporateId, companyId:params.companyId]
  }

  @Transactional
  def deleteConciliation(ConciliationCommissionsInvoice conciliation) {
    log.info "Deleting conciliation to apply: ${conciliation.dump()}"
    PaymentM1Emitter payment = conciliation.payment

    conciliationCommissionsInvoiceService.deleteConciliation(conciliation)

    redirect action:"chooseInvoiceToConciliate", id:payment.id, params:[corporateId:params.corporateId, companyId:params.companyId]
  }

}
