package com.modulus.uno

import grails.transaction.Transactional
import com.modulus.uno.machine.MachineEventImplementer

class EventNotificationService implements MachineEventImplementer {

  NotifyService notifyService 
  def grailsApplication

  void executeEvent(String className,Long instanceId) {
    log.debug("Sending the email...")
  }

}
