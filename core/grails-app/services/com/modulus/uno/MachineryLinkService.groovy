package com.modulus.uno

import grails.transaction.Transactional
import org.springframework.context.i18n.LocaleContextHolder as LCH

@Transactional
class MachineryLinkService {

  def grailsApplication
  def messageSource

  MachineryLink createMachineryLinkForThisInstance(Long companyId,String className,Machine machine){
    def clazz = grailsApplication.domainClasses.find{ it.clazz.simpleName == className }?.clazz

    if(!Machinery.class.isAssignableFrom(clazz)){
      throw new RuntimeException("Machinery is not assignable from ${className}")
    }

    MachineryLink machineryLink = MachineryLink.findByCompanyRefAndType(companyId,className) ?: new MachineryLink(companyRef:companyId,
                                                                                                                  type:className)
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
