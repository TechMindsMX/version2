package com.modulus.uno.machine

import org.springframework.context.ApplicationContext
import grails.util.Holders
import groovyx.gpars.actor.DefaultActor

class MachineEventExecuterActorService extends SupervisedActor {
  
  def grailsApplication
  MachineEventExecuterSupervisorService machineEventExecuterSupervisorService

  void onException(Throwable throwable){
    machineEventExecuterSupervisorService << new SupervisedException(throwable)
  }

  void act(){
    loop{
      react { EventImplementer eventImplementer ->
        try {
          def service = Holders.getApplicationContext().getBean(eventImplementer.eventImplementerName)
          service.executeEvent(eventImplementer.instanceClassName,eventImplementer.instanceId)
        }
        catch(Exception e){
          log.error "${e.properties}"
        }
      }
    }
  }

}
