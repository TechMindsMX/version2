package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class CombinationLinkService {

  CombinationLink createCombinationLinkForInstance(def instance,Combination combination){
    if(!Linker.class.isAssignableFrom(instance.class)){
      throw new RuntimeException("Linker is not assignable from ${instance.class.simpleName}")
    }

    CombinationLink combinationLink = CombinationLink.findByTypeAndInstanceRef(instance.class.simpleName,instance.id) ?: new CombinationLink(type:instance.class.simpleName,
                                                                                                                                             instanceRef:instance.id)

    combinationLink.addToCombinations(combination)
    combinationLink.save()
    combinationLink
  }

  ArrayList<CombinationLink> findCombinationLinksForInstance(def instance){
    def criteria = CombinationLink.createCriteria()

    ArrayList<CombinationLink> combinationLinks =  criteria.list{
      eq("type",instance.class.simpleName)
      eq("instanceRef",instance.id)
    } ?: []

    combinationLinks
  }

}
