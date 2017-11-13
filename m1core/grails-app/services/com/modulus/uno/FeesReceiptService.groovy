package com.modulus.uno

import grails.transaction.Transactional
import java.math.RoundingMode

import com.modulus.uno.stp.StpService

@Transactional
class FeesReceiptService {

  DocumentService documentService
  StpService stpService
  def emailSenderService
  def grailsApplication
  TransactionService transactionService
  CommissionTransactionService commissionTransactionService

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
    def data = sendPaymentToStp(feesReceipt)
    Transaction transaction = saveTransaction(feesReceipt, data)
    feesReceipt.status = FeesReceiptStatus.EJECUTADA
    feesReceipt.transaction = transaction
    feesReceipt.save()
    registerCommissionForFeesReceipt(feesReceipt)
    emailSenderService.notifyFeesReceiptChangeStatus(feesReceipt)
    feesReceipt
  }

  private def sendPaymentToStp(FeesReceipt feesReceipt) {
    String fullConcept = "HONORARIOS ID:${feesReceipt.id}, ${feesReceipt.collaboratorName}"
    String adjustConcept = fullConcept.length() > 40 ? fullConcept.substring(0,40) : fullConcept
    def data = [
        institucionContraparte: feesReceipt.bankAccount.banco.bankingCode,
        empresa: feesReceipt.company.accounts.first().aliasStp,
        fechaDeOperacion: new Date().format("yyyyMMdd"),
        folioOrigen: "",
        claveDeRastreo: "${new Date().time}",
        institucionOperante: grailsApplication.config.stp.institutionOperation,
        montoDelPago: feesReceipt.netAmount.setScale(2, RoundingMode.HALF_UP),
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
    data.keyTransaction = stpService.sendPayOrder(data)
    data
  }

  private Transaction saveTransaction(FeesReceipt feesReceipt, def data) {
    Map parameters = [
      keyTransaction:data.keyTransaction,
      trackingKey:data.claveDeRastreo,
      amount:data.montoDelPago,
      paymentConcept:data.conceptoDelPago,
      keyAccount:feesReceipt.company.accounts.first().stpClabe,
      referenceNumber:data.referenciaNumerica,transactionType:TransactionType.WITHDRAW,
      transactionStatus:TransactionStatus.AUTHORIZED
    ]
    Transaction transaction = new Transaction(parameters)
    transactionService.saveTransaction(transaction)
    transaction
  }

  def sendToAuthorize(FeesReceipt feesReceipt) {
    feesReceipt.status = FeesReceiptStatus.POR_AUTORIZAR
    feesReceipt.save flush:true
    emailSenderService.notifyFeesReceiptChangeStatus(feesReceipt)
  }

  private void registerCommissionForFeesReceipt(FeesReceipt feesReceipt) {
    FeeCommand feeCommand = createFeeCommand(feesReceipt)
    commissionTransactionService.saveCommissionTransaction(feeCommand)
  }

  private FeeCommand createFeeCommand(FeesReceipt feesReceipt) {
    def command = null
    Commission commission = feesReceipt.company.commissions.find { com ->
        com.type == CommissionType."PAGO"
    }

    if (!commission) {
      throw new BusinessException("No existe comisión de pago registrada")
    }

    BigDecimal netAmountFeesReceipt = feesReceipt.amount + feesReceipt.iva - feesReceipt.ivaWithHolding - feesReceipt.isr
    BigDecimal amountFee = 0
    if (commission){
      if (commission.fee){
        amountFee = commission.fee * 1.0
      } else {
        amountFee = netAmountFeesReceipt * (commission.percentage/100)
      }
      command = new FeeCommand(companyId:feesReceipt.company.id, amount:amountFee.setScale(2, RoundingMode.HALF_UP),type:commission.type, transactionId:feesReceipt.transaction.id)
    }
    command
  }

  FeesReceipt reverseFeesReceiptForTransaction(Transaction transaction) {
    FeesReceipt feesReceipt = FeesReceipt.findByTransaction(transaction)
    if (feesReceipt) {
      feesReceipt.status = FeesReceiptStatus.PAGO_DEVUELTO
      feesReceipt.save()
    }
    feesReceipt
  }

}
