package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import java.lang.Void as Should
import grails.test.mixin.Mock
import spock.lang.FailsWith
import org.springframework.context.i18n.LocaleContextHolder

@TestFor(MachineryLinkService)
@Mock([PurchaseOrder,Bank,State,Machine,MachineryLink,Company])
class MachineryLinkServiceSpec extends Specification {

  Should "create the link between the state machine and an instance"(){
    given:"the instance"
      PurchaseOrder instance = new PurchaseOrder()
      instance.save(validate:false)
    and:"the company"
      Company company = new Company(bussinessName:"MakingDevs")
      company.save(validate:false)
    and:"the machinery"
      State initialState = new State()
      initialState.save()
      Machine machine = new Machine(initialState:initialState)
    when:
      MachineryLink machineryLink = service.createMachineryLinkForThisInstance(instance,machine)
    then:
      machineryLink.id
      machineryLink.machineryRef == instance.id
      machineryLink.type == PurchaseOrder.class.simpleName
      machineryLink.machine
      machineryLink.machine.id == machine.id
  }

  @FailsWith(RuntimeException)
  Should "fail while trying to create a machine for an instace without the implements"(){
    given:
      Bank instance = new Bank()
      instance.save()
      Machine machine = new Machine()
    and:"the company"
      Company company = new Company(bussinessName:"MakingDevs")
      company.save(validate:false)
    when:
      MachineryLink machineryLink = service.createMachineryLinkForThisInstance(company.id,instance.class.simpleName,machine)
    then:
      !machineryLink.id
  }

  Should "get the classes that implements Machinery interface"(){
    given:"the messages"
      ArrayList<String> names = grailsApplication.domainClasses*.clazz.simpleName
      names.each{ name ->
        messageSource.addMessage("${name[0].toLowerCase()}${name[1..name.size()-1]}.name",LocaleContextHolder.getLocale(),name)
      }
    when:
      ArrayList<String> classes = service.getClassesWithMachineryInterface()
    then:
      classes.size() == 1
  }

}
