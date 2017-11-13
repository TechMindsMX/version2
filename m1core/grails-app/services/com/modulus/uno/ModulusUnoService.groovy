package com.modulus.uno

import grails.transaction.Transactional
import java.math.RoundingMode

import com.modulus.uno.stp.StpService

@Transactional
class ModulusUnoService {

  def restService
  def corporateService
  def grailsApplication
  StpService stpService
  TransactionService transactionService
  def stpClabeService
  def commissionTransactionService

  static final commissionType = [
    SaleOrder : "FACTURA",
    LoanOrder : "PRESTAMO",
    PurchaseOrder : "PAGO",
    CashOutOrder : "PAGO"
  ]

  static final cashOutConcept = [
    PurchaseOrder : "PAGO A PROVEEDOR",
    CashOutOrder : "RETIRO"
  ]

  private FeeCommand createFeeCommandFromOrder(def order){
    def command = null
    String comType = commissionType."${order.class.simpleName}"
    Commission commission = order.company.commissions.find { com ->
        com.type == CommissionType."${comType}"
    }

    BigDecimal amountFee = 0
    if (commission){
      if (commission.fee){
        amountFee = commission.fee * 1.0
      } else {
        amountFee = order.class.simpleName == "SaleOrder" ? order.total * (commission.percentage/100) : order.amount * (commission.percentage/100)
      }
      command = new FeeCommand(companyId:order.company.id, amount:amountFee.setScale(2, RoundingMode.HALF_UP), type:commission.type)
    }
    command
  }

  private FeeCommand createFeeCommandFromPurchaseOrder(PurchaseOrder order, BigDecimal amount){
    def command = null
    Commission commission = order.company.commissions.find { com ->
        com.type == CommissionType."PAGO"
    }

    BigDecimal amountFee = 0
    if (commission){
      if (commission.fee){
        amountFee = commission.fee * 1.0
      } else {
        amountFee = amount * (commission.percentage/100)
      }
      command = new FeeCommand(companyId:order.company.id, amount:amountFee.setScale(2, RoundingMode.HALF_UP),type:commission.type)
    }
    command
  }

  def applyTransferFunds (TransferFundsCommand command) {
    restService.sendCommandWithAuth(command, grailsApplication.config.modulus.loanCreate)
  }

  def approveCashOutOrder(CashOutOrder cashOutOrder) {
    //TODO Todo esto deberia de estar en otro servicio que no sea este ya que seria el que orqueteste
    //todas las llamadas
    FeeCommand feeCommand = createFeeCommandFromOrder(cashOutOrder)
    if (!feeCommand){
      throw new CommissionException("No existe comisión para la operación")
    }
    if (!cashOutOrder.company.accounts.first().aliasStp){
      throw new BusinessException("No se puede procesar el pago porque la cuenta de la empresa aún no está dada de alta en STP")
    }


    BigDecimal amount = cashOutOrder.amount.setScale(2, RoundingMode.HALF_UP)
    String payerName = cashOutOrder.company.bussinessName.length() > 40 ? cashOutOrder.company.bussinessName.substring(0,40).trim() : cashOutOrder.company.bussinessName.trim()
    def data = [
      institucionContraparte: cashOutOrder.account.banco.bankingCode,
      empresa: cashOutOrder.company.accounts?.first()?.aliasStp,
      fechaDeOperacion: "${new Date().format("yyyyMMdd")}",
      folioOrigen: "",
      claveDeRastreo: "${new Date().time}",
      institucionOperante: grailsApplication.config.stp.institutionOperation,
      montoDelPago: amount.setScale(2,RoundingMode.HALF_UP),
      tipoDelPago: "1",
      tipoDeLaCuentaDelOrdenante: "",
      nombreDelOrdenante: payerName,
      cuentaDelOrdenante: "",
      rfcCurpDelOrdenante: "",
      tipoDeCuentaDelBeneficiario: grailsApplication.config.stp.typeAccount,
      nombreDelBeneficiario: payerName,
      cuentaDelBeneficiario: cashOutOrder.account.clabe,
      rfcCurpDelBeneficiario: "NA",
      emailDelBeneficiario: getMailFromLegalRepresentatitveCompany(cashOutOrder.company),
      tipoDeCuentaDelBeneficiario2: "",
      nombreDelBeneficiario2: "",
      cuentaDelBeneficiario2: "",
      rfcCurpDelBeneficiario2: "",
      conceptoDelPago: "${cashOutConcept.CashOutOrder} ID:${cashOutOrder.id}",
      conceptoDelPago2: "",
      claveDelCatalogoDeUsuario1: "",
      claveDelCatalogoDeUsuario2: "",
      claveDelPago: "",
      referenciaDeCobranza: "",
      referenciaNumerica: "1${new Date().format("yyMMdd")}",
      tipoDeOperacion: "",
      topologia: "",
      usuario: "",
      medioDeEntrega: "",
      prioridad: "",
      iva: ""
    ]
    String keyTransaction = stpService.sendPayOrder(data)
    log.info "Key transaction stp: ${keyTransaction}"
    Map parameters = [keyTransaction:keyTransaction,trackingKey:data.claveDeRastreo,
    amount:data.montoDelPago,paymentConcept:data.conceptoDelPago,keyAccount:cashOutOrder.company.accounts?.first()?.stpClabe,
    referenceNumber:data.referenciaNumerica,transactionType:TransactionType.WITHDRAW,
    transactionStatus:TransactionStatus.AUTHORIZED]
    Transaction transaction = new Transaction(parameters)
    transactionService.saveTransaction(transaction)
    feeCommand.transactionId = transaction.id
    commissionTransactionService.saveCommissionTransaction(feeCommand)
    transaction
  }

