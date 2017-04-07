package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class FeesReceiptService {

  DocumentService documentService
  StpService stpService
  def emailSenderService
  def grailsApplication
  TransactionService transactionService

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
    String fullConcept = "HONORARIOS ID:${feesReceipt.id}, ${feesReceipt.collaboratorName}"
    String adjustConcept = fullConcept.length() > 40 ? fullConcept.substring(0,40) : fullConcept
    def data = [
        institucionContraparte: feesReceipt.bankAccount.banco.bankingCode,
        empresa: feesReceipt.company.accounts.first().aliasStp,
        fechaDeOperacion: new Date().format("yyyyMMdd"),  
        folioOrigen: "",
        claveDeRastreo: new Date().toTimestamp(),
        institucionOperante: grailsApplication.config.stp.institutionOperation,
        montoDelPago: feesReceipt.amount + feesReceipt.iva - feesReceipt.ivaWithHolding - feesReceipt.isr,
        tipoDelPago: "1",
        tipoDeLaCuentaDelOrdenante: "",
        nombreDelOrdenante: feesReceipt.company.bussinessName,
        cuentaDelOrdenante: "",
        rfcCurpDelOrdenante: "",
        tipoDeCuentaDelBeneficiario: grailsApplication.config.stp.typeAccount,
        nombreDelBeneficiario: feesReceipt.collaboratorName,
        cuentaDelBeneficiario: feesReceipt.bankAccount.clabe,
        rfcCurpDelBeneficiario: "NA",
        emailDelBeneficiario: "mailBeneficiary@mail.com",
        tipoDeCuentaDelBeneficiario2: "",
        nombreDelBeneficiario2: "",
        cuentaDelBeneficiario2: "",
        rfcCurpDelBeneficiario2: "",
        conceptoDelPago: adjustConcept,
        conceptoDelPago2: "",
        claveDelCatalogoDeUsuario1: "",
        claveDelCatalogoDeUsuario2: "",
        claveDelPago: "",
        referenciaDeCobranza: "",
        referenciaNumerica: "1${new Date().format("yyMMdd")}",
        tipoDeOperación: "",
        topologia: "",
        usuario: "",
        medioDeEntrega: "",
        prioridad: "",
        iva: ""
    ]
    String keyTransaction = stpService.sendPayOrder(data)
    Map parameters = [keyTransaction:keyTransaction,trackingKey:data.claveDeRastreo,
    amount:data.montoDelPago,paymentConcept:data.conceptoDelPago,keyAccount:feesReceipt.company.accounts.first().stpClabe,
    referenceNumber:data.referenciaNumerica,transactionType:TransactionType.WITHDRAW,
    transactionStatus:TransactionStatus.AUTHORIZED]
    Transaction transaction = new Transaction(parameters)
    transactionService.saveTransaction(transaction)
    feesReceipt.status = FeesReceiptStatus.EJECUTADA
    feesReceipt.transaction = transaction
    //TODO Esto no deberia de ser flush true
    feesReceipt.save flush:true
    emailSenderService.notifyFeesReceiptChangeStatus(feesReceipt)
    feesReceipt
  }

  def sendToAuthorize(FeesReceipt feesReceipt) {
    feesReceipt.status = FeesReceiptStatus.POR_AUTORIZAR
    feesReceipt.save flush:true
    emailSenderService.notifyFeesReceiptChangeStatus(feesReceipt)
  }
  
}
