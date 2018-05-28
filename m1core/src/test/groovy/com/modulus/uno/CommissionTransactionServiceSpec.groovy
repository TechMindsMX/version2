package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll
import grails.test.mixin.Mock
import java.math.RoundingMode

import com.modulus.uno.saleorder.SaleOrder
import com.modulus.uno.saleorder.SaleOrderItem
import com.modulus.uno.saleorder.CreditNote
import com.modulus.uno.saleorder.CreditNoteItem
import com.modulus.uno.status.CommissionTransactionStatus


@TestFor(CommissionTransactionService)
@Mock([CommissionTransaction, Company, Transaction, SaleOrder, SaleOrderItem, Commission, CreditNote, CreditNoteItem])
class CommissionTransactionServiceSpec extends Specification {

  GrailsApplicationMock grailsApplication = new GrailsApplicationMock()
  CollaboratorService collaboratorService = Mock(CollaboratorService)

  def setup(){
    service.grailsApplication = grailsApplication
    service.collaboratorService = collaboratorService
  }

  void "Should save a commission transaction"() {
    given:"A fee command"
      FeeCommand feeCommand = new FeeCommand(companyId:"1", amount:new BigDecimal(10), type:"PAGO", transactionId:"1")
    and:
      Company company = new Company().save(validate:false)
      Transaction transaction = new Transaction().save(validate:false)
    when:"We save the commission"
      def commission = service.saveCommissionTransaction(feeCommand)
    then:
      commission.id
  }

  void "Should obtain the commissions pending balance for company"() {
    given:"A company"
      Company company = new Company().save(validate:false)
      Commission commission1 = new Commission(fee:5, percentage:0, type:CommissionType.DEPOSITO).save(validate:false)
      Commission commission2 = new Commission(fee:10, percentage:0, type:CommissionType.PAGO).save(validate:false)
      company.addToCommissions(commission1)
      company.addToCommissions(commission2)
      company.save(validate:false)
    and:"The period"
      Period period = new Period(init:new Date().parse("dd-MM-yyyy","01-05-2017"), end:new Date().parse("dd-MM-yyyy", "31-05-2017"))
    when:
      def balance = service.getCommissionsBalanceInPeriodForCompanyAndStatus(company, CommissionTransactionStatus.PENDING, period)
    then:
      balance.size() == company.commissions.size()
  }

  @Unroll
  void "Should save a invoice commission transaction with amount=#amountExpected when commission fee=#fee and percentage=#percentage and sale order total=#price for a company"() {
    given:"A company"
      Company company = new Company(rfc:"XXX010101XXX").save(validate:false)
    and:"The invoice commission"
      Commission commission = new Commission(fee:fee, percentage:percentage, type:CommissionType.FACTURA).save(validate:false)
      company.addToCommissions(commission)
    and:"The sale order"
      SaleOrder saleOrder = new SaleOrder(company:company).save(validate:false)
      SaleOrderItem saleOrderItem = new SaleOrderItem()
      saleOrderItem.price = price
      saleOrderItem.quantity = 1
      saleOrderItem.save(validate:false)
      saleOrder.addToItems(saleOrderItem)
    when:
      def transaction = service.registerCommissionForSaleOrder(saleOrder)
    then:
      transaction.id
      transaction.amount == amountExpected
    where:
    fee   | percentage  | price   ||  amountExpected
    2     | 0           | 100     ||  2
    2     | 0           | 50      ||  2
    0     | 10          | 100     ||  10
    0     | 5           | 100     ||  5
    0     | 10          | 50      ||  5
  }

  void "Should throw exception when company hasn't invoice commission"() {
    given:"A company"
      Company company = new Company(rfc:"XXX010101XXX").save(validate:false)
    and:"The invoice commission"
      Commission commission = new Commission(fee:2, percentage:0, type:CommissionType.PAGO).save(validate:false)
      company.addToCommissions(commission)
    and:"The sale order"
      SaleOrder saleOrder = new SaleOrder(company:company).save(validate:false)
      SaleOrderItem saleOrderItem = new SaleOrderItem()
      saleOrderItem.price = 100
      saleOrderItem.quantity = 1
      saleOrderItem.save(validate:false)
      saleOrder.addToItems(saleOrderItem)
    when:
      def transaction = service.registerCommissionForSaleOrder(saleOrder)
    then:
      thrown BusinessException
  }

