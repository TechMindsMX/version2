package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll
import grails.test.mixin.Mock

@TestFor(NotifyService)
@Mock([NotificationForState, GroupNotification, User, FeesReceipt, BusinessEntity, CashOutOrder, LoanOrder, LoanPaymentOrder, SaleOrder, Company, PurchaseOrder,Corporate,SaleOrderItem, Bank, BankAccount, PaymentToPurchase, Transaction, ModulusUnoAccount, Payment, ComposeName, PurchaseOrderItem])
class NotifyServiceSpec extends Specification {

  GrailsApplicationMock grailsApplication = new GrailsApplicationMock()
  CorporateService corporateService = Mock(CorporateService)
  RestService restService = Mock(RestService)

  static final URL = "makingdevs-qa.modulusuno.com"

  def setup(){
    service.grailsApplication = grailsApplication
    service.corporateService = corporateService
    service.restService = restService
  }

  @Unroll("Obtain the params when the SALEORDER is #status")
  void "obtain the params for SaleOrder Status to populate the email"(){
    given:"a sale order"
      def saleOrder = new SaleOrder(rfc:"MDE130712JA6", clientName:"PatitoAC" ,rejectReason: RejectReason.DOCUMENTO_INVALIDO, comments:"fake")
      def item = new SaleOrderItem(sku:'A001',name:'Gazelle A25',price:new BigDecimal(0.0), ieps:new BigDecimal(0.0), iva:new BigDecimal(0.0), unitType:"UNIDAD", currencyType:CurrencyType.PESOS)
      saleOrder.addToItems(item)
      saleOrder.payments = []
      saleOrder.save(validate:false)
    and:
      def company = new Company().save(validate:false)
      saleOrder.company = company
      saleOrder.save(validate:false)
    and:
      Corporate corporate = new Corporate(nameCorporate:"makingdevs", corporateUrl:"makingdevs").save()
      corporate.addToCompanies(company)
      corporate.save()
    and:
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${grailsApplication.config.grails.plugin.awssdk.domain.base.url}"
    when:"we extract the params"
      def params = service.prepareParametersToSendForSaleOrder(saleOrder, status)
    then:"we should get"
      params == expectedParams
    where:
      status << [
      SaleOrderStatus.CREADA,
      SaleOrderStatus.POR_AUTORIZAR,
      SaleOrderStatus.AUTORIZADA,
      SaleOrderStatus.RECHAZADA,
      SaleOrderStatus.PAGADA,
      SaleOrderStatus.EJECUTADA,
      SaleOrderStatus.CANCELADA,
      SaleOrderStatus.CANCELACION_POR_AUTORIZAR,
      SaleOrderStatus.CANCELACION_AUTORIZADA,
      SaleOrderStatus.CANCELACION_EJECUTADA
    ]
    expectedParams << [
      [id:"1", clientName:"PatitoAC", rfc:"MDE130712JA6", status:"CREADA", url:URL],
      [id:"1", clientName:"PatitoAC", rfc:"MDE130712JA6", status:"PUESTA EN ESPERA DE SER AUTORIZADA", url:URL],
      [id:"1", clientName:"PatitoAC", rfc:"MDE130712JA6", status:"AUTORIZADA", url:URL],
      [id:"1", rfc:"MDE130712JA6", clientName:"PatitoAC" ,rejectReason: RejectReason.DOCUMENTO_INVALIDO.toString(), comments:"fake", status:"RECHAZADA", url:URL],
      [id:"1", clientName:"PatitoAC", rfc:"MDE130712JA6", status:"PAGADA", url:URL],
      [id:"1", clientName:"PatitoAC", rfc:"MDE130712JA6", status:"EJECUTADA", url:URL],
      [id:"1", rfc:"MDE130712JA6", clientName:"PatitoAC" ,rejectReason: RejectReason.DOCUMENTO_INVALIDO.toString(), comments:"fake", status:"CANCELADA", url:URL],
      [id:"1", rfc:"MDE130712JA6", clientName:"PatitoAC" ,rejectReason: RejectReason.DOCUMENTO_INVALIDO.toString(), comments:"fake", status:"CANCELADA POR AUTORIZAR", url:URL],
      [id:"1", rfc:"MDE130712JA6", clientName:"PatitoAC" ,rejectReason: RejectReason.DOCUMENTO_INVALIDO.toString(), comments:"fake", status:"CANCELACION AUTORIZADA", url:URL],
      [id:"1", rfc:"MDE130712JA6", clientName:"PatitoAC" ,rejectReason: RejectReason.DOCUMENTO_INVALIDO.toString(), comments:"fake", status:"CANCELACION EJECUTADA", url:URL]
    ]
   }

