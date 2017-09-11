package com.modulus.uno.menu

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class MenuController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

	MenuOperationsService menuOperationsService

  def index(Integer max) {
    params.max = Math.min(max ?: 25, 100)
    respond Menu.list(params), model:[menuCount: Menu.count()]
  }

  def show(Menu menu) {
    respond menu
  }

  def create() {
    respond new Menu(params)
  }

  @Transactional
  def save(Menu menu) {
    if (menu == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    if (menu.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond menu.errors, view:'create'
      return
    }

    menu.save flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.created.message', args: [message(code: 'menu.label', default: 'Menu'), menu.id])
        redirect menu
      }
      '*' { respond menu, [status: CREATED] }
    }
  }

  def edit(Menu menu) {
    respond menu
  }

  @Transactional
  def update(Menu menu) {
    if (menu == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    if (menu.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond menu.errors, view:'edit'
      return
    }

    menu.save flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.updated.message', args: [message(code: 'menu.label', default: 'Menu'), menu.id])
        redirect menu
      }
      '*'{ respond menu, [status: OK] }
    }
  }

  @Transactional
  def delete(Menu menu) {

    if (menu == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

		menuOperationsService.removeMenuForAllRolesAssigned(menu)
    menu.delete flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'menu.label', default: 'Menu'), menu.id])
        redirect action:"index", method:"GET"
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'menu.label', default: 'Menu'), params.id])
        redirect action: "index", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }
}
