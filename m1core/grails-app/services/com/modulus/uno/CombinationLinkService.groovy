package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class CombinationLinkService {

  CombinationLink createCombinationLinkForInstance(def instance,Combination currentCombination){
    if(!Linker.class.isAssignableFrom(instance.class)){
      throw new RuntimeException("Linker is not assignable from ${instance.class.simpleName}")
    }

    def criteria = CombinationLink.createCriteria()

    CombinationLink combinationLink = criteria.get{
                                        eq("type",instance.class.simpleName)
                                        eq("instanceRef",instance.id)

                                        combination{
                                          eq("id",currentCombination.id)
                                        }

                                      } ?: new CombinationLink(type:instance.class.simpleName,
                                                               instanceRef:instance.id)

    combinationLink.combination = currentCombination
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
