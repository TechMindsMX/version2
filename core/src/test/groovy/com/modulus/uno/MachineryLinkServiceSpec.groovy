package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import java.lang.Void as Should
import grails.test.mixin.Mock
import spock.lang.FailsWith

@TestFor(MachineryLinkService)
@Mock([PurchaseOrder,Bank,State,Machine,MachineryLink])
class MachineryLinkServiceSpec extends Specification {

  Should "create the link between the state machine and an instance"(){
    given:"the instance"
      PurchaseOrder instance = new PurchaseOrder()
      instance.save(validate:false)
    and:"the machinery"
      State initialState = new State()
      initialState.save()
      Machine machine = new Machine(initialState:initialState)
    when:
      MachineryLink machineryLink = service.createMachineryLinkForThisInstance(instance,machine)
    then:
      machineryLink.id
      machineryLink.type == PurchaseOrder.class.simpleName
      machineryLink.machine
      machineryLink.machine.id == machinery.id
  }

  @FailsWith(RuntimeException)
  Should "fail while trying to create a machine for an instace without the implements"(){
    given:
      Bank instance = new Bank()
      instance.save()
      Machine machine = new Machine()
    when:
      MachineryLink machineryLink = service.createMachineryLinkForThisInstance(instance,machine)
    then:
      !machineryLink.id
  }

}
