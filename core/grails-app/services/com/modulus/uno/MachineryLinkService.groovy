package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class MachineryLinkService {

  MachineryLink createMachineriesForThisInstance(def instance,ArrayList<Machinery> machineries){
    if(!StateMachine.class.isAssignableFrom(instance.class)){
      throw new RuntimeException("State Machine is not assignable from ${instance.class.simpleName}")
    }
    
    MachineryLink machineryLink = MachineryLink.findByMachineryRefAndType(instance.id,instance.class.simpleName) ?: new MachineryLink(machineryRef:instance.id,
                                                                                                                                      type:instance.class.simpleName)
    machineries.each{ machinery ->
      machineryLink.addToMachineries(machinery)
    }

    machineryLink.save()
    machineryLink
  }

}
