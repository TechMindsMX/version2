package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(EmailSenderService)
@Mock([Company,User,Role,UserRoleCompany, BusinessEntity, LoanOrder, PurchaseOrder, Role, UserRole])
class EmailSenderServiceSpec extends Specification {

  def directorService = Mock(DirectorService)

  def setup() {
    service.directorService = directorService
  }

  void "Get an email list for send email"(){
    given:"A list of users"
    def user1= new User(username:"User1",enabled:true,
    profile:new Profile(name:"User1", email:"user1@me.com")).save(validate:false)
    def user2= new User(username:"User2",enabled:true,
    profile:new Profile(name:"User2", email:"user2@me.com")).save(validate:false)
    def user3= new User(username:"User3", enabled:true,
    profile:new Profile(name:"User3", email:"user3@me.com")).save(validate:false)
    def userList = [user1, user2, user3]
    and:"A company"
    def company = new Company(bussinessName:"C1")
    company.save(validate:false)
    and:
    directorService.findUsersOfCompanyByRole(_,_) >> userList
    when: "we want to get the email list of users"
    def emailList = service.getEmailList(company, [])
    then: "We should get a list of emails"
    emailList == ["user1@me.com", "user2@me.com", "user3@me.com"]
  }
}
