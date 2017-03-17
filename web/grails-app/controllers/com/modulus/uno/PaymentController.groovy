package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import java.math.RoundingMode

class PaymentController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def paymentService
  def saleOrderService
  def conciliationService

  @Transactional(readOnly = true)
  def index(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    def payments = paymentService.getPaymentsToList(session.company ? session.company.toLong() : session.company, params)

    [payments: payments.list, paymentsCount: payments.items]
  }

  def show(Payment payment) {
    respond payment
  }

  @Transactional(readOnly = true)
  def create() {
    respond new Payment(params), model:[companies:Company.findAllByStatus(CompanyStatus.ACCEPTED)]
  }

  @Transactional
  def save(Payment payment) {
    payment.amount = new BigDecimal(params.amount) // TODO: Wuackala
    if (payment == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    if (payment.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond payment.errors, view:'create'
      return
    }

    payment.save flush:true

    //TODO: enviar mail al integrado de la notificación de pago

    redirect action:'index', params:[status:"PENDING"]

  }

  def edit(Payment payment) {
    respond payment
  }

  @Transactional
  def update(Payment payment) {
    if (payment == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    if (payment.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond payment.errors, view:'edit'
      return
    }

    payment.save flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.updated.message', args: [message(code: 'payment.label', default: 'Payment'), payment.id])
        redirect payment
      }
      '*'{ respond payment, [status: OK] }
    }
  }

  @Transactional
  def delete(Payment payment) {

    if (payment == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    payment.delete flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'payment.label', default: 'Payment'), payment.id])
        redirect action:"index", method:"GET"
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'payment.label', default: 'Payment'), params.id])
        redirect action: "index", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }

  def reconcile() {
    Company company = Company.get(session.company)
    def payments = Payment.findAllByCompanyAndStatus(company, PaymentStatus.PENDING)
    def saleOrders = SaleOrder.findAllByCompanyAndStatus(company, SaleOrderStatus.EJECUTADA)
    [payments:payments, saleOrders: saleOrders, company:company]
  }

  def tieOrderWithPayment(){
    try {
      def p = paymentService.concilationForSaleOrderWithPayment(params.long("saleOrder.id"),params.long('id'))
      flash.message = """\
        El pago por \$ ${p.amount} se concilió con la orden para el cliente '${p.saleOrder.clientName}' por un monto de \$ ${p.saleOrder.total.setScale(2, RoundingMode.HALF_UP)}
      """
    }catch(e){
      flash.message = e.message
    }
    redirect action:'reconcile'
  }

  def cancelPayment(Payment payment){
    payment.status = PaymentStatus.CANCELED
    payment.save()
    redirect action:'index'
  }

  def conciliation() {
  }

  def referencedPayments() {
    Company company = Company.get(session.company)
    Map styleClasses = [tabReferenced:"active", tabNotReferenced:"", tabInvoiceWithoutPayment:""]
    Map payments = paymentService.findReferencedPaymentsForCompany(company)
    render view:"conciliation", model:[payments:payments, styleClasses:styleClasses]
  }

  def notReferencedPayments() {
    Map styleClasses = [tabReferenced:"", tabNotReferenced:"active", tabInvoiceWithoutPayment:""]
    render view:"conciliation", model:[styleClasses:styleClasses]
  }

  def conciliateInvoicesWithoutPayments() {
    Map styleClasses = [tabReferenced:"", tabNotReferenced:"", tabInvoiceWithoutPayment:"active"]
    render view:"conciliation", model:[styleClasses:styleClasses]
  }

  def chooseInvoiceToConciliate(Payment payment) {
    log.info "Payment to conciliate: ${payment.dump()}"
    BigDecimal toApply = conciliationService.getTotalToApplyForPayment(payment)
    List<Conciliation> conciliations = conciliationService.getConciliationsToApplyForPayment(payment)
    List<SaleOrder> saleOrders = getSaleOrdersToListForPayment(payment, conciliations)

    [payment:payment, saleOrders:saleOrders, toApply:toApply, conciliations:conciliations]
  }

  private List<SaleOrder> getSaleOrdersToListForPayment(Payment payment, List<Conciliation> conciliations) {
    List<SaleOrder> saleOrders = payment.rfc ? saleOrderService.findOrdersToConciliateForCompanyAndClient(payment.company, payment.rfc) : saleOrderService.findOrdersToConciliateForCompany(payment.company)
    List<SaleOrder> saleOrdersFiltered = saleOrders.findAll { saleOrder ->
      if (!conciliations.find { conciliation -> conciliation.saleOrder == saleOrder }){
        saleOrder
      }
    }

  }
  @Transactional
  def addSaleOrderToConciliate(ConciliationCommand command) {
    log.info "Adding conciliation to apply: ${command.dump()}"

    if (command.hasErrors()){
      transactionStatus.setRollbackOnly()
    }

    Conciliation conciliation = command.createConciliation()
    try {
      conciliationService.saveConciliation(conciliation)
    } catch (BusinessException ex) {
      flash.message = ex.message
    }

    redirect action:"chooseInvoiceToConciliate", id:command.paymentId
  }

  @Transactional
  def deleteConciliation(Conciliation conciliation) {
    log.info "Deleting conciliation to apply: ${conciliation.dump()}"
    Payment payment = conciliation.payment

    conciliationService.deleteConciliation(conciliation)

    redirect action:"chooseInvoiceToConciliate", id:payment.id
  }

}
