package com.modulus.uno

import grails.transaction.Transactional
import com.modulus.uno.machine.MachineEventImplementer

@Transactional
class EventNotificationService implements MachineEventImplementer {
  
  NotificationForStateService notificationForStateService
  def notifyService
  def machineService
  def grailsApplication

  void executeEvent(String className,Long instanceId) {
    Class clazz = grailsApplication.domainClasses.find { it.clazz.simpleName == className }.clazz
    def instance = clazz.findById(instanceId)
    def state = machineService.getCurrentStateOfInstance(instance)
    
    ArrayList<NotificationForState> notifys = notificationForStateService.findByState(state.id)
    notifyService.sendEmailToGroup(notifys, instance.getNotificationData())
  }

}