  @Unroll("Obtain the params when the CashOutOrder is #status")
  void "obtain the params for CashOut Order Status to populate the email"(){
    given:"a cashOut order"
      def cashOutOrder = new CashOutOrder(amount:9999, comments:"falsa", rejectReason:RejectReason.DOCUMENTO_INVALIDO)
      Transaction transaction = new Transaction(paymentConcept:"Concepto", trackingKey:"Rastreo", referenceNumber:"Referencia", dateCreated:Date.parse("dd-MM-yyyy hh:mm:ss", "10-06-2017 10:30:15")).save(validate:false)
      cashOutOrder.transaction = transaction
      BankAccount bankAccount = new BankAccount(banco:new Bank(name:"ElBanco").save(validate:false), clabe:"Clabe").save(validate:false)
      cashOutOrder.account = bankAccount
      cashOutOrder.save(validate:false)
    and:
      def company = new Company().save(validate:false)
      ModulusUnoAccount m1Account = new ModulusUnoAccount(aliasStp:"AliasStp").save(validate:false)
      company.addToAccounts(m1Account)
      company.save(validate:false)
      cashOutOrder.company = company
      cashOutOrder.save(validate:false)
    and:
      Corporate corporate = new Corporate(nameCorporate:"makingdevs", corporateUrl:"makingdevs").save()
      corporate.addToCompanies(company)
      corporate.save()
    and:
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${grailsApplication.config.grails.plugin.awssdk.domain.base.url}"
    when:"we extract the params"
    def params = service.parametersForCashOutOrder(cashOutOrder, status)
    then:"we should get"
    params == expectedParams
    where:
    status << [
      CashOutOrderStatus.CREATED,
      CashOutOrderStatus.IN_PROCESS,
      CashOutOrderStatus.TO_AUTHORIZED,
      CashOutOrderStatus.AUTHORIZED,
      CashOutOrderStatus.REJECTED,
      CashOutOrderStatus.EXECUTED,
      CashOutOrderStatus.CANCELED
      ]

    expectedParams <<[
      [id:"1", amount:"9999", status:"CREADA", url:URL],
      [id:"1", amount:"9999", status:"EN PROCESO", url:URL],
      [id:"1", amount:"9999", status:"PUESTA EN ESPERA DE SER AUTORIZADA", url:URL],
      [id:"1", amount:"9999", status:"AUTORIZADA", url:URL],
      [id:"1", amount:"9999", status:"RECHAZADA", comments:"falsa", rejectReason:RejectReason.DOCUMENTO_INVALIDO.toString(), url:URL],
      [id:"1", amount:"9999", status:"EJECUTADA", url:URL, 'paymentConcept':'Concepto', 'trackingKey':'Rastreo', 'referenceNumber':'Referencia', 'dateCreated':'10-06-2017 10:30:15', 'destinyBank':'ElBanco','destinyBankAccount':'Clabe', 'aliasStp':'AliasStp'],
      [id:"1", amount:"9999", status:"CANCELADA", comments:"falsa", rejectReason:RejectReason.DOCUMENTO_INVALIDO.toString(), url:URL]
      ]
  }

