package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class MachineryLinkService {

  def grailsApplication

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

  ArrayList<String> getNamesOfClassesWithMachineryInterface(){
    ArrayList<String> names = []
    grailsApplication.domainClasses.findAll { Machinery.class.isAssignableFrom(it.clazz) }*.clazz*.simpleName
  }

}
