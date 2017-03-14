package com.modulus.uno.machine

import org.springframework.context.ApplicationContext
import grails.util.Holders
import groovyx.gpars.actor.DefaultActor

class MachineEventExecuterActorService extends DefaultActor {
  
  def grailsApplication

  void act(){
    loop{
      react { EventImplementer eventImplementer ->
        try {
          def service = Holders.getApplicationContext().getBean(eventImplementer.eventImplementerName)
          service.executeEvent(eventImplementer.instance)
        }
        catch(Exception e){
          log.error "${e.properties}"
        }
      }
    }
  }

}
