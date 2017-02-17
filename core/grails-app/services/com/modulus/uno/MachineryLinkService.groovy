package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class MachineryLinkService {

  MachineryLink createMachineryLinkForThisInstance(def instance,Machinery machinery){
    if(!StateMachine.class.isAssignableFrom(instance.class)){
      throw new RuntimeException("State Machine is not assignable from ${instance.class.simpleName}")
    }
    
    MachineryLink machineryLink = MachineryLink.findByMachineryRefAndType(instance.id,instance.class.simpleName) ?: new MachineryLink(machineryRef:instance.id,
                                                                                                                                      type:instance.class.simpleName)
    machineryLink.machinery = machinery
    machineryLink.save()
    machineryLink
  }

}
