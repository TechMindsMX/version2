package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import java.lang.Void as Should
import grails.test.mixin.Mock
import spock.lang.Unroll

@TestFor(CorporateService)
@Mock([Corporate,Company,User, Role,UserRole,Profile])
class CorporateServiceSpec extends Specification {

  def setup() {
  }

  Should "create a corporate"() {
    given: "set variables to name and url "
      Corporate corporate = new Corporate(nameCorporate:"MakingDevs",
                                          corporateUrl:"MakingDevs-Fico")
    when:
      service.saveNewCorporate(corporate)
    then:
      corporate.nameCorporate == "MakingDevs"
      corporate.companies == null
      corporate.id == 1
  }

  Should "add a company to corporate"() {
    given: "create a company"
      Company company = new Company(rfc:"RODS861224JUI",bussinessName:"prueba",employeeNumbers:2,grossAnnualBilling:1000.00 ).save()
    and: "create a corporate"
      Corporate corporate = new Corporate(nameCorporate:"test", corporateUrl:"url").save(validate:false)
    when:
      Corporate corporateWithACompany = service.addCompanyToCorporate(corporate, Company.get(1))
    then:
      corporateWithACompany.companies.size() == 1
  }

  Should "add users to corparate"() {
    given: "create a user"
      User user = new User(username:"nuevo", password:"123456789Abc")
    and: "create a corpotate"
      Corporate corporate = new Corporate(nameCorporate:"test", corporateUrl:"url").save(validate:false)
    and: "create Role corporative"
      new Role("ROLE_CORPORATIVE").save()
    and: "the user servie Mock"
      def userServiceMock = Mock(UserService) {
        1 * setAuthorityToUser(_,_) >> user
      }
      service.userService = userServiceMock
    when:
      def corporateWithUser = service.addNewUserToCorporate(corporate.id,user,'ROLE_CORPORATIVE')
    then:
      corporateWithUser.users.size() == 1
      corporateWithUser.users.first() == user
  }

  Should "get the corporate of one user with ROLE_CORPORATIVE"(){
    given:"the user"
      User user = new User(username:"nuevo", password:"123456789Abc").save(validate:false)
    and:"the corporative"
      Corporate corporate = new Corporate(nameCorporate:"Corporate1",corporateUrl:"someUrl").save(validate:false)
    and: "create Role corporative"
      new Role("ROLE_CORPORATIVE").save()
    and: "the user servie Mock"
      def userServiceMock = Mock(UserService) {
        1 * setAuthorityToUser(_,_) >> user
      }
      service.userService = userServiceMock
    and:"the user is added to the corporate"
      service.addNewUserToCorporate(corporate.id,user,'ROLE_CORPORATIVE')
    when:
      Corporate userCorporate = service.findCorporateOfUser(user)
    then:
      userCorporate.id == corporate.id
      userCorporate.nameCorporate == "Corporate1"
  }

  Should "get the corporate users by role"(){
    given:"the corporate"
      Corporate corporate = new Corporate(nameCorporate:"Corporate I",
                                          corporateUrl:"http://www.someurl.com")
      corporate.save(validate:false)

    and:"the current user"
      Role role = new Role(authority:_authority)
      User user = new User(username:"egjimenezg@gmail.com",
                           password:"1234567890")

      user.save(validate:false)
      role.save(validate:false)
      UserRole.create(user,role)
    and:"the spring security service mock"
      service.springSecurityService = [currentUser:user]
    and:"the users without roles"
      ArrayList<User> users = [new User(username:"user1"),
                               new User(username:"user2")]

      users.each{ _user -> _user.save(validate:false) }
      users.each{ _user ->
        corporate.addToUsers(_user)
      }
    when:
      ArrayList<User> corporateUsers = service.findCorporativeUsers(corporate.id)
    then:
      corporateUsers.size() == _size
    where:
      _authority         | _size
      "ROLE_M1"          | 0
      "ROLE_CORPORATIVE" | 2
  }

}