  @Unroll
  void "Should save a fixed commission transaction with amount=#amountExpected when commission fee=#fee for a company"() {
    given:"A company"
      Company company = new Company(rfc:"XXX010101XXX").save(validate:false)
    and:"The invoice commission"
      Commission commission = new Commission(fee:fee, percentage:0, type:CommissionType.FIJA).save(validate:false)
      company.addToCommissions(commission)
    when:
      def transaction = service.applyFixedCommissionToCompany(company)
    then:
      transaction.id
      transaction.amount == amountExpected
    where:
    fee   ||  amountExpected
    1000  ||  1000
    2000  ||  2000
  }

  void "Should throw exception when company hasn't fixed commission"() {
    given:"A company"
      Company company = new Company(rfc:"XXX010101XXX").save(validate:false)
    and:"The invoice commission"
      Commission commission = new Commission(fee:2, percentage:0, type:CommissionType.PAGO).save(validate:false)
      company.addToCommissions(commission)
    when:
      def transaction = service.applyFixedCommissionToCompany(company)
    then:
      thrown BusinessException
  }

  @Unroll
  void "Should return #expect when company has fixed commission transaction = #transactionFound"() {
    given:"A company"
      Company company = new Company(rfc:"XXX010101XXX").save(validate:false)
    and:"The current month period"
      Period period = new Period(init:new Date().parse("dd-MM-yyyy", "01-04-2017"), end:new Date().parse("dd-MM-yyyy", "30-04-2017"))
      collaboratorService.getCurrentMonthPeriod() >> period
    and:"Fixed Commission Transaction"
      CommissionTransaction.metaClass.static.findByTypeAndCompanyAndDateCreatedBetween = {type,comp,init,end -> transactionFound }
    when:
      Boolean result = service.companyHasFixedCommissionAppliedInCurrentMonth(company)
    then:
      result == expect
    where:
      transactionFound                                  ||  expect
      new CommissionTransaction().save(validate:false)  ||  true
      null                                              ||  false
  }

  @Unroll
  void "Should return #result for sale order is or not is commission invoice"() {
    given:""
      CommissionTransaction.metaClass.static.findByInvoice = { transaction }
    and:
      SaleOrder saleOrder = new SaleOrder().save(validate:false)
    when:
      def isCommissionInvoice = service.saleOrderIsCommissionsInvoice(saleOrder)
    then:
      isCommissionInvoice == result
    where:
      transaction ||  result
      new CommissionTransaction().save(validate:false)  ||  true
      null                                              ||  false
  }

  void "Should set Charged status for all transactions linked to sale order"() {
    given:"The sale order"
      SaleOrder saleOrder = new SaleOrder().save(validate:false)
      CommissionTransactionStatus status = CommissionTransactionStatus.INVOICED
    and:"The transactions"
      List<CommissionTransaction> transactions = [new CommissionTransaction(status:status, invoice:saleOrder).save(validate:false)]
      CommissionTransaction.metaClass.static.findAllByInvoiceAndStatus = {sO, statusTr ->  transactions }
    when:
      service.conciliateTransactionsForSaleOrder(saleOrder)
    then:
      transactions.first().status == CommissionTransactionStatus.CHARGED
  }

  void "Should unlink transactions from sale order"() {
    given:"The sale order"
      SaleOrder saleOrder = new SaleOrder().save(validate:false)
      CommissionTransactionStatus status = CommissionTransactionStatus.INVOICED
    and:"The transactions"
      List<CommissionTransaction> transactions = [new CommissionTransaction(status:status, invoice:saleOrder).save(validate:false)]
      CommissionTransaction.metaClass.static.findAllByInvoiceAndStatus = {sO, statusTr ->  transactions }
    when:
      service.unlinkTransactionsForSaleOrder(saleOrder)
    then:
      transactions.first().status == CommissionTransactionStatus.PENDING
      transactions.first().invoice == null
  }

  @Unroll
  void "Should save a invoice commission transaction with amount=#amountExpected when commission fee=#fee and percentage=#percentage and credit note total=#price for a company"() {
    given:"A company"
      Company company = new Company(rfc:"XXX010101XXX").save(validate:false)
    and:"The invoice commission"
      Commission commission = new Commission(fee:fee, percentage:percentage, type:CommissionType.FACTURA).save(validate:false)
      company.addToCommissions(commission)
    and:"The sale order"
      SaleOrder saleOrder = new SaleOrder(company:company).save(validate:false)
      CreditNote creditNote = new CreditNote(saleOrder:saleOrder).save(validate:false)
      CreditNoteItem creditNoteItem = new CreditNoteItem()
      creditNoteItem.price = price
      creditNoteItem.quantity = 1
      creditNoteItem.save(validate:false)
      creditNote.addToItems(creditNoteItem)
    when:
      def transaction = service.registerCommissionForCreditNote(creditNote)
    then:
      transaction.id
      transaction.amount == amountExpected
    where:
    fee   | percentage  | price   ||  amountExpected
    2     | 0           | 100     ||  2
    2     | 0           | 50      ||  2
    0     | 10          | 100     ||  10
    0     | 5           | 100     ||  5
    0     | 10          | 50      ||  5
  }

