package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CommissionController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def corporateService

  def index(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    def company = Company.get(params.companyId)
    respond Commission.findAllByCompany(company), model:[company: company]
  }

  def show(Commission commission) {
    respond commission
  }

  def create() {
    def company = Company.get(params.long("companyId"))
    respond new Commission(params), model:[company: company]
  }

  @Transactional
  def save(CommissionCommand command) {
    if (command == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    if (command.hasErrors()) {
      transactionStatus.setRollbackOnly()
      render view:'create', model:[company:Company.get(params.company), commandErrors:command.errors]
      return
    }

    Commission commission = command.createCommission()
    commission.company = Company.get(params.long('company'))

    if (!commission.validate()){
      transactionStatus.setRollbackOnly()
      render view:'create', model:[commission:commission, company:commission.company]
      return
    }

    commission.save flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'commission.created.message', args: [message(code: 'commission.label', default: 'Commission'), commission.id])
        redirect action:'show', id: commission.id, params: params
      }
      '*' { respond commission, [status: CREATED] }
    }
  }

  def edit(Commission commission) {
    Company company = Company.get(params.long("companyId"))
    respond commission, model:[company: company]
  }

  @Transactional
  def update(CommissionCommand command) {
    if (command == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    def commission = Commission.findById(params.id)
    def company = commission.company
    commission.properties = command.createCommission().properties
    commission.company = company

    if (commission.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond commission.errors, view:'edit', model:[company:commission.company]
      return
    }

    if (commissionTypeAlreadyExists(commission)) {
      transactionStatus.setRollbackOnly()
      respond commission, view:'edit', model:[company:commission.company]
      return
    }

    commission.save flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'commission.updated.message')
        redirect commission
      }
      '*'{ respond commission, [status: OK] }
    }
  }

  @Transactional
  def delete(Commission commission) {

    if (commission == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    Company company = commission.company
    commission.delete flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'commission.label', default: 'Commission'), commission.id])
        redirect action:'index', params: [companyId: company.id]
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  private Commission commissionTypeAlreadyExists(Commission commission) {
    Commission.findByCompanyAndTypeAndIdNotEqual(commission.company, commission.type, commission.id)
  }

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'commission.label', default: 'Commission'), params.id])
        redirect action: "index", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }
}
