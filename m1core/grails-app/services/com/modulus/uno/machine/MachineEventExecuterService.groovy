package com.modulus.uno.machine

import grails.transaction.Transactional
import javax.annotation.PostConstruct

@Transactional
class MachineEventExecuterService{

  def grailsApplication
  MachineEventExecuterActorService machineEventExecuterActorService 

  @PostConstruct 
  private def init() {
    machineEventExecuterActorService.start()
  }

  def executeEvents(def instance){
    ArrayList<String> eventImplementerNames = grailsApplication.serviceClasses.findAll{ serviceClazz -> MachineEventImplementer.isAssignableFrom(serviceClazz.clazz) }*.propertyName
    
    eventImplementerNames.each{ eventImplementerName ->
      EventImplementer eventImplementer = new EventImplementer(eventImplementerName:eventImplementerName,instance:instance)
      machineEventExecuterActorService.send(eventImplementer)
    }

  }

}