  private String getMailFromLegalRepresentatitveCompany(Company company) {
    def users = corporateService.findLegalRepresentativesOfCompany(company.id)
    users ? users.first().profile.email : ""
  }

  def consultBalanceOfAccount(String account) {
    transactionService.getBalanceByKeyAccountPriorToDate(account, new Date())
  }

  def consultBalanceIntegratorOfType(String type) {
    def balance = restService.getBalancesIntegrator(type, grailsApplication.config.modulus.integratorBalance)
    [balance.balance, balance.usd]
  }

  def cashInWithCommissionFromSaleOrder(SaleOrder order){
    FeeCommand feeCommand = createFeeCommandFromOrder(order)
    if (!feeCommand){
      throw new CommissionException("No existe comisión para la operación")
    }

    CashinWithCommissionCommand command = new CashinWithCommissionCommand(uuid:order.company.accounts.first().timoneUuid, amount:order.total.setScale(2, RoundingMode.HALF_UP), fee:feeCommand.amount, feeType:feeCommand.type, concept:"FACTURA ID:${order.id}, ${order.clientName.toUpperCase()}, ${order.rfc}")
    def cashinResult = restService.sendCommandWithAuth(command, grailsApplication.config.modulus.cashin)
    cashinResult
  }

  def payPurchaseOrder(PurchaseOrder order, BigDecimal amountToPay) {
    FeeCommand feeCommand = createFeeCommandFromPurchaseOrder(order, amountToPay)
    if (!feeCommand){
      throw new CommissionException("No existe comisión para la operación")
    }

    if (!order.company.accounts.first().aliasStp){
      throw new BusinessException("No se puede procesar el pago porque la cuenta de la empresa aún no está dada de alta en STP")
    }

    String fullConcept = "${cashOutConcept.PurchaseOrder} ID:${order.id}, ${order.providerName.toUpperCase()}"
    String adjustConcept = fullConcept.length() > 40 ? fullConcept.substring(0,40).trim() : fullConcept.trim()
    String payerName = order.company.bussinessName.length() > 40 ? order.company.bussinessName.substring(0,40).trim() : order.company.bussinessName.trim()
    String beneficiaryName = order.providerName.length() > 40 ? order.providerName.substring(0,40).trim() : order.providerName.trim()
    def data = [
        institucionContraparte: order.bankAccount.banco.bankingCode,
        empresa: order.company.accounts?.first()?.aliasStp,
        fechaDeOperacion: "${new Date().format("yyyyMMdd")}",
        folioOrigen: "",
        claveDeRastreo: "${new Date().time}",
        institucionOperante: grailsApplication.config.stp.institutionOperation,
        montoDelPago: amountToPay.setScale(2, RoundingMode.HALF_UP),
        tipoDelPago: "1",
        tipoDeLaCuentaDelOrdenante: "",
        nombreDelOrdenante: payerName,
        cuentaDelOrdenante: "",
        rfcCurpDelOrdenante: "",
        tipoDeCuentaDelBeneficiario: grailsApplication.config.stp.typeAccount,
        nombreDelBeneficiario: beneficiaryName,
        cuentaDelBeneficiario: order.bankAccount.clabe,
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
        tipoDeOperacion: "",
        topologia: "",
        usuario: "",
        medioDeEntrega: "",
        prioridad: "",
        iva: ""
    ]
    String keyTransaction = stpService.sendPayOrder(data)
    Map parameters = [keyTransaction:keyTransaction,trackingKey:data.claveDeRastreo,
    amount:data.montoDelPago,paymentConcept:data.conceptoDelPago,keyAccount:order.company.accounts?.first()?.stpClabe,
    referenceNumber:data.referenciaNumerica,transactionType:TransactionType.WITHDRAW,
    transactionStatus:TransactionStatus.AUTHORIZED]
    Transaction transaction = new Transaction(parameters)
    transactionService.saveTransaction(transaction)
    feeCommand.transactionId = transaction.id
    commissionTransactionService.saveCommissionTransaction(feeCommand)
    transaction
  }

  def generedModulusUnoAccountByCompany(Company company) {
    ModulusUnoAccount account = new ModulusUnoAccount()
    account.account = ""
    account.balance = ""
    account.integraUuid = company.uuid
    account.stpClabe = stpClabeService.generateSTPMainAccount(grailsApplication.config.modulus.stpPayerAccount, 3)
    account.timoneUuid = ""
    account.company = company
    account.save()
    account
  }

  def getTransactionsInPeriodOfIntegrator(AccountStatementCommand command){
    restService.getTransactionsIntegrator(command, grailsApplication.config.modulus.integratorTransactions)
  }

  def generateSubAccountStpForClient(CreateAccountCommand command) {
    def accountResult = restService.sendCommandWithAuth(command, grailsApplication.config.modulus.subAccountUser)
    log.info "New Sub Account Registered ${accountResult}"
    accountResult?.json?.stpClabe
  }
}