  void "Should throw exception when company hasn't invoice commission"() {
    given:"A company"
      Company company = new Company(rfc:"XXX010101XXX").save(validate:false)
    and:"The invoice commission"
      Commission commission = new Commission(fee:2, percentage:0, type:CommissionType.PAGO).save(validate:false)
      company.addToCommissions(commission)
    and:"The sale order"
      SaleOrder saleOrder = new SaleOrder(company:company).save(validate:false)
      CreditNote creditNote = new CreditNote(saleOrder:saleOrder).save(validate:false)
      CreditNoteItem creditNoteItem = new CreditNoteItem()
      creditNoteItem.price = 100
      creditNoteItem.quantity = 1
      creditNoteItem.save(validate:false)
      creditNote.addToItems(creditNoteItem)
    when:
      def transaction = service.registerCommissionForCreditNote(creditNote)
    then:
      thrown BusinessException
  }

  @Unroll
  void "Should get the commission type = #type for a company"() {
    given:"The company"
      Company company = new Company().save(validate:false)
    and:"The commissions company"
      Commission commission1 = new Commission(type:CommissionType.FACTURA, company:company).save(validate:false)
      Commission commission2 = new Commission(type:CommissionType.FIJA, company:company).save(validate:false)
      Commission commission3 = new Commission(type:CommissionType.PAGO, company:company).save(validate:false)
      Commission commission4 = new Commission(type:CommissionType.DEPOSITO, company:company).save(validate:false)
      Commission commission5 = new Commission(type:CommissionType.RECIBO_NOMINA, company:company).save(validate:false)
      company.commissions = [commission1, commission2, commission3, commission4, commission5]
      company.save(validate:false)
    when:
      def result = service.getCommissionForCompanyByType(company, type)
    then:
      result
    where:
      type << [CommissionType.FACTURA, CommissionType.DEPOSITO, CommissionType.FIJA, CommissionType.PAGO, CommissionType.RECIBO_NOMINA]
  }

  void "Should don't get the commission type for a company"() {
    given:"The company"
      Company company = new Company().save(validate:false)
    and:"The commissions company"
      Commission commission1 = new Commission(type:CommissionType.FACTURA, company:company).save(validate:false)
      Commission commission2 = new Commission(type:CommissionType.FIJA, company:company).save(validate:false)
      Commission commission3 = new Commission(type:CommissionType.DEPOSITO, company:company).save(validate:false)
      company.commissions = [commission1, commission2, commission3]
      company.save(validate:false)
    when:
      def result = service.getCommissionForCompanyByType(company, CommissionType.PAGO)
    then:
      !result
  }

  @Unroll
  void "Should get fee command with amount = #theAmount for a commission transaction of type paysheet receipt when the commission fee = #theFee and percentage = #thePercent"() {
    given:"The company"
      Company company = new Company().save(validate:false)
    and:"The commission type for paysheet receipt"
      Commission commission = new Commission(type:CommissionType.RECIBO_NOMINA, fee:theFee, percentage:thePercent).save(validate:false)
      company.addToCommissions(commission)
    and:"The base amount"
      BigDecimal baseAmount = new BigDecimal(2500)
    when:
      def command = service.createFeeCommandForPaysheetReceipt(baseAmount, company)
    then:
      command
      command.amount == theAmount.setScale(2, RoundingMode.HALF_UP)
      command.type == "RECIBO_NOMINA"
    where:
      theFee        |   thePercent        || theAmount
      new BigDecimal(5)   | new BigDecimal(0)     || new BigDecimal(5)
      new BigDecimal(0)   | new BigDecimal(10)     || new BigDecimal(250)
  }

  void "Should thrown a exception when try get fee command for a commission transaction of type paysheet receipt"() {
    given:"The company"
      Company company = new Company().save(validate:false)
    and:"The commission type for paysheet receipt"
      Commission commission = new Commission(type:CommissionType.FACTURA, fee:new BigDecimal(5)).save(validate:false)
      company.addToCommissions(commission)
    and:"The base amount"
      BigDecimal baseAmount = new BigDecimal(2500)
    when:
      def command = service.createFeeCommandForPaysheetReceipt(baseAmount, company)
    then:
      thrown BusinessException
  }

}