  @Unroll("Obtain the params when the Purchase Order is #status")
  void "obtain the params for Purchase Order Status to populate the email"(){
    given:"a purchase order"
      def purchaseOrder = new PurchaseOrder(providerName:"Fake Inc", rejectReason: RejectReason.DOCUMENTO_INVALIDO, rejectComment:"Fake")
      purchaseOrder.save(validate:false)
			PurchaseOrderItem item = new PurchaseOrderItem(name:"Item", unitType:"PIECE").save(validate:false)
			purchaseOrder.addToItems(item)
			purchaseOrder.save(validate:false)
    and:
      def company = new Company().save(validate:false)
      purchaseOrder.company = company
      purchaseOrder.save(validate:false)
    and:
      Corporate corporate = new Corporate(nameCorporate:"makingdevs", corporateUrl:"makingdevs").save()
      corporate.addToCompanies(company)
      corporate.save()
    and:
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${grailsApplication.config.grails.plugin.awssdk.domain.base.url}"
    when:"we extract the params"
    def params = service.parametersForPurchaseOrder(purchaseOrder, status)
    then:"we should get"
    params == expectedParams
    where:
    status << [
      PurchaseOrderStatus.CREADA,
      PurchaseOrderStatus.POR_AUTORIZAR,
      PurchaseOrderStatus.AUTORIZADA,
      PurchaseOrderStatus.RECHAZADA,
      PurchaseOrderStatus.PAGADA,
      PurchaseOrderStatus.CANCELADA
      ]
    expectedParams <<[
      [id:"1", providerName:"Fake Inc", status:"CREADA", url:URL],
      [id:"1", providerName:"Fake Inc", status:"PUESTA EN ESPERA DE SER AUTORIZADA", url:URL],
      [id:"1", providerName:"Fake Inc", status:"AUTORIZADA", url:URL],
      [id:"1", providerName:"Fake Inc", rejectComment:"Fake", rejectReason:RejectReason.DOCUMENTO_INVALIDO.toString(), status:"RECHAZADA", url:URL ],
      [id:"1", providerName:"Fake Inc", rejectComment:"Fake", rejectReason:RejectReason.DOCUMENTO_INVALIDO.toString(), status:"PAGADA", url:URL],
      [id:"1", providerName:"Fake Inc", rejectComment:"Fake", rejectReason:RejectReason.DOCUMENTO_INVALIDO.toString(), status:"CANCELADA", url:URL]
    ]
  }

  @Unroll("Obtain the params when the Loan Order is #status")
  void "obtain the params for Loan Order Status to populate the email"(){
    given:"a loan order"
    def loanOrder = new LoanOrder (amount:88888, rejectComment:"Mentiroso", rejectReason: RejectReason.DOCUMENTO_INVALIDO)
    loanOrder.save(validate:false)
    and:
      def company = new Company().save(validate:false)
      loanOrder.company = company
      loanOrder.save(validate:false)
    and:
      Corporate corporate = new Corporate(nameCorporate:"makingdevs", corporateUrl:"makingdevs").save()
      corporate.addToCompanies(company)
      corporate.save()
    and:
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${grailsApplication.config.grails.plugin.awssdk.domain.base.url}"
    when:"we extract the params"
    def params = service.parametersForLoanOrder(loanOrder, status)
    then:"we should get"
    params == expectedParams
    where:
    status << [
      LoanOrderStatus.CREATED,
      LoanOrderStatus.VALIDATE,
      LoanOrderStatus.AUTHORIZED,
      LoanOrderStatus.EXECUTED,
      LoanOrderStatus.APPROVED,
      LoanOrderStatus.ACCEPTED,
      LoanOrderStatus.REJECTED,
      LoanOrderStatus.CANCELED
    ]
    expectedParams <<[
      [id:"1", amount:"88888", status:"CREADA", url:URL],
      [id:"1", amount:"88888", status:"PUESTA EN ESPERA DE SER AUTORIZADA", url:URL],
      [id:"1", amount:"88888", status:"AUTORIZADA", url:URL],
      [id:"1", amount:"88888", status:"EJECUTADA", url:URL],
      [id:"1", amount:"88888", status:"APROBADA", url:URL],
      [id:"1", amount:"88888", status:"ACEPTADA", url:URL],
      [id:"1", amount:"88888", status:"RECHAZADA", rejectComment:"Mentiroso", rejectReason:RejectReason.DOCUMENTO_INVALIDO.toString(), url:URL],
      [id:"1", amount:"88888", status:"CANCELADA", rejectComment:"Mentiroso", rejectReason:RejectReason.DOCUMENTO_INVALIDO.toString(), url:URL]
]
  }

