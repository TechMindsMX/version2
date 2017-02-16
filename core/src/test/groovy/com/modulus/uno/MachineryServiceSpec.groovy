package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import java.lang.Void as Should
import grails.test.mixin.Mock

@TestFor(MachineryService)
@Mock([PurchaseOrder,State,Machinery])
class MachineryServiceSpec extends Specification {

  Should "create the link between the state machine and an instance"(){
    given:"the instance"
      PurchaseOrder instance = new PurchaseOrder()
    and:"the machinery"
      State initialState = new State()
      initialState.save()
      Machinery machinery = new Machinery(initialState:initialState)
    when:
      MachineryLink machineryLink = service.createMachineriesForThisInstance(instance,[machinery])
    then:
      machineryLink.id
      machineryLink.type == PurchaseOrder.class.simpleName
      machineryLink.machineries
      machineryLink.machineries[0].id == machinery.id
  }

}
