package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional
class FeesReceiptController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  FeesReceiptService feesReceiptService
  def springSecurityService
  def companyService
  def emailSenderService

  def index(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    respond FeesReceipt.findAllByBusinessEntity(BusinessEntity.get(params.businessEntity)), model:[businessEntity: params.businessEntity]
  }

  def sendToAuthorize(FeesReceipt feesReceipt){
    Company company = Company.get(session.company)
    feesReceiptService.sendToAuthorize(feesReceipt)

    render view:"list", model:[feesReceiptList:FeesReceipt.findAllByCompany(company)]
  }

  def authorizeFeesReceipt(FeesReceipt feesReceipt){
    Company company = Company.get(session.company)
    User user = springSecurityService.currentUser
    feesReceiptService.addAuthorizationToFeesReceipt(feesReceipt, user)
    if (feesReceiptService.isFullAuthorize(feesReceipt)){
      feesReceiptService.authorizeFeesReceipt(feesReceipt)
    }
    render view:"list", model:[feesReceiptList:FeesReceipt.findAllByStatusAndCompany(FeesReceiptStatus.POR_AUTORIZAR, company)]
  }

  def executeFeesReceipt(FeesReceipt feesReceipt){
    String messageSuccess = message(code:"feesReceipt.already.executed")
    if (companyService.enoughBalanceCompany(feesReceipt.company, feesReceipt.netAmount)){
      if (feesReceipt.status == FeesReceiptStatus.AUTORIZADA) {
        feesReceiptService.executeFeesReceipt(feesReceipt)
        messageSuccess = message(code:"feesReceipt.executed.message")
      }
      render view:"list", model:[feesReceiptList:FeesReceipt.findAllByStatus(FeesReceiptStatus.AUTORIZADA), messageSuccess:messageSuccess]
    } else {
      flash.message = message(code:'company.insufficient.balance')
      redirect action:'show', id:feesReceipt.id
    }
  }

  def cancelFeesReceipt(FeesReceipt feesReceipt){
    Company company = Company.get(session.company)
    feesReceipt.status = FeesReceiptStatus.CANCELADA
    feesReceipt.save()
    emailSenderService.notifyFeesReceiptChangeStatus(feesReceipt)
    render view:"list", model:[feesReceiptList:FeesReceipt.findAllByStatusAndCompany(FeesReceiptStatus.POR_AUTORIZAR, company)]
  }

  def rejectFeesReceipt(FeesReceipt feesReceipt){
    feesReceipt.status = FeesReceiptStatus.RECHAZADA
    feesReceipt.save()
    emailSenderService.notifyFeesReceiptChangeStatus(feesReceipt)
    render view:"list", model:[feesReceiptList:FeesReceipt.findAllByStatus(FeesReceiptStatus.AUTORIZADA)]
  }

  def addDocumentToFeesReceipt(FeesReceipt feesReceipt){
    feesReceiptService.addDocumentToFeesReceipt(params.feesReceiptDocument, feesReceipt, 'feesReceipt')
    redirect action:"show", id:feesReceipt.id, params:params
  }

  def list(Integer max){
    params.max = Math.min(max ?: 25, 100)
    Company company = Company.get(session.company ?: params.company)
    if(params.status) {
      respond FeesReceipt.findAllByStatusAndCompany(FeesReceiptStatus."${params.status}", company)
    } else {
      respond FeesReceipt.findAllByCompany(company)
    }
  }

  def show(FeesReceipt feesReceipt) {
    [feesReceipt:feesReceipt, baseUrlDocuments:grailsApplication.config.grails.url.base.images]
  }

  def listAll(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    List<FeesReceipt> feesReceiptList = FeesReceipt.findAll(params)
    Integer feesReceiptCount = FeesReceipt.countByStatus(FeesReceiptStatus.AUTORIZADA)
    render view:'list', model:[feesReceiptList:feesReceiptList, feesReceiptCount:feesReceiptCount]
  }

  def listToAuthorize(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    Company company = Company.get(session.company)
    List<FeesReceipt> feesReceiptList = FeesReceipt.findAllByStatusAndCompany(FeesReceiptStatus.POR_AUTORIZAR, company, params)
    Integer feesReceiptCount = FeesReceipt.countByStatusAndCompany(FeesReceiptStatus.POR_AUTORIZAR, company)
    render view:'list', model:[feesReceiptList:feesReceiptList, feesReceiptCount:feesReceiptCount]
  }

  def listToExecute(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    List<FeesReceipt> feesReceiptList = FeesReceipt.findAllByStatus(FeesReceiptStatus.AUTORIZADA, params)
    Integer feesReceiptCount = FeesReceipt.countByStatus(FeesReceiptStatus.AUTORIZADA)
    render view:'list', model:[feesReceiptList:feesReceiptList, feesReceiptCount:feesReceiptCount]
  }

  def create() {
    respond new FeesReceipt(params)
  }

  @Transactional
  def save(FeesReceiptCommand command) {
    def feesReceipt = command.createFeesReceipt()
    feesReceipt.company = Company.get(params.company)
    feesReceipt.collaboratorName = BusinessEntity.get(params.businessEntity).toString()
    feesReceipt.bankAccount = BankAccount.get(params.bankAccount)

    if (!feesReceipt.validate()){
      render view:'create', model:[feesReceipt:feesReceipt]
      return
    }

    if (feesReceipt.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond feesReceipt.errors, view:'create'
      return
    }

    feesReceipt.save flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'feesReceipt.created', args: [message(code: 'feesReceipt.label', default: 'FeesReceipt'), feesReceipt.id])
        redirect feesReceipt
      }
      '*' { respond feesReceipt, [status: CREATED] }
    }
  }

  def edit(FeesReceipt feesReceipt) {
    respond feesReceipt
  }

  @Transactional
  def update(FeesReceiptCommand command) {
    def feesReceipt = FeesReceipt.get(params.id)
    def businessEntity = feesReceipt.businessEntity

    feesReceipt.properties = command.createFeesReceipt().properties
    feesReceipt.businessEntity = businessEntity

    if (feesReceipt.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond feesReceipt.errors, view:'edit'
      return
    }

    feesReceipt.save flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.updated.message', args: [message(code: 'feesReceipt.label', default: 'FeesReceipt'), feesReceipt.id])
        redirect feesReceipt
      }
      '*'{ respond feesReceipt, [status: OK] }
    }
  }

  @Transactional
  def delete(FeesReceipt feesReceipt) {

    if (feesReceipt == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    feesReceipt.delete flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'feesReceipt.label', default: 'FeesReceipt'), feesReceipt.id])
        redirect action:"index", method:"GET"
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'feesReceipt.label', default: 'FeesReceipt'), params.id])
        redirect action: "index", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }
}