  @Unroll("Obtain the params when the LoanPayment Order is #status")
  void "obtain the params for LoanPayment Order Status to populate the email"(){
    given:"a loan payment order"
      def loanPaymentOrder = new LoanPaymentOrder(amountInterest:10000, amountIvaInterest:20000, amountToCapital:30000, rejectComment:"Fake", rejectReason:RejectReason.DOCUMENTO_INVALIDO)
      loanPaymentOrder.save(validate:false)
    and:
      def company = new Company().save(validate:false)
      loanPaymentOrder.company = company
      loanPaymentOrder.save(validate:false)
    and:
      Corporate corporate = new Corporate(nameCorporate:"makingdevs", corporateUrl:"makingdevs").save()
      corporate.addToCompanies(company)
      corporate.save()
    and:
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${grailsApplication.config.grails.plugin.awssdk.domain.base.url}"
    when:"we extract the params"
    def params = service.parametersForLoanPaymentOrder(loanPaymentOrder, status)
    then:"we should get"
    params == expectedParams
    where:
    status << [
    LoanPaymentOrderStatus.CREATED,
    LoanPaymentOrderStatus.VALIDATE,
    LoanPaymentOrderStatus.AUTHORIZED,
    LoanPaymentOrderStatus.REJECTED,
    LoanPaymentOrderStatus.EXECUTED,
    LoanPaymentOrderStatus.CANCELED
    ]
    expectedParams <<[
    [id:"1", amountInterest:"10000", status:"CREADA", amountIvaInterest:"20000", amountToCapital:"30000", url:URL],
    [id:"1", amountInterest:"10000", status:"PUESTA EN ESPERA DE SER AUTORIZADA", amountIvaInterest:"20000", amountToCapital:"30000", url:URL],
    [id:"1", amountInterest:"10000", status:"AUTORIZADA", amountIvaInterest:"20000", amountToCapital:"30000", url:URL],
    [id:"1", amountInterest:"10000", status:"RECHAZADA", amountIvaInterest:"20000", amountToCapital:"30000", rejectComment:"Fake", rejectReason: RejectReason.DOCUMENTO_INVALIDO.toString(), url:URL],
    [id:"1", amountInterest:"10000", status:"EJECUTADA", amountIvaInterest:"20000", amountToCapital:"30000", url:URL],
    [id:"1", amountInterest:"10000", status:"CANCELADA", amountIvaInterest:"20000", amountToCapital:"30000", rejectComment:"Fake", rejectReason: RejectReason.DOCUMENTO_INVALIDO.toString(), url:URL]
]
     }

  @Unroll("Obtain the params when the Fees Receipt is #status")
  void "obtain the params for Fees Receipt Status to populate the email"(){
    given:"a company"
      def company = new Company("rfc":"qwerty123456", "bussinessName":"apple")
      ModulusUnoAccount m1Account = new ModulusUnoAccount(aliasStp:"AliasStp").save(validate:false)
      company.addToAccounts(m1Account)
      company.save(validate:false)
    and:
      Corporate corporate = new Corporate(nameCorporate:"makingdevs", corporateUrl:"makingdevs").save()
      corporate.addToCompanies(company)
      corporate.save()
    and: "a fees Receipt"
      def feesReceipt = new FeesReceipt(collaboratorName:"Empleado", rejectReason:RejectReason.DOCUMENTO_INVALIDO, comments:"fake", amount:new BigDecimal(100))
      feesReceipt.company = company
      Transaction transaction = new Transaction(paymentConcept:"Concepto", trackingKey:"Rastreo", referenceNumber:"Referencia", dateCreated:Date.parse("dd-MM-yyyy hh:mm:ss", "10-06-2017 10:30:15")).save(validate:false)
      feesReceipt.transaction = transaction
      BankAccount bankAccount = new BankAccount(banco:new Bank(name:"ElBanco").save(validate:false), clabe:"Clabe").save(validate:false)
      feesReceipt.bankAccount = bankAccount
      feesReceipt.save(validate:false)
    and:
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${grailsApplication.config.grails.plugin.awssdk.domain.base.url}"
    when:"we extract the params"
      def params = service.parametersForFeesReceipt(feesReceipt, status, company)
    then:"we should get"
      params == expectedParams
    where:
    status << [
    FeesReceiptStatus.CREADA,
    FeesReceiptStatus.POR_AUTORIZAR,
    FeesReceiptStatus.AUTORIZADA,
    FeesReceiptStatus.EJECUTADA,
    FeesReceiptStatus.CANCELADA,
    FeesReceiptStatus.RECHAZADA
    ]
    expectedParams <<[
    ['id':"1", 'company':'apple', 'status':'CREADA', 'url':URL],
    ['id':"1", 'company':'apple', 'status':'PUESTA EN ESPERA DE SER AUTORIZADA', 'url':URL],
    ['id':"1", 'company':'apple', 'status':'AUTORIZADA', 'url':URL],
    ['id':"1", 'collaboratorName':'Empleado', 'company':'apple', 'status':'EJECUTADA', 'url':URL, 'amount':'95.33', 'paymentConcept':'Concepto', 'trackingKey':'Rastreo', 'referenceNumber':'Referencia', 'dateCreated':'10-06-2017 10:30:15', 'destinyBank':'ElBanco','destinyBankAccount':'Clabe', 'aliasStp':'AliasStp' ],
    ['id':"1", 'company':'apple', 'status':'CANCELADA', 'rejectReason': RejectReason.DOCUMENTO_INVALIDO.toString(), 'comments':'fake', 'url':URL],
    ['id':"1", 'company':'apple', 'status':'RECHAZADA', 'rejectReason': RejectReason.DOCUMENTO_INVALIDO.toString(), 'comments':'fake', 'url':URL]
    ]
  }

