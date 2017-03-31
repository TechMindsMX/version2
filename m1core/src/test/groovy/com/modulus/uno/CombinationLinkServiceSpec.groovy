package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.FailsWith
import java.lang.Void as Should
import com.modulus.uno.machine.Machine

@TestFor(CombinationLinkService)
@Mock([Machine,Company,Combination,CombinationLink])
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
      CombinationLink combinationLink = service.createCombinationLinkForInstance(instance,combination)
    then:
      combinationLink.type == instance.class.simpleName
      combinationLink.instanceRef == instance.id
  }
  
  @FailsWith(RuntimeException)
  Should "fail while trying to create a combination for an instance without implements"(){
    given:"the instance"
      Bank instance = new Bank()
      instance.save()
    and:"the combination"
      Combination combination = createCombination()
    when:
      CombinationLink combinationLink = service.createCombinationLinkForInstance(instance,combination)
    then:
      !combinationLink.id
  }

  Should "get the combination links for an instance"(){
    given:"the combination link of an instance"
      Company instance = new Company(bussinessName:"Company 1").save(validate:false)
      Combination combination = createCombination()
      CombinationLink combinationLink = service.createCombinationLinkForInstance(instance,combination)
    when:
      ArrayList<CombinationLink> combinationLinks = service.findCombinationLinksForInstance(instance)
    then:
      combinationLinks.size() == 1
  }

  private Combination createCombination(){
    Machine machine = new Machine().save(validate:false)
    Combination combination = new Combination(classLeft:"PurchaseOrder",
                                              classRight:machine.class.simpleName,
                                              rightInstanceId:machine.id)
    combination
  }

}
