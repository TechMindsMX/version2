package com.modulus.uno.machine

import grails.transaction.Transactional
import org.springframework.context.ApplicationContext
import grails.util.Holders

@Transactional
class MachineEventExecuterService{

  def grailsApplication

  def executeEvents(def instance){
    ArrayList<MachineEvent> events = grailsApplication.serviceClasses.findAll{ serviceClazz -> MachineEvent.isAssignableFrom(serviceClazz.clazz) }
    ApplicationContext ctx = Holders.getApplicationContext()

    events.each{ event ->
      def service = ctx.getBean(event.propertyName)
      service.executeEvent(instance)
    }
  }

}
