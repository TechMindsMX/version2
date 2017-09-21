package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import com.modulus.uno.menu.Menu
import com.modulus.uno.menu.MenuService

@Transactional(readOnly = true)
class MenuController {

  MenuService menuService

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", removeSubmenu: "DELETE"]

  def index(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    respond Menu.list(params), model:[menuCount: Menu.count()]
  }

  def show(Menu menu) {
    def allMenus = Menu.list()
    def menusNotIncluded = (allMenus - menu?.menus ?: []).findAll { m -> !m.menus  }
    respond menu, model:[menusNotIncluded: menusNotIncluded]
  }

  def addSubmenu(){
    menuService.addFewSubmenusToMenu(params.id.toLong(), params.menuOption*.toLong())
    redirect action:"show", id: params.id
  }

  def removeSubmenu(){
    menuService.removeSubmenuToMenu(params.long('id'), params.long('submenuId'))
    redirect action:"show", id: params.id
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
