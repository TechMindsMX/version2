package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock

@TestFor(CashOutOrderService)
@Mock([Company,User,UserRole,Role,Profile,BankAccount,CashOutOrder,Authorization])
class CashOutOrderServiceSpec extends Specification {

  ModulusUnoService modulusUnoService = Mock(ModulusUnoService)

  def setup() {
    service.modulusUnoService = modulusUnoService
  }

  def "adding a autorization to cashOut order"() {
    given:
      def company = new Company().save(validate:false)
    and:
      def cashout = new CashOutOrder()
      cashout.amount = 100
      cashout.timoneUuid = "12qw34er56ty78ui90op"
      cashout.timoneAccount = "1630000002"
      cashout.account = new BankAccount().save(validate:false)
      cashout.company = company
      cashout.save()
    and:
      def userRole = new Role(authority:'ROLE_AUTHORIZER_EJECUTOR').save(validate:false)
      createUserWithRole('autorizador1', 'autorizador1', 'autorizador1@email.com', userRole)
    when:
      def cashoutResult = service.addAutorizationToCashoutOrder(cashout,User.get(1))
    then:
      cashoutResult.authorizations.size() == 1
  }

  def "verify if this cashOutOrder have not the number of authorizations"() {
    given:
      def cashOutOrder = new CashOutOrder()
      cashOutOrder.amount = 100
      cashOutOrder.timoneUuid = "123er567fgh89kjhg"
      cashOutOrder.account = new BankAccount().save(validate:false)
      cashOutOrder.company = new Company(numberOfAuthorizations:1).save(validate:false)
      cashOutOrder.save()
    when:
      def isAvailable = service.isAvailableForAuthorize(cashOutOrder)
    then:
      isAvailable == false
  }

  def "Should approve a cashout order"() {
    given:"A cashout order"
      def company = new Company().save(validate:false)
      def cashout = new CashOutOrder()
      cashout.amount = 100
      cashout.timoneUuid = "12qw34er56ty78ui90op"
      cashout.timoneAccount = "1630000002"
      cashout.account = new BankAccount().save(validate:false)
      cashout.company = company
      cashout.save()
    when:
      def cashResult = service.authorizeAndDoCashOutOrder(cashout)
    then:
      1 * modulusUnoService.approveCashOutOrder(_)
  }

  private def createUserWithRole(String username, String password, String email, def userRole) {
      def user = User.findByUsername(username) ?: new User(username:username,
      password:password,
      enabled:true,
      profile:new Profile(name:username,
      lastName:'lastName',
      motherLastName:'motherLastName',
      email:email)).save(validate:false)

      if(!UserRole.get(user.id,userRole.id))
        UserRole.create user, userRole, true
  }

}
