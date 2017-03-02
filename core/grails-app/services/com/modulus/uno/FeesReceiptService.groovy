package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class FeesReceiptService {

  DocumentService documentService
  RestService restService
  def emailSenderService
  def grailsApplication

  def addAuthorizationToFeesReceipt(FeesReceipt feesReceipt, User user){
    Authorization authorization = new Authorization(user:user)
    feesReceipt.addToAuthorizations(authorization)
    feesReceipt.save flush:true
    feesReceipt
  }

  def authorizeFeesReceipt(FeesReceipt feesReceipt) {
    feesReceipt.status = FeesReceiptStatus.AUTORIZADA
    feesReceipt.save flush:true
    emailSenderService.notifyFeesReceiptChangeStatus(feesReceipt)
  }

  BigDecimal getTotalFeesReceiptAuthorizedOfCompany(Company company){
    FeesReceipt.findAllByCompanyAndStatus(company, FeesReceiptStatus.AUTORIZADA).netAmount.sum()
  }

  Boolean isFullAuthorize(FeesReceipt feesReceipt){
    (feesReceipt.authorizations?.size() ?: 0) >= feesReceipt.company.numberOfAuthorizations
  }

  def addDocumentToFeesReceipt(def document, FeesReceipt feesReceipt, String type){
    documentService.uploadDocumentForOrder(document,type,feesReceipt)
  }

  def executeFeesReceipt(FeesReceipt feesReceipt){
    def command = createFeesReceiptCommand(feesReceipt)
    restService.sendCommandWithAuth(command, grailsApplication.config.modulus.receiptCreate)
    feesReceipt.status = FeesReceiptStatus.EJECUTADA
    feesReceipt.save flush:true
    emailSenderService.notifyFeesReceiptChangeStatus(feesReceipt)
    feesReceipt
  }

  def sendToAuthorize(FeesReceipt feesReceipt) {
    feesReceipt.status = FeesReceiptStatus.POR_AUTORIZAR
    feesReceipt.save flush:true
    emailSenderService.notifyFeesReceiptChangeStatus(feesReceipt)
  }

  private def createFeesReceiptCommand(FeesReceipt feesReceipt){
    new FeesReceiptModulusunoCommand(
      uuid:feesReceipt.company.accounts.first().timoneUuid,
      clabe:feesReceipt.bankAccount.clabe,
      bankCode:feesReceipt.bankAccount.banco.bankingCode,
      amount: feesReceipt.amount + feesReceipt.iva - feesReceipt.ivaWithHolding - feesReceipt.isr,
      iva:feesReceipt.ivaWithHolding,
      isr:feesReceipt.isr,
      beneficiary:feesReceipt.collaboratorName,
      concept:"HONORARIOS",
      payerName:feesReceipt.company.accounts.first().aliasStp,
      payerClabe:feesReceipt.company.accounts.first().stpClabe
    )
  }

}
