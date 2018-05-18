package com.modulus.uno.saleorder

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SaleOrderItemController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def index(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    respond SaleOrderItem.list(params), model:[saleOrderItemCount: SaleOrderItem.count()]
  }

  def show(SaleOrderItem saleOrderItem) {
    respond saleOrderItem
  }

  def create() {
    respond new SaleOrderItem(params)
  }

  @Transactional
  def save(SaleOrderItemCommand command) {
    if (!command) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    def saleOrder = SaleOrder.findById(params.saleOrder.id)

    def saleOrderItem = command.createSaleOrderItem()
    saleOrderItem.saleOrder = saleOrder

    if (saleOrderItem.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond saleOrderItem.errors, view:'create'
      return
    }


    saleOrderItem.save flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'saleOrderItem.created', args: [:])
        redirect controller:'saleOrder', action:'show', id:saleOrderItem.saleOrder.id, params:params
      }
      '*' { respond saleOrderItem, [status: CREATED] }
    }
  }

  def edit(SaleOrderItem saleOrderItem) {
    respond saleOrderItem
  }

  @Transactional
  def update(SaleOrderItemCommand command) {
    if (!command) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }
    def saleOrderItem = SaleOrderItem.findById(params.id)
    def saleOrder = saleOrderItem.saleOrder
    saleOrderItem.properties = command.createSaleOrderItem.properties
    saleOrderItem.saleOrder = saleOrder
    if (saleOrderItem.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond saleOrderItem.errors, view:'edit'
      return
    }

    saleOrderItem.save flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.updated.message', args: [message(code: 'saleOrderItem.label', default: 'SaleOrderItem'), saleOrderItem.id])
        redirect saleOrderItem
      }
      '*'{ respond saleOrderItem, [status: OK] }
    }
  }

  @Transactional
  def delete(SaleOrderItem saleOrderItem) {

    if (saleOrderItem == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    saleOrderItem.delete flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'saleOrderItem.label', default: 'SaleOrderItem'), saleOrderItem.id])
        redirect action:"index", method:"GET"
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'saleOrderItem.label', default: 'SaleOrderItem'), params.id])
        redirect action: "index", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }
}
