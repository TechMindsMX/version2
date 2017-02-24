package com.modulus.uno

import grails.transaction.Transactional
import org.springframework.context.i18n.LocaleContextHolder as LCH

@Transactional
class MachineryLinkService {

  def grailsApplication
  def messageSource

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

  ArrayList<Map> getClassesWithMachineryInterface(){
    ArrayList<Map> machineryClasses= []
    grailsApplication.domainClasses.findAll { Machinery.class.isAssignableFrom(it.clazz) }*.clazz*.simpleName.each{ className ->
      String name = "${className[0].toLowerCase()}${className[1..className.size()-1]}"
      machineryClasses << [key:className,
                          value:messageSource.getMessage("${name}.name",null,LCH.getLocale())]
    }
    machineryClasses
  }

}
