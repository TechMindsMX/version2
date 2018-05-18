package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Ignore

@TestFor(PurchaseOrderService)
@Mock([BankAccount, BusinessEntity, PurchaseOrder, Company, ModulusUnoAccount, Commission, User, Address, Authorization,PaymentToPurchase, PurchaseOrderItem, Transaction])
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

  @Unroll
  void "should update the payment date with #sendDate for a purchase order"() {
    given:
      def purchaseOrder = new PurchaseOrder()
      purchaseOrder.providerName = "prueba"
      purchaseOrder.fechaPago = fechaPago
      purchaseOrder.originalDate = originalDate
      purchaseOrder.save(validate:false)
    when:
      def purchaseOrderResult = service.updateDatePaymentForOrder(1, newDate)
    then:
      purchaseOrderResult.originalDate != null
      purchaseOrderResult.fechaPago == newDate
   where:
      fechaPago     | originalDate  || newDate
      new Date()    | null          || new Date()+5  
      new Date()+1  | new Date()+10 || new Date()+17

  }

	@Unroll
  void "should add the payment with data=#thePaymentData to purchase order"() {
    given:"The purchase order"
      PurchaseOrder purchaseOrder = new PurchaseOrder(payments:[]).save(validate:false)
			purchaseOrder.metaClass.addToPayments = {
				payments.add(new PaymentToPurchase().save(validate:false))
			}
    and:"The payment data"
			Map paymentData = thePaymentData
    when:
      def result = service.addingPaymentToPurchaseOrder(purchaseOrder, paymentData)
    then:
      result.payments.size() == totalPayments
		where:
			thePaymentData 	|| 	totalPayments
			[amount:1000, transaction:new Transaction().save(validate:false), source:SourcePayment.MODULUS_UNO] || 1
			[amount:1000, transaction:null, source:SourcePayment.BANKING] || 1
  }

  @Unroll
  void "verify if payment not exceeds amount of order when amountItem=#amountItem1, payment1=#paymentAmount1, and payment2=#paymentAmount2"() {
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
        "123.125" | "50"           | new BigDecimal("50.13") || false
        "123.125" | "50"           | new BigDecimal("73.13") || false
        "123.125" | "50"           | new BigDecimal("92.832") || false
        "123.125" | "50"           | new BigDecimal("92.835") || true
  }

  @Unroll
  void "Should save the payment to purchase order when source is #theSource"() {
    given: "A purchase order"
      PurchaseOrder purchaseOrder = createPurchaseOrderForTest(PurchaseOrderStatus.AUTORIZADA)
		and:"Payment data"
			Map paymentData = [amount:theAmount, sourcePayment:theSource]
    and:
      modulusUnoService.payPurchaseOrder(_,_) >> new Transaction().save(validate:false)
    when:
      def result = service.payPurchaseOrder(purchaseOrder, paymentData)
    then:
      result.payments.size() == totalPayments
      callsM1Service * modulusUnoService.payPurchaseOrder(_, _)
      callsEmailer * emailSenderService.notifyPurchaseOrderChangeStatus(_)
			result.status == finalStatus
		where:
			theAmount 	| theSource 	|| 	totalPayments 	| callsM1Service 	| callsEmailer | finalStatus
			1000 		| SourcePayment.MODULUS_UNO 	|| 1 	| 1 	| 1 	| PurchaseOrderStatus.PAGADA
			1000 		| SourcePayment.BANKING 	|| 1 	| 0 	| 1 	| PurchaseOrderStatus.PAGADA
			800 		| SourcePayment.BANKING 	|| 1 	| 0 	| 0 	| PurchaseOrderStatus.AUTORIZADA
  }

  void "Should change status of purchase order payment for a reverted transaction and purchase order is authorized"() {
    given:"A transaction"
      Transaction transaction = new Transaction().save(validate:false)
    and:
      PaymentToPurchase payment = new PaymentToPurchase(transaction:transaction).save(validate:false)
    and:
      PurchaseOrder purchaseOrder = new PurchaseOrder(status:PurchaseOrderStatus.AUTORIZADA)
      purchaseOrder.addToPayments(payment)
      purchaseOrder.save(validate:false)
    when:
      service.reversePaymentPurchaseForTransaction(transaction)
    then:
      payment.status == PaymentToPurchaseStatus.REFUND
      purchaseOrder.status == PurchaseOrderStatus.AUTORIZADA
  }

  void "Should change status of purchase order payment and purchase order for a reverted transaction"() {
    given:"A transaction"
      Transaction transaction = new Transaction().save(validate:false)
    and:
      PaymentToPurchase payment = new PaymentToPurchase(transaction:transaction).save(validate:false)
    and:
      PurchaseOrder purchaseOrder = new PurchaseOrder(status:PurchaseOrderStatus.PAGADA)
      purchaseOrder.addToPayments(payment)
      purchaseOrder.save(validate:false)
    when:
      service.reversePaymentPurchaseForTransaction(transaction)
    then:
      payment.status == PaymentToPurchaseStatus.REFUND
      purchaseOrder.status == PurchaseOrderStatus.AUTORIZADA
  }

	void "Should get the purchase order of a payment to purchase"() {
		given:"The payment"
			PaymentToPurchase paymentToPurchase = new PaymentToPurchase().save(validate:false)
		and:"The purchase orders"
			PurchaseOrder po1 = new PurchaseOrder().save(validate:false)
			PurchaseOrder po2 = new PurchaseOrder().save(validate:false)
			po2.addToPayments(paymentToPurchase)
			po2.save(validate:false)
			PurchaseOrder po3 = new PurchaseOrder().save(validate:false)
		when:
			def result = service.getPurchaseOrderOfPaymentToPurchase(paymentToPurchase)
	  then:
			result.id == po2.id
	}

  @Unroll
  void "Should get the filter list of purchase orders with filter params=#theParams"() {
    given:"The company"
      Company company = new Company(rfc:"A").save(validate:false)
    and:"The current sale orders"
      PurchaseOrder so1 = new PurchaseOrder(providerName:"Proveedor 1", company:company).save(validate:false)
      PurchaseOrder so2 = new PurchaseOrder(providerName:"Proveedor 1", company:new Company(rfc:"B").save(validate:false)).save(validate:false)
      PurchaseOrder so3 = new PurchaseOrder(providerName:"Proveedor 2", company:company).save(validate:false)
      PurchaseOrder so4 = new PurchaseOrder(providerName:"Proveedor 2", company:company).save(validate:false)
      PurchaseOrder so5 = new PurchaseOrder(providerName:"Proveedor 22", company:company).save(validate:false)
      PurchaseOrder so6 = new PurchaseOrder(providerName:"Proveedor 3", company:company).save(validate:false)
      PurchaseOrder so7 = new PurchaseOrder(providerName:"Proveedor 33", company:company).save(validate:false)
      PurchaseOrder so8 = new PurchaseOrder(providerName:"Proveedor 4", company:company).save(validate:false)
      PurchaseOrder so9 = new PurchaseOrder(providerName:"Proveedor 44", company:company).save(validate:false)
    and:"The filter params"
      Map params = theParams
    when:
      def result = service.searchPurchaseOrders("1".toLong(), params)
    then:
      result.size() == sizeList
    where:
      theParams       ||   sizeList
      [providerName:"Proveedor"]    |   8
      [providerName:"Proveedor 2"]    |   3
      [providerName:"Proveedor 22"]    |   1
      [providerName:"Proveedor 5"]    |   0
  }

  @Unroll
  void "Should get all purchase orders with missing docs when current purchase orders is #thePurchaseOrders"() {
    given:
      println "The purchase orders: ${thePurchaseOrders}"
      Company company = new Company(purchaseOrders:thePurchaseOrders).save(validate:false)
    when:
      def results = service.getPurchaseOrdersWithMissingDocs(company)
    then: "value"
      results.items == theExpectedItems
    where:
    thePurchaseOrders   || theExpectedItems
    []                  ||  0
    [createPurchaseOrderForTest(PurchaseOrderStatus.PAGADA)] || 1
    [createPurchaseOrderForTest(PurchaseOrderStatus.PAGADA), createPurchaseOrderForTest(PurchaseOrderStatus.CREADA)] || 1
    [
      new PurchaseOrder(providerName:"UNO", status:PurchaseOrderStatus.PAGADA, items:[new PurchaseOrderItem(quantity:1, price:100, ieps:0, iva:0).save(validate:false)]).save(validate:false, flush:true),
      new PurchaseOrder(providerName:"DOS", status:PurchaseOrderStatus.PAGADA, items:[new PurchaseOrderItem(quantity:1, price:1000, ieps:0, iva:0).save(validate:false)]).save(validate:false, flush:true),
      new PurchaseOrder(providerName:"TRES", status:PurchaseOrderStatus.AUTORIZADA, items:[new PurchaseOrderItem(quantity:1, price:10, ieps:0, iva:0).save(validate:false)]).save(validate:false, flush:true),
      new PurchaseOrder(providerName:"CUATRO", status:PurchaseOrderStatus.CANCELADA, items:[new PurchaseOrderItem(quantity:1, price:1, ieps:0, iva:0).save(validate:false)]).save(validate:false, flush:true)
    ] || 2
  }

  private PaymentToPurchase createPayment(String amount) {
    new PaymentToPurchase(amount: new BigDecimal(amount)).save()
  }

	private PurchaseOrder createPurchaseOrderForTest(PurchaseOrderStatus status) {
		PurchaseOrder purchaseOrder = new PurchaseOrder(status:status).save(validate:false)
		PurchaseOrderItem item = new PurchaseOrderItem(quantity:1, price:1000, ieps:0, iva:0, purchaseOrder:purchaseOrder).save(validate:false)
		purchaseOrder.addToItems(item)
		purchaseOrder.save(validate:false)
		purchaseOrder
	}

  private List<PurchaseOrder> createPurchaseOrderList() {
    [
      new PurchaseOrder(id:1, status:PurchaseOrderStatus.PAGADA, items:[new PurchaseOrderItem(quantity:1, price:1000, ieps:0, iva:0).save(validate:false)]).save(validate:false),
      new PurchaseOrder(id:2, status:PurchaseOrderStatus.PAGADA, items:[new PurchaseOrderItem(quantity:1, price:1000, ieps:0, iva:0).save(validate:false)]).save(validate:false),
      new PurchaseOrder(id:3, status:PurchaseOrderStatus.AUTORIZADA, items:[new PurchaseOrderItem(quantity:1, price:1000, ieps:0, iva:0).save(validate:false)]).save(validate:false),
      new PurchaseOrder(id:4, status:PurchaseOrderStatus.CANCELADA, items:[new PurchaseOrderItem(quantity:1, price:1000, ieps:0, iva:0).save(validate:false)]).save(validate:false)
    ]
  }
}