  void "Get parameters for build params of Email when add a Provider, a Client, and Employee"(){
    given:"A new Provider, new Client and new Employee"
      def provider = new BusinessEntity( rfc:'abc123456', name:'patitoABC')
      def client = new BusinessEntity( rfc:'pasc123456', name:'carlo')
      def employee = new BusinessEntity( rfc:'sara123456', name:'karlo')
      provider.save(validate:false)
      client.save(validate:false)
      employee.save(validate:false)
    and: " A company"
      def company = new Company('rfc':'qwerty123456', 'bussinessName':'PAtitoABC')
      company.save(validate:false)
    and:
      Corporate corporate = new Corporate(nameCorporate:"makingdevs", corporateUrl:"makingdevs").save()
      corporate.addToCompanies(company)
      corporate.save()
    and:
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${grailsApplication.config.grails.plugin.awssdk.domain.base.url}"
    when:"we extract the params"
    def paramsProvider = service.parametersForBusinessEntity(provider, company)
    def paramsClient = service.parametersForBusinessEntity(client, company)
    def paramsEmployee = service.parametersForBusinessEntity(employee, company)
    then:"we should get"
    paramsProvider == ['id':"1", 'rfc':'abc123456', company:"PAtitoABC", url:URL]
    paramsClient == ['id':"2", 'rfc':'pasc123456', company:"PAtitoABC", url:URL]
    paramsEmployee == ['id':"3", 'rfc':'sara123456', company:"PAtitoABC", url:URL]
    }

  void "Get parameters for build params when we need confirm account or password recovery"(){
    given:"A string token"
      String token = "www.token.com/integradora/here/pick/me"
    and: "A map"
      def message=[:]
      message.token = token
    when:"We need the email with tokenURL"
      def params = service.parametersForRecoveryToken(message)
    then:"We should get the next map of params"
      params == [token:"www.token.com/integradora/here/pick/me"]
  }

  void "Build email Map without receivers, with: id template and a map of params"(){
    given:"A map of params"
      def params = ["a":1]
    and: "Emailer Id"
      def idEmailer= "12345678qwerty"
    and: "Email to send"
      def toSend="hi@me.com"
    when:"Build the email map to send to EmailerApp v2"
      def emailMap = service.buildEmailerMap(idEmailer, toSend,  params)
    then:"We should get a map"
      emailMap == [id:"12345678qwerty", to:"hi@me.com", subject:"Mensaje de Modulus Uno", params:["a":1] ]
  }

  void "Send emails notifications for a group"(){
    given:"A group notification"
      def group = new GroupNotification(name:"Contadores", notificationId:"notificationId123", users:usersToNotify()).save(validate:false)
    and:"A list of notifications"
      def notification1 = new NotificationForState(groupNotification:1, stateMachine:2).save(validate:false)
      def notification2 = new NotificationForState(groupNotification:1, stateMachine:2).save(validate:false)
      def notifys = [notification1, notification2]
    and:"a emailer params"
      def emailerParams = ["tag1":"Emailer test", "tag2":"Message to send"]
    when:"We want to send email to every user from the group notification"
      service.sendEmailToGroup(notifys, emailerParams)
    then:"We should call the rest service for every user to notify  "
      4 * restService.sendEmailToEmailer(_)
  }

  private usersToNotify(){
    (1..2).collect {
      def u = new User(username:"user$it", profile: new Profile(email:"user$it@modulus.uno")).save(validate:false)
      u
    }
  }

