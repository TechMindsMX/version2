package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class MenuOperationController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond MenuOperation.list(params), model:[menuOperationCount: MenuOperation.count()]
    }

    def show(MenuOperation menuOperation) {
        respond menuOperation
    }

    def create() {
        respond new MenuOperation(params)
    }

    @Transactional
    def save(MenuOperation menuOperation) {
        if (menuOperation == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (menuOperation.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond menuOperation.errors, view:'create'
            return
        }

        menuOperation.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'menuOperation.label', default: 'MenuOperation'), menuOperation.id])
                redirect menuOperation
            }
            '*' { respond menuOperation, [status: CREATED] }
        }
    }

    def edit(MenuOperation menuOperation) {
        respond menuOperation
    }

    @Transactional
    def update(MenuOperation menuOperation) {
        if (menuOperation == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (menuOperation.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond menuOperation.errors, view:'edit'
            return
        }

        menuOperation.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'menuOperation.label', default: 'MenuOperation'), menuOperation.id])
                redirect menuOperation
            }
            '*'{ respond menuOperation, [status: OK] }
        }
    }

    @Transactional
    def delete(MenuOperation menuOperation) {

        if (menuOperation == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        menuOperation.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'menuOperation.label', default: 'MenuOperation'), menuOperation.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'menuOperation.label', default: 'MenuOperation'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
