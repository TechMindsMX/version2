package com.modulus.uno.machine

import grails.transaction.Transactional
import javax.annotation.PostConstruct

@Transactional
class MachineEventExecuterService{

  def grailsApplication
  MachineEventExecuterSupervisorService machineEventExecuterSupervisorService
  MachineEventExecuterActorService machineEventExecuterActorService

  @PostConstruct 
  private def init() {
    machineEventExecuterActorService.start()
    machineEventExecuterSupervisorService.start()
    machineEventExecuterSupervisorService.link(machineEventExecuterActorService)
  }

  def executeEvents(def instance){
    ArrayList<String> eventImplementerNames = grailsApplication.serviceClasses.findAll{ serviceClazz -> MachineEventImplementer.isAssignableFrom(serviceClazz.clazz) }*.propertyName

    eventImplementerNames.each{ eventImplementerName ->
      EventImplementer eventImplementer = new EventImplementer(eventImplementerName:eventImplementerName,
                                                               instanceClassName:instance.class.simpleName,
                                                               instanceId:instance.id)

      machineEventExecuterSupervisorService.send(eventImplementer)
    }

  }

}
