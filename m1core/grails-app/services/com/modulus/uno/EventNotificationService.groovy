package com.modulus.uno

import grails.transaction.Transactional
import com.modulus.uno.machine.MachineEventImplementer

class EventNotificationService implements MachineEventImplementer {

  NotifyService notifyService 

  void executeEvent(def instance) {
    log.debug("Sending the email...")
  }

}
