package com.modulus.uno.machine

import org.springframework.context.ApplicationContext
import groovyx.gpars.actor.DefaultActor

class MachineEventExecuterActorService extends DefaultActor {

  static scope = "prototype"

  MachineEventExecuterSupervisorService machineEventExecuterSupervisorService
  def grailsApplication

  void onException(Throwable throwable){
    machineEventExecuterSupervisorService << new SupervisedException(throwable.message)
  }

  void act(){
    loop{
      react { EventImplementer eventImplementer ->
        def service = grailsApplication.mainContext.getBean(eventImplementer.eventImplementerName)
        service.executeEvent(eventImplementer.instanceClassName,eventImplementer.instanceId)
      }
    }
  }

}