  void "obtain the params for payment to Purchase Order"(){
    given:"a purchase order"
      BankAccount bankAccount = new BankAccount(clabe:"Clabe", banco:new Bank(name:"BankName").save(validate:false)).save(validate:false)
      def purchaseOrder = new PurchaseOrder(providerName:"Fake Inc", bankAccount:bankAccount)
			PurchaseOrderItem item = new PurchaseOrderItem(name:"Item", unitType:"PIECE").save(validate:false)
			purchaseOrder.addToItems(item)
      purchaseOrder.save(validate:false)
    and:
      ModulusUnoAccount m1Account = new ModulusUnoAccount(aliasStp:"AliasStp").save(validate:false)
      def company = new Company().save(validate:false)
      company.addToAccounts(m1Account)
      company.save(validate:false)
      purchaseOrder.company = company
      Transaction transaction = new Transaction(paymentConcept:"Concepto", trackingKey:"Rastreo", referenceNumber:"Referencia").save(validate:false)
      PaymentToPurchase payment = new PaymentToPurchase(amount:new BigDecimal(1500), transaction:transaction, dateCreated:new Date()).save(validate:false)
      purchaseOrder.addToPayments(payment)
      purchaseOrder.save(validate:false)
    and:
      Corporate corporate = new Corporate(nameCorporate:"makingdevs", corporateUrl:"makingdevs").save()
      corporate.addToCompanies(company)
      corporate.save()
    and:
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${grailsApplication.config.grails.plugin.awssdk.domain.base.url}"
    when:"we extract the params"
    def params = service.parametersForPaymentToPurchase(purchaseOrder)
    then:"we should get"
    params.id == 1
    params.providerName == "Fake Inc"
    params.paymentConcept == "Concepto"
    params.trackingKey == "Rastreo"
    params.referenceNumber == "Referencia"
    params.amount == "1500"
    params.dateCreated.substring(0,10) == payment.dateCreated.format("dd-MM-yyyy")
    params.destinyBank == "BankName"
    params.destinyBankAccount == "Clabe"
    params.aliasStp == "AliasStp"
    params.url == URL
  }

  void "obtain the params for stp deposit when payment isn't from any client"(){
    given:"the payment"
      def company = new Company().save(validate:false)
      Transaction transaction = new Transaction(paymentConcept:"Concepto", trackingKey:"Rastreo", referenceNumber:"Referencia").save(validate:false)
      Payment payment = new Payment(amount:new BigDecimal(1000), dateCreated:new Date(), transaction:transaction, company:company).save(validate:false)
    and:
      Corporate corporate = new Corporate(nameCorporate:"makingdevs", corporateUrl:"makingdevs").save()
      corporate.addToCompanies(company)
      corporate.save()
    and:
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${grailsApplication.config.grails.plugin.awssdk.domain.base.url}"
    when:"we extract the params"
    def params = service.parametersForStpDeposit(payment)
    then:"we should get"
    params.paymentConcept == "Concepto"
    params.trackingKey == "Rastreo"
    params.referenceNumber == "Referencia"
    params.amount == "1000"
    params.dateCreated == payment.dateCreated.format("dd-MM-yyyy hh:mm:ss")
    params.company == "NO IDENTIFICADO"
    params.url == URL
  }

  void "obtain the params for stp deposit when payment is from any client"(){
    given:"the payment"
      def company = new Company().save(validate:false)
      Transaction transaction = new Transaction(paymentConcept:"Concepto", trackingKey:"Rastreo", referenceNumber:"Referencia").save(validate:false)
      Payment payment = new Payment(amount:new BigDecimal(1000), dateCreated:Date.parse("dd-MM-yyyy hh:mm:ss", "10-06-2017 10:30:15"), transaction:transaction, company:company, rfc:"RFC").save(validate:false)
    and:
      Corporate corporate = new Corporate(nameCorporate:"makingdevs", corporateUrl:"makingdevs").save()
      corporate.addToCompanies(company)
      corporate.save()
    and:
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${grailsApplication.config.grails.plugin.awssdk.domain.base.url}"
    and:
      BusinessEntity businessEntity = new BusinessEntity(type:BusinessEntityType.MORAL).save(validate:false)
      ComposeName name = new ComposeName(value:"Client", type:NameType.RAZON_SOCIAL).save(validate:false)
      businessEntity.addToNames(name)
      BusinessEntity.metaClass.static.findByRfc = { businessEntity }
    when:"we extract the params"
    def params = service.parametersForStpDeposit(payment)
    then:"we should get"
    params.paymentConcept == "Concepto"
    params.trackingKey == "Rastreo"
    params.referenceNumber == "Referencia"
    params.amount == "1000"
    params.dateCreated == "10-06-2017 10:30:15"
    params.company == "Client"
    params.url == URL
  }

}
