package com.modulus.uno

import com.modulus.uno.credit.Credit

class CredisController {
  // static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

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

    if (credit.id == null) {
      render view:'create', model: [credit: credit, company: company]
      return
    }

    redirect(action: "show", id: credit.id)
  }

  def update(Credit credit) {
    println credit.dump()
    // if (command == null) {
    //   transactionStatus.setRollbackOnly()
    //   notFound()
    //   return
    // }

    // def product = Product.findById(params.id)
    // def company = product.company
    // product.properties = command.createProduct().properties
    // product.company = company
    // if (product.hasErrors()) {
    //   transactionStatus.setRollbackOnly()
    //   respond product.errors, view:'edit'
    //   return
    // }
    // product.save flush:true
    // request.withFormat {
    //   form multipartForm {
    //     flash.message = message(code: 'product.updated', args: [:])
    //     redirect product
    //   }
    //   '*'{ respond product, [status: OK] }
    // }
  }

}
