package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import java.math.RoundingMode

import com.modulus.uno.BusinessEntity

class PaymentController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def paymentService
  def movimientosBancariosService
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

  def cancelPayment(Payment payment){
    payment.status = PaymentStatus.CANCELED
    payment.save()
    redirect action:'index'
  }

  def conciliation() {
  }

  def referencedPayments() {
    Company company = Company.get(session.company)
    Map styleClasses = [tabReferenced:"active", tabNotReferenced:"", tabBankingDeposits:"", tabBankingWithdraws:""]
    Map payments = paymentService.findReferencedPaymentsForCompany(company)
    render view:"conciliation", model:[payments:payments, styleClasses:styleClasses, conciliated:false]
  }

  def notReferencedPayments() {
    Company company = Company.get(session.company)
    Map styleClasses = [tabReferenced:"", tabNotReferenced:"active", tabBankingDeposits:"", tabBankingWithdraws:""]
    Map payments = paymentService.findNotReferencedPaymentsForCompany(company)
    render view:"conciliation", model:[payments:payments, styleClasses:styleClasses, conciliated:false]
  }

  def conciliateBankingDeposits() {
    Company company = Company.get(session.company)
    Map styleClasses = [tabReferenced:"", tabNotReferenced:"", tabBankingDeposits:"active", tabBankingWithdraws:""]
    List<MovimientosBancarios> bankingDeposits = movimientosBancariosService.findBankingDepositsToConciliateForCompany(company)
    render view:"conciliation", model:[bankingDeposits:bankingDeposits, styleClasses:styleClasses, conciliated:false]
  }

  def conciliateBankingWithdraws() {
    Company company = Company.get(session.company)
    Map styleClasses = [tabReferenced:"", tabNotReferenced:"", tabBankingDeposits:"", tabBankingWithdraws:"active"]
    List<MovimientosBancarios> bankingWithdraws = movimientosBancariosService.findBankingWithdrawsToConciliateForCompany(company)
    render view:"conciliation", model:[bankingWithdraws:bankingWithdraws, styleClasses:styleClasses, conciliated:false]
  }

  def referencedPaymentsConciliated() {
    Company company = Company.get(session.company)
    Map styleClasses = [tabReferenced:"active", tabNotReferenced:"", tabBankingDeposits:"", tabBankingWithdraws:""]
    Map payments = paymentService.findReferencedPaymentsConciliated(company)
    render view:"conciliation", model:[payments:payments, styleClasses:styleClasses, conciliated:true]
  }

  def notReferencedPaymentsConciliated() {
    Company company = Company.get(session.company)
    Map styleClasses = [tabReferenced:"", tabNotReferenced:"active", tabBankingDeposits:"", tabBankingWithdraws:""]
    Map payments = paymentService.findNotReferencedPaymentsForCompanyConciliated(company)
    render view:"conciliation", model:[payments:payments, styleClasses:styleClasses, conciliated:true]
  }

  def conciliateBankingDepositsConciliated() {
    Company company = Company.get(session.company)
    Map styleClasses = [tabReferenced:"", tabNotReferenced:"", tabBankingDeposits:"active", tabBankingWithdraws:""]
    List<MovimientosBancarios> bankingDeposits = movimientosBancariosService.findBankingDepositsForCompanyConciliated(company)
    render view:"conciliation", model:[bankingDeposits:bankingDeposits, styleClasses:styleClasses, conciliated:true]
  }

  def conciliateBankingWithdrawsConciliated() {
    Company company = Company.get(session.company)
    Map styleClasses = [tabReferenced:"", tabNotReferenced:"", tabBankingDeposits:"", tabBankingWithdraws:"active"]
    List<MovimientosBancarios> bankingWithdraws = movimientosBancariosService.findBankingWithdrawsForCompanyConciliated(company)
    render view:"conciliation", model:[bankingWithdraws:bankingWithdraws, styleClasses:styleClasses, conciliated:true]
  }

  def referencedPaymentsByRfc(String rfc) {
    Company company = Company.get(session.company)
    Map styleClasses = [tabReferenced:"active", tabNotReferenced:"", tabBankingDeposits:"", tabBankingWithdraws:""]
    def payments = paymentService.findReferencedPaymentsByRfc(company, rfc)
    render view:"conciliation", model:[payments:payments, styleClasses:styleClasses]
  }
}
