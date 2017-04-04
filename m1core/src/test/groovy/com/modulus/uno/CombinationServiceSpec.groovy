package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import java.lang.Void as Should
import com.modulus.uno.machine.Machine

@TestFor(CombinationService)
@Mock([Combination, Machine])
class CombinationServiceSpec extends Specification {

  Should "create the combination for an instance with a another class"(){
    given:"the instance"
      Machine instance = new Machine().save(validate:false)
    and:"the class which it is related to"
      String className = "PurchaseOrder"
    when:
      Combination combination = service.createCombinationOfInstanceWithClass(instance,className)
    then:
      combination.classLeft == "PurchaseOrder"
      combination.classRight == instance.class.simpleName
      combination.rightInstanceId == instance.id
  }

}
