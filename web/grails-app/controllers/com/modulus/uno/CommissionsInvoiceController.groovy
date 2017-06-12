package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CommissionsInvoiceController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def commissionTransactionService
  def commissionsInvoiceService
  def paymentM1EmitterService

  def listCommissionsInvoice(Company company) {
    params.max = 25
    params.sort = "dateCreated"
    params.order = "desc"
    Corporate corporate = Corporate.get(params.corporateId)
    [invoices:commissionsInvoiceService.getCommissionsInvoiceForCompany(company, params), corporate:corporate, company:company]
  }

  @Transactional
  def stampInvoice(CommissionsInvoice invoice) {
    Corporate corporate = Corporate.get(params.corporateId)
    commissionsInvoiceService.stampInvoice(invoice)
    redirect action:"listCommissionsInvoice", id:invoice.receiver.id, params:[corporateId:corporate.id]
  }

  def showCommissionsInvoice(CommissionsInvoice invoice) {
    Corporate corporate = Corporate.get(params.corporateId)
    List commissionsSummary = commissionsInvoiceService.getCommissionsSummaryFromInvoice(invoice)
    [corporate:corporate, invoice:invoice, commissionsSummary:commissionsSummary]
  }

  @Transactional
  def deleteInvoice(CommissionsInvoice invoice) {
    Corporate corporate = Corporate.get(params.corporateId)
    Company company = invoice.receiver
    commissionsInvoiceService.deleteCommissionsInvoice(invoice)
    redirect action:"listCommissionsInvoice", id:company.id, params:[corporateId:corporate.id]
  }

  @Transactional
  def cancelStampedInvoice(CommissionsInvoice invoice) {
    Corporate corporate = Corporate.get(params.corporateId)
    commissionsInvoiceService.cancelStampedCommissionsInvoice(invoice)
    redirect action:"listCommissionsInvoice", id:invoice.receiver.id, params:[corporateId:corporate.id]
  }

  def conciliate(Company company) {
    Corporate corporate = Corporate.get(params.corporateId)
    [corporate:corporate, company:company, payments:paymentM1EmitterService.getPaymentsInStatus(PaymentStatus.PENDING)]
  }
}
