package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll
import grails.test.mixin.Mock

@TestFor(NotifyService)
@Mock([User, FeesReceipt, BusinessEntity, CashOutOrder, LoanOrder, LoanPaymentOrder, SaleOrder, Company, DepositOrder, PurchaseOrder,Corporate,SaleOrderItem])
class NotifyServiceSpec extends Specification {

  GrailsApplicationMock grailsApplication = new GrailsApplicationMock()
  CorporateService corporateService = Mock(CorporateService)

  def setup(){
    service.grailsApplication = grailsApplication
    service.corporateService = corporateService
  }

  @Unroll("Obtain the params when the DEPOSIT ORDER is #status")
  void "get params for Deposit Order"() {
  given:"a deposit order"
    def depositOrder = new DepositOrder(amount:10000, rejectComment:"Fake", rejectReason: RejectReason.DOCUMENTO_INVALIDO)
    depositOrder.save(validate:false)
  and:
    def company = new Company().save(validate:false)
    depositOrder.company = company
    depositOrder.save(validate:false)
  and:
    Corporate corporate = new Corporate(nameCorporate:"makingdevs", corporateUrl:"makingdevs").save()
    corporate.addToCompanies(company)
    corporate.save()
  and:
    corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${System.env['DOMAIN_BASE_URL']}"
  when:"we extract the params"
    def params = service.parametersForDepositOrder(depositOrder, status)
  then:"we should get"
    params == expectedParams
  where:
    status << [
      DepositOrderStatus.CREATED,
      DepositOrderStatus.VALIDATE,
      DepositOrderStatus.AUTHORIZED,
      DepositOrderStatus.REJECTED,
      DepositOrderStatus.EXECUTED,
      DepositOrderStatus.CANCELED,
      DepositOrderStatus.CONCILIATED
      ]

  expectedParams <<[
    [id:1, amount:10000, status:"CREADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
    [id:1, amount:10000, status:"PUESTA EN ESPERA DE SER AUTORIZADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
    [id:1, amount:10000, status:"AUTORIZADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}" ],
    [id:1, amount:10000, status:"RECHAZADA", rejectComment:"Fake", rejectReason: RejectReason.DOCUMENTO_INVALIDO, url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
    [id:1, amount:10000, status:"EJECUTADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
    [id:1, amount:10000, status:"CANCELADA", rejectComment:"Fake", rejectReason: RejectReason.DOCUMENTO_INVALIDO, url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
    [id:1, amount:10000, status:"CONCILIADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"]
  ]
  }

  @Unroll("Obtain the params when the SALEORDER is #status")
  void "obtain the params for SaleOrder Status to populate the email"(){
    given:"a sale order"
      def saleOrder = new SaleOrder(rfc:"MDE130712JA6", clientName:"PatitoAC" ,rejectReason: RejectReason.DOCUMENTO_INVALIDO, comments:"fake")
      def item = new SaleOrderItem(sku:'A001',name:'Gazelle A25',price:new BigDecimal(0.0), ieps:new BigDecimal(0.0), iva:new BigDecimal(0.0), unitType:UnitType.UNIDADES, currencyType:CurrencyType.PESOS)
      saleOrder.addToItems(item)
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
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${System.env['DOMAIN_BASE_URL']}"
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
      [id:1, clientName:"PatitoAC", rfc:"MDE130712JA6", status:"CREADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, clientName:"PatitoAC", rfc:"MDE130712JA6", status:"PUESTA EN ESPERA DE SER AUTORIZADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, clientName:"PatitoAC", rfc:"MDE130712JA6", status:"AUTORIZADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, rfc:"MDE130712JA6", clientName:"PatitoAC" ,rejectReason: RejectReason.DOCUMENTO_INVALIDO, comments:"fake", status:"RECHAZADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, clientName:"PatitoAC", rfc:"MDE130712JA6", status:"PAGADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, clientName:"PatitoAC", rfc:"MDE130712JA6", status:"EJECUTADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, rfc:"MDE130712JA6", clientName:"PatitoAC" ,rejectReason: RejectReason.DOCUMENTO_INVALIDO, comments:"fake", status:"CANCELADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, rfc:"MDE130712JA6", clientName:"PatitoAC" ,rejectReason: RejectReason.DOCUMENTO_INVALIDO, comments:"fake", status:"CANCELADA POR AUTORIZAR", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, rfc:"MDE130712JA6", clientName:"PatitoAC" ,rejectReason: RejectReason.DOCUMENTO_INVALIDO, comments:"fake", status:"CANCELACION AUTORIZADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, rfc:"MDE130712JA6", clientName:"PatitoAC" ,rejectReason: RejectReason.DOCUMENTO_INVALIDO, comments:"fake", status:"CANCELACION EJECUTADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"]
    ]
   }

  @Unroll("Obtain the params when the CashOutOrder is #status")
  void "obtain the params for CashOut Order Status to populate the email"(){
    given:"a cashOut order"
      def cashOutOrder = new CashOutOrder(amount:9999, comments:"falsa", rejectReason:RejectReason.DOCUMENTO_INVALIDO)
      cashOutOrder.save(validate:false)
    and:
      def company = new Company().save(validate:false)
      cashOutOrder.company = company
      cashOutOrder.save(validate:false)
    and:
      Corporate corporate = new Corporate(nameCorporate:"makingdevs", corporateUrl:"makingdevs").save()
      corporate.addToCompanies(company)
      corporate.save()
    and:
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${System.env['DOMAIN_BASE_URL']}"
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
      [id:1, amount:9999, status:"CREADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, amount:9999, status:"EN PROCESO", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, amount:9999, status:"PUESTA EN ESPERA DE SER AUTORIZADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, amount:9999, status:"AUTORIZADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, amount:9999, status:"RECHAZADA", comments:"falsa", rejectReason:RejectReason.DOCUMENTO_INVALIDO, url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, amount:9999, status:"EJECUTADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, amount:9999, status:"CANCELADA", comments:"falsa", rejectReason:RejectReason.DOCUMENTO_INVALIDO, url:"makingdevs${System.env['DOMAIN_BASE_URL']}"]
      ]
  }

  @Unroll("Obtain the params when the Purchase Order is #status")
  void "obtain the params for Purchase Order Status to populate the email"(){
    given:"a purchase order"
      def purchaseOrder = new PurchaseOrder(providerName:"Fake Inc", rejectReason: RejectReason.DOCUMENTO_INVALIDO, rejectComment:"Fake")
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
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${System.env['DOMAIN_BASE_URL']}"
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
      [id:1, providerName:"Fake Inc", status:"CREADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, providerName:"Fake Inc", status:"PUESTA EN ESPERA DE SER AUTORIZADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, providerName:"Fake Inc", status:"AUTORIZADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, providerName:"Fake Inc", rejectComment:"Fake", rejectReason:RejectReason.DOCUMENTO_INVALIDO, status:"RECHAZADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}" ],
      [id:1, providerName:"Fake Inc", rejectComment:"Fake", rejectReason:RejectReason.DOCUMENTO_INVALIDO, status:"PAGADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, providerName:"Fake Inc", rejectComment:"Fake", rejectReason:RejectReason.DOCUMENTO_INVALIDO, status:"CANCELADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"]
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
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${System.env['DOMAIN_BASE_URL']}"
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
      [id:1, amount:88888, status:"CREADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, amount:88888, status:"PUESTA EN ESPERA DE SER AUTORIZADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, amount:88888, status:"AUTORIZADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, amount:88888, status:"EJECUTADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, amount:88888, status:"APROBADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, amount:88888, status:"ACEPTADA", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, amount:88888, status:"RECHAZADA", rejectComment:"Mentiroso", rejectReason:RejectReason.DOCUMENTO_INVALIDO, url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
      [id:1, amount:88888, status:"CANCELADA", rejectComment:"Mentiroso", rejectReason:RejectReason.DOCUMENTO_INVALIDO, url:"makingdevs${System.env['DOMAIN_BASE_URL']}"]
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
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${System.env['DOMAIN_BASE_URL']}"
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
    [id:1, amountInterest:10000, status:"CREADA", amountIvaInterest:20000, amountToCapital:30000, url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
    [id:1, amountInterest:10000, status:"PUESTA EN ESPERA DE SER AUTORIZADA", amountIvaInterest:20000, amountToCapital:30000, url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
    [id:1, amountInterest:10000, status:"AUTORIZADA", amountIvaInterest:20000, amountToCapital:30000, url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
    [id:1, amountInterest:10000, status:"RECHAZADA", amountIvaInterest:20000, amountToCapital:30000, rejectComment:"Fake", rejectReason: RejectReason.DOCUMENTO_INVALIDO, url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
    [id:1, amountInterest:10000, status:"EJECUTADA", amountIvaInterest:20000, amountToCapital:30000, url:"makingdevs${System.env['DOMAIN_BASE_URL']}"],
    [id:1, amountInterest:10000, status:"CANCELADA", amountIvaInterest:20000, amountToCapital:30000, rejectComment:"Fake", rejectReason: RejectReason.DOCUMENTO_INVALIDO, url:"makingdevs${System.env['DOMAIN_BASE_URL']}"]
]
     }

  @Unroll("Obtain the params when the Fees Receipt is #status")
  void "obtain the params for Fees Receipt Status to populate the email"(){
    given:"a company"
    def company = new Company("rfc":"qwerty123456", "bussinessName":"apple")
    company.save(validate:false)
    and:
    Corporate corporate = new Corporate(nameCorporate:"makingdevs", corporateUrl:"makingdevs").save()
    corporate.addToCompanies(company)
    corporate.save()
    and: "a fees Receipt"
    def feesReceipt = new FeesReceipt(rejectReason:RejectReason.DOCUMENTO_INVALIDO, comments:"fake")
    feesReceipt.company = company
    feesReceipt.save(validate:false)
    and:
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${System.env['DOMAIN_BASE_URL']}"
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
    ['id':1, 'company':'apple', 'status':'CREADA', 'url':"makingdevs${System.env['DOMAIN_BASE_URL']}"],
    ['id':1, 'company':'apple', 'status':'PUESTA EN ESPERA DE SER AUTORIZADA', 'url':"makingdevs${System.env['DOMAIN_BASE_URL']}"],
    ['id':1, 'company':'apple', 'status':'AUTORIZADA', 'url':"makingdevs${System.env['DOMAIN_BASE_URL']}"],
    ['id':1, 'company':'apple', 'status':'EJECUTADA', 'url':"makingdevs${System.env['DOMAIN_BASE_URL']}"],
    ['id':1, 'company':'apple', 'status':'CANCELADA', 'rejectReason': RejectReason.DOCUMENTO_INVALIDO, 'comments':'fake', 'url':"makingdevs${System.env['DOMAIN_BASE_URL']}"],
    ['id':1, 'company':'apple', 'status':'RECHAZADA', 'rejectReason': RejectReason.DOCUMENTO_INVALIDO, 'comments':'fake', 'url':"makingdevs${System.env['DOMAIN_BASE_URL']}"]
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
      corporateService.findCorporateByCompanyId(company.id) >> "${corporate.corporateUrl}${System.env['DOMAIN_BASE_URL']}"
    when:"we extract the params"
    def paramsProvider = service.parametersForBusinessEntity(provider, company)
    def paramsClient = service.parametersForBusinessEntity(client, company)
    def paramsEmployee = service.parametersForBusinessEntity(employee, company)
    then:"we should get"
    paramsProvider == ['id':1, 'rfc':'abc123456', company:"PAtitoABC", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"]
    paramsClient == ['id':2, 'rfc':'pasc123456', company:"PAtitoABC", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"]
    paramsEmployee == ['id':3, 'rfc':'sara123456', company:"PAtitoABC", url:"makingdevs${System.env['DOMAIN_BASE_URL']}"]
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



}
