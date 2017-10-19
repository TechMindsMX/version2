package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import jxl.Sheet
import jxl.Workbook


@Transactional(readOnly = true)
class MovimientosBancariosController {

  def movimientosBancariosService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", multiMovimientosBancarios: "POST"]

    def index() {
      def bankAccountsOfCompany = Company.findById(session.company.toLong()).banksAccounts.sort{it.id}
      [bankAccountsOfCompany:bankAccountsOfCompany]
    }

    def show(BankAccount bankAccount) {
      params.max = 25
      params.sort = "dateEvent"
      params.order = "desc"
      def movimientosBancarios = MovimientosBancarios.findAllByCuenta(bankAccount, params)
      [movimientosBancarios:movimientosBancarios, movimientosCount:MovimientosBancarios.countByCuenta(bankAccount), bankAccount:bankAccount]
    }

    def create() {
      def bankAccount = BankAccount.findById(params.id)
        respond new MovimientosBancarios(cuenta:bankAccount)
    }

    @Transactional
    def save(MovimientosBancariosCommand command) {
      def movimientosBancarios = command.createObject()
      if (movimientosBancarios.hasErrors()) {
        transactionStatus.setRollbackOnly()
        respond movimientosBancarios.errors, view:'create'
        return
      }

      movimientosBancarios.save()
      redirect action:"show", id:"${movimientosBancarios.cuenta.id}"
    }

    def multiMovimientos() {
      def bankAccountsOfCompany = Company.findById(session.company.toLong()).banksAccounts
      [bankAccountsOfCompany:bankAccountsOfCompany]
    }

    @Transactional
    def multiMovimientosBancarios() {
      def bankAccountsOfCompany = Company.findById(session.company.toLong()).banksAccounts
      BankAccount account = BankAccount.findById(params.cuenta)
      def file = request.getFile('multiMovimientos')
      Workbook workbook = Workbook.getWorkbook(file.getInputStream());
      int numRows
      (0..workbook.numberOfSheets-1).each{ sheetNo ->
        Sheet page = workbook.getSheet(sheetNo)
        int numColumns = page.columns
        numRows = page.rows
        String data
        (1..numRows-1).each { row ->
          def listElementInRow = []
          (0..4).each { column ->
            data = page.getCell(column, row).contents
            listElementInRow.add(data)
          }
          MovimientosBancariosCommand command = createCommandMovimientos(listElementInRow)
          log.info command.validate()
          if (!command.validate()) {
            transactionStatus.setRollbackOnly()
            flash.command = command.errors
            render view: 'multiMovimientos' ,model:["bankAccountsOfCompany":bankAccountsOfCompany]
            return
          }
          movimientosBancariosService.createMovimeintosBancariosFromList(command,account)
        }
      }
      render view: 'multiMovimientos' ,model:["bankAccountsOfCompany":bankAccountsOfCompany]
    }

    def edit(MovimientosBancarios movimientosBancarios) {
        respond movimientosBancarios
    }

    @Transactional
    def update(MovimientosBancariosCommand command) {
			log.info "Updating bank transaction: ${command.dump()}"
        if (command == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (command.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond command.errors, view:'edit'
            return
        }

				MovimientosBancarios transactionBanking = command.getTransactionBankingToUpdate()
        transactionBanking.save()
				log.info "Bank transaction updated: ${transactionBanking.dump()}"
				
    	redirect action:"show", id:"${transactionBanking.cuenta.id}"
    }

    @Transactional
    def delete(MovimientosBancarios movimientosBancarios) {

        if (movimientosBancarios == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        movimientosBancarios.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'movimientosBancarios.label', default: 'MovimientosBancarios'), movimientosBancarios.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'movimientosBancarios.label', default: 'MovimientosBancarios'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    private MovimientosBancariosCommand createCommandMovimientos(List listRowElements) {
      MovimientosBancariosCommand command = new MovimientosBancariosCommand()
      command.concept = listRowElements.get(1)
      command.reference = listRowElements.get(2)
      command.dateEvent = listRowElements.get(0)
      command.amount = listRowElements.get(3) ?: listRowElements.get(4)
      command.debito = listRowElements.get(3)?:0
      command.credito = listRowElements.get(4)?:0
      command
    }
}
