package com.modulus.uno

import com.modulus.uno.credit.Credit
import com.modulus.uno.credit.CreditCommand

class CreditController {

  def creditService

  def index() {
    Company company = Company.get(session.company.toLong())
    def data = creditService.list(company, params)

    respond data.credits, model: [credits: data.credits, total: data.total]
  }

  def create() {
    Company company = Company.get(session.company.toLong())
    respond new Credit(params), model:[company:company]
  }

  def show(Credit credit) {
    Company company = Company.get(session.company)
    respond credit, model: [company: company]
  }

  def edit(Credit credit) {
    Company company = Company.get(session.company)
    respond credit, model: [company: company]
  }

  def save(Credit credit) {
    def company = Company.get(session.company.toLong())

    credit = creditService.createCreditForCompany(credit, company)

    if (credit.hasErrors()) {
      render view:'create', model: [credit: credit, company: company]
      return
    }

    redirect(action: "show", id: credit.id)
  }

  def update(CreditCommand creditCommand) {
    if (!creditCommand) {
      notFound()
      return
    }

    credit.hasCredit = params.enableCredits ?: false
    def company = Company.get(session.company.toLong())
    def credit = creditService.updateCreditForCompany(creditCommand, company)

    if(credit.hasErrors()) {
      respond credit, model: [company: company], view:'edit'
      return
    }

    redirect(action: "show", id: credit.id)
  }

  def authorize() {
    Company company = Company.get(session.company.toLong())
    def data = creditService.list(company, params)
    respond data.credits, model: [credits: data.credits, total: data.total]
  }

  def authorizeSave(CreditCommand creditCommand) {
   if (!creditCommand) {
      notFound()
      return
    }
    def credit = creditService.authorizeCredit(creditCommand)

    if(credit.hasErrors()) {
      respond credit, model: [company: company], view:'authorize'
      return
    }

   redirect(action: "authorize")
  }

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'credit.label', default: 'Credit'), params.id])
        redirect action: "index", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }

}
