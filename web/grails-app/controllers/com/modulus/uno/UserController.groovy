package com.modulus.uno

import static org.springframework.http.HttpStatus.*

class UserController {

  def userService
  def companyService
  def legalRepresentativeService

  static defaultAction = "create"
  static allowedMethods = [save: "POST", update: "POST", delete: "DELETE"]

  def index(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    respond User.list(params), model:[userCount: User.count()]
  }

  def show(User user) {
    respond user
  }

  def profile(User user) {
    render view:"profile", model:[user:user]
  }

  def create() {
    render view:"create", model:[user:new UserCommand(),legal:params.legal,company:session.company]
  }

  def legalRepresentative() {
    [user:new UserCommand(),legal:params.legal,company:session.company]
  }

  def edit(User user) {
    respond user,model:[company:session.company]
  }

  def update(User user) {
    if (user.hasErrors()) {
      log.error "Error updating user ${user.id}"
      render view:"edit", model:[user:user]
      return
    }

    Company company = Company.get(session.company)
		user.save()
    render view:"profile", model:[user:user]
  }

  def delete(User user) {

    if (user == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    user.delete flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'user.label', default: 'User'), user.id])
        redirect action:"index", method:"GET"
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
        redirect action: "index", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }
}
