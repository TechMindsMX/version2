package com.modulus.uno

import grails.transaction.Transactional
import java.math.RoundingMode

@Transactional
class ModulusUnoService {

  def restService
  def corporateService
  def grailsApplication
  StpService stpService
  TransactionService transactionService
  def stpClabeService

  static final feeType = [
    SaleOrder : "SALE_FEE",
    LoanOrder : "LOAN_FEE",
    PurchaseOrder : "PAYMENT_FEE",
    CashOutOrder : "CASHOUT_FEE"
  ]

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

    String fType = feeType."${order.class.simpleName}"

    BigDecimal amountFee = 0
    if (commission){
      if (commission.fee){
        amountFee = commission.fee * 1.0
      } else {
        amountFee = order.class.simpleName == "SaleOrder" ? order.total * (commission.percentage/100) : order.amount * (commission.percentage/100)
      }
      String uuid = order.company.accounts.first().timoneUuid
      command = new FeeCommand(uuid:uuid,amount:amountFee.setScale(2, RoundingMode.HALF_UP),type:fType)
    }
    command
  }

  private FeeCommand createFeeCommandFromPurchaseOrder(PurchaseOrder order, PaymentToPurchase payment){
    def command = null
    Commission commission = order.company.commissions.find { com ->
        com.type == CommissionType."PAGO"
    }

    String fType = feeType."${order.class.simpleName}"

    BigDecimal amountFee = 0
    if (commission){
      if (commission.fee){
        amountFee = commission.fee * 1.0
      } else {
        amountFee = payment.amount * (commission.percentage/100)
      }
      String uuid = order.company.accounts.first().timoneUuid
      command = new FeeCommand(uuid:uuid,amount:amountFee.setScale(2, RoundingMode.HALF_UP),type:fType)
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
    def data = [
      institucionContraparte: cashOutOrder.account.banco.bankingCode,
      empresa: cashOutOrder.company.accounts?.first()?.aliasStp,
      fechaDeOperacion: new Date().format("yyyyMMdd"),
      folioOrigen: "",
      claveDeRastreo: new Date().getTime().toString(),
      institucionOperante: grailsApplication.config.stp.institutionOperation,
      montoDelPago: amount,
      tipoDelPago: "1",
      tipoDeLaCuentaDelOrdenante: "",
      nombreDelOrdenante: cashOutOrder.company.bussinessName,
      cuentaDelOrdenante: "",
      rfcCurpDelOrdenante: "",
      tipoDeCuentaDelBeneficiario: grailsApplication.config.stp.typeAccount,
      nombreDelBeneficiario: cashOutOrder.company.bussinessName,
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
      tipoDeOperación: "",
      topologia: "",
      usuario: "",
      medioDeEntrega: "",
      prioridad: "",
      iva: ""
    ]
    String keyTransaction = stpService.sendPayOrder(data)
    Map parameters = [keyTransaction:keyTransaction,trackingKey:data.claveDeRastreo,
    amount:data.montoDelPago,paymentConcept:data.conceptoDelPago,keyAccount:cashOutOrder.company.accounts?.first()?.stpClabe,
    referenceNumber:data.referenciaNumerica,transactionType:TransactionType.WITHDRAW,
    transactionStatus:TransactionStatus.AUTHORIZED]
    Transaction transaction = new Transaction(parameters)
    transactionService.saveTransaction(transaction)
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

  def payPurchaseOrder(PurchaseOrder order, PaymentToPurchase payment) {
    FeeCommand feeCommand = createFeeCommandFromPurchaseOrder(order, payment)
    if (!feeCommand){
      throw new CommissionException("No existe comisión para la operación")
    }

    if (!order.company.accounts.first().aliasStp){
      throw new BusinessException("No se puede procesar el pago porque la cuenta de la empresa aún no está dada de alta en STP")
    }

    String fullConcept = "${cashOutConcept.PurchaseOrder} ID:${order.id}, ${order.providerName.toUpperCase()}"
    String adjustConcept = fullConcept.length() > 40 ? fullConcept.substring(0,40) : fullConcept
    def data = [
        institucionContraparte: order.bankAccount.banco.bankingCode,
        empresa: order.company.accounts?.first()?.aliasStp,
        fechaDeOperacion: new Date().format("yyyyMMdd"),
        folioOrigen: "",
        claveDeRastreo: new Date().toTimestamp(),
        institucionOperante: grailsApplication.config.stp.institutionOperation,
        montoDelPago: payment.amount.setScale(2, RoundingMode.HALF_UP),
        tipoDelPago: "1",
        tipoDeLaCuentaDelOrdenante: "",
        nombreDelOrdenante: order.company.bussinessName,
        cuentaDelOrdenante: "",
        rfcCurpDelOrdenante: "",
        tipoDeCuentaDelBeneficiario: grailsApplication.config.stp.typeAccount,
        nombreDelBeneficiario: order.providerName,
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
        tipoDeOperación: "",
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
