package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock
import spock.lang.Unroll
import java.math.RoundingMode
import java.lang.Void as Should

import com.modulus.uno.stp.StpClabeService
import com.modulus.uno.stp.StpService
import com.modulus.uno.saleorder.SaleOrder
import com.modulus.uno.saleorder.SaleOrderItem

@TestFor(ModulusUnoService)
//TODO esto es demasiado para un servicio posible refactor
@Mock([User,Role,UserRoleCompany,Profile,Company,ModulusUnoAccount,Commission,SaleOrder,SaleOrderItem, CashOutOrder, Transaction])
class ModulusUnoServiceSpec extends Specification {

  def restService = Mock(RestService)
  def corporateService = Mock(CorporateService)
  def stpService = Mock(StpService)
  def stpClabeService = Mock(StpClabeService)
  def transactionService = Mock(TransactionService)
  def commissionTransactionService = Mock(CommissionTransactionService)

  def setup() {
    service.restService = restService
    service.stpService = stpService
    service.stpClabeService = stpClabeService
    service.transactionService = transactionService
    service.corporateService = corporateService
    grailsApplication.config.stp.typeAccount = "some"
    grailsApplication.config.stp.institutionOperation = "some"
    service.commissionTransactionService = commissionTransactionService
  }

  Should "create a m1 account for a company"() {
    given:
      Company company = new Company()
      company.bussinessName = "apple"
      company.rfc = "ROD861224NHA"
      company.employeeNumbers = 5
      company.grossAnnualBilling = 23000.00
      company.save()
    and:
      def grailsApplication = new GrailsApplicationMock()
      service.grailsApplication = grailsApplication
    and:
      stpClabeService.generateSTPMainAccount(_, _) >> "stpClabeForCompany"
    when:"We create an account"
      def account = service.generedModulusUnoAccountByCompany(company)
    then:"We expect account created"
      account
      account.stpClabe
  }

 @Unroll
 void "should apply fee to order with fee-commission=#fee, percentage-commission=#percentage and order is a SaleOrder"() {
    given:
       def account = new ModulusUnoAccount(timoneUuid:"qwer23456rty567ty").save(validate:false)
    and:
      def commission = new Commission(fee:fee, percentage:percentage, type:CommissionType.FACTURA)
    and:
      def company = new Company().save(validate:false)
      company.addToAccounts(account)
      company.addToCommissions(commission)
      company.save(validate:false)
    and:
      def order = new SaleOrder(company:company)
      def item = new SaleOrderItem(quantity:1, price:100.00f, iva:16.00)
      item.save(validate:false)
      order.items = [item]
      order.save(validate:false)
    when:
      FeeCommand feeCommand = service.createFeeCommandFromOrder(order)
    then:
      feeCommand.amount == result
    where:
      fee | percentage  |  result
      0.0 | 3.0         | 3.48
      5.0 | 0.0         | 5.00
 }

  void "generate a cashout to a integrate"() {
    given:
      def account = new ModulusUnoAccount(stpClabe:"stpClabe", aliasStp:"aliasStp").save(validate:false)
    and:
      def company = new Company(bussinessName:"CompanyName").save(validate:false)
      company.addToAccounts(account)
      company.save(validate:false)
    and:
      def commission = new Commission(fee:0.00, percentage:5.00, type:CommissionType.PAGO)
      company.addToCommissions(commission)
      company.save(validate:false)
    and:
      BankAccount bankAccount = Mock(BankAccount)
      Bank bank = Mock(Bank)
      bank.bankingCode >> '123'
      bankAccount.banco >> bank
      bankAccount.clabe >> '1234567890'
      def order = new CashOutOrder()
      order.amount = 100
      order.account = bankAccount
      order.company = company
      order.save()
    and:"A legal representative"
      User user = new User()
      Profile profile = new Profile()
      profile.email = "emailUser"
      profile.save(validate:false)
      user.profile = profile
      user.save(validate:false)
      corporateService.findLegalRepresentativesOfCompany(_) >> [user]
    when:
       service.approveCashOutOrder(order)
    then:
      1 * stpService.sendPayOrder(_)
      1 * transactionService.saveTransaction(_)
      1 * commissionTransactionService.saveCommissionTransaction(_)
  }

  void "Should get transactions of integrator"() {
    given:
      AccountStatementCommand command = Mock(AccountStatementCommand)
    and:
      def grailsApplication = new GrailsApplicationMock()
      service.grailsApplication = grailsApplication
    when:
      def result = service.getTransactionsInPeriodOfIntegrator(command)
    then:
      1 * restService.getTransactionsIntegrator(_ as AccountStatementCommand, grailsApplication.config.modulus.integratorTransactions)
  }

  void "should create an user with stp subaccount"() {
    given:"A create account command"
      CreateAccountCommand command = new CreateAccountCommand(payerAccount:"thePayerAccount",
                                                              uuid:"theUuid",
                                                              name:"theClientName")
    and:
      def grailsApplication = new GrailsApplicationMock()
      service.grailsApplication = grailsApplication
    when:"We create an account"
      def result = service.generateSubAccountStpForClient(command)
    then:"We expect service was called"
      1 * restService.sendCommandWithAuth(_ as CreateAccountCommand, _)
  }

}
