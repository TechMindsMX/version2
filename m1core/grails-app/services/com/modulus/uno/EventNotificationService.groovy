package com.modulus.uno

import grails.transaction.Transactional
import com.modulus.uno.machine.MachineEvent

@Transactional
class EventNotificationService implements MachineEvent {

  NotifyService notifyService 

  void executeEvent() {
    log.debug("Sending the email...")
  }

}
