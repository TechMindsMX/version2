package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(PurchaseOrderService)
@Mock([BankAccount, BusinessEntity, PurchaseOrder, Company, User, Address, Authorization,PaymentToPurchase, PurchaseOrderItem, Transaction])
class PurchaseOrderServiceSpec extends Specification {

  def emailSenderService = Mock(EmailSenderService)
  def documentService = Mock(DocumentService)
  def modulusUnoService = Mock(ModulusUnoService)
  def restService = Mock(RestService)

  def setup(){
    service.emailSenderService = emailSenderService
    service.documentService = documentService
    service.modulusUnoService = modulusUnoService
    service.restService = restService
  }

  void "should create a purchase order"() {
    given:"A company"
    def company = new Company(rfc:"JIGE930831NZ1",
                                bussinessName:"Apple Computers",
                                webSite:"http://www.apple.com",
                                employeeNumbers:40,
                                grossAnnualBilling:4000).save(validate:false)
    and:"A business entity"
      def businessEntity = new BusinessEntity(rfc:'XXX010101XXX', website:'http://www.iecce.mx',type:BusinessEntityType.FISICA).save(validate:false)
      BankAccount bankAccount = new BankAccount().save(validate:false)
      Map params = [providerId:businessEntity.id, bankAccountId:bankAccount.id, fechaPago:new Date().format("dd/MM/yyyy").toString(), externalId:"12345"]
    when:"We create a purchase order"
      def result = service.createPurchaseOrder(company.id, params)
    then:"We expect a new purchase order"
      result instanceof PurchaseOrder
      result.status == PurchaseOrderStatus.CREADA
  }

  void "should send a purchase order to confirmation"(){
  given:"A purchase order"
    def company = new Company(rfc:"JIGE930831NZ1",
                                bussinessName:"Apple Computers",
                                webSite:"http://www.apple.com",
                                employeeNumbers:40,
                                grossAnnualBilling:4000).save(validate:false)
    def businessEntity = new BusinessEntity(rfc:'XXX010101XXX', website:'http://www.iecce.mx',type:BusinessEntityType.FISICA).save(validate:false)
    BankAccount bankAccount = new BankAccount().save(validate:false)
    Map params = [providerId:businessEntity.id, bankAccountId:bankAccount.id, fechaPago:new Date().format("dd/MM/yyyy").toString(), externalId:"12345"]
    def purchaseOrder = service.createPurchaseOrder(company.id, params)
  when:"We authorize a purchase order"
    def result = service.requestAuthorizationForTheOrder(purchaseOrder)
  then:"We expect new status"
    result.status == PurchaseOrderStatus.POR_AUTORIZAR
    1 * emailSenderService.notifyPurchaseOrderChangeStatus(purchaseOrder)
    result instanceof PurchaseOrder
  }

  void "verify if exist  or not original date in purchaseOrder"() {
    given:
      def purchaseOrder = new PurchaseOrder()
      purchaseOrder.providerName = "prueba"
      purchaseOrder.fechaPago = fechaPago
      purchaseOrder.originalDate = originalDate
      purchaseOrder.save(validate:false)
    when:
      def purchaseORderResult = service.updateDatePaymentForOrder(1, sendDate)
    then:
      purchaseORderResult.originalDate != null
      purchaseORderResult.fechaPago == sendDate
   where:
      fechaPago     | originalDate  | sendDate      | fechaPagoOriginal
      new Date()    | null          | new Date()+5  | new Date()
      new Date()+1  | new Date()+17 | new Date()+17 | new Date()+17

  }

  void "verify purchase order had one payment"() {
    given:
      def purchaseOrder = new PurchaseOrder()
      purchaseOrder.providerName = "prueba"
      purchaseOrder.save(validate:false)
    and:
      BigDecimal amount = new BigDecimal(10)
      Transaction transaction = new Transaction().save(validate:false)
    when:
      def purchaseOrderResult = service.addingPaymentToPurchaseOrder(purchaseOrder, amount, transaction.id)
    then:
      purchaseOrderResult.payments.size() == 1
  }



  void "verify if payment not exceeds amount of order"() {
     given: "create a purchase order"
      def purchaseOrder = new PurchaseOrder()
      purchaseOrder.providerName = "prueba"
      purchaseOrder.status = PurchaseOrderStatus.AUTORIZADA
      purchaseOrder.save(validate:false)
    and: "create one items and add this to purchase order"
      def item1 = new PurchaseOrderItem(name:'item1',quantity:1,price:new BigDecimal(amountItem1), unitType:"UNIDADES", purchaseOrder:purchaseOrder )
      purchaseOrder.addToItems(item1)
      purchaseOrder.save(validate:false)
    and: "create one paymentToPurchase and add to purchase order"
      purchaseOrder.addToPayments(createPayment(paymentAmount1))
      purchaseOrder.save()
    when:
      boolean response = service.amountExceedsTotal(paymentAmount2,purchaseOrder)
    then:
      response == result
    where:
      amountItem1 | paymentAmount1 | paymentAmount2         || result
        "590"     | "75"           | new BigDecimal("85")   || false
        "116"     | "100"          | new BigDecimal("16")   || false
        "50"      | "1"            | new BigDecimal("30")   || false
        "300"     | "500"          | new BigDecimal("900")  || true
        "900"     | "0"            | new BigDecimal("860")  || false
        "355"     | "390"          | new BigDecimal("900")  || true
        "1600"    | "100"          | new BigDecimal("860")  || false
        "123.12"  | "600.32"       | new BigDecimal("1000") || true

  }

  private PaymentToPurchase createPayment(String amount) {
    new PaymentToPurchase(amount: new BigDecimal(amount)).save()
  }

}
