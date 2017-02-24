package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Ignore
import java.lang.Void as Should

@TestFor(ActionService)
@Mock([Company,Action])
class ActionServiceSpec extends Specification {

  @Ignore
  Should "create an action for a company"(){
    given:
      Company company = new Company(bussinessName:"MakingDevs")
      company.save(validate:false)
    and:"the action"
      Action action = new Action(name:"Some action")
    when:
      service.addActionToCompany(action,company.id)
    then:
      company.actions.size() == 1
  }

}
