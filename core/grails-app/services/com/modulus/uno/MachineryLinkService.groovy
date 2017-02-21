package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class MachineryLinkService {

  MachineryLink createMachineryLinkForThisInstance(def instance,Machine machine){
    if(!Machinery.class.isAssignableFrom(instance.class)){
      throw new RuntimeException("Machinery is not assignable from ${instance.class.simpleName}")
    }

    MachineryLink machineryLink = MachineryLink.findByMachineryRefAndType(instance.id,instance.class.simpleName) ?: new MachineryLink(machineryRef:instance.id,
                                                                                                                                      type:instance.class.simpleName)
    machineryLink.machine = machine
    machineryLink.save()
    machineryLink
  }

}
