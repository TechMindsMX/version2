package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import java.lang.Void as Should
import com.modulus.uno.machine.Machine

@TestFor(CombinationLinkService)
class CombinationLinkServiceSpec extends Specification {

  Should "create a combination for an instance"(){
    given:"the instance"
      Company instance = new Company(bussinessName:"Company 1").save(validate:false)
    and:"the combination of two classes"
      Machine machine = new Machine().save(validate:false) 
      Combination combination = new Combination(classLeft:"PurchaseOrder",
                                                classRight:machine.class.simpleName,
                                                rightInstanceId:machine.id)
    when:
      CombinationLink combinationLink = service.createCombinationLinkForThisInstance(instance,combination)
    then:
      combinationLink.type == instance.class.simpleName
      combinationLink.instanceRef == instance.id
  }

}
