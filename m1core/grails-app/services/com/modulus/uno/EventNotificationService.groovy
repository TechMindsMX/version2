package com.modulus.uno

import grails.transaction.Transactional
import com.modulus.uno.machine.MachineEventImplementer

class EventNotificationService implements MachineEventImplementer {

  def notificationForStateService
  def notifyService
  def machineService
  def grailsApplication

  void executeEvent(String className,Long instanceId) {
    Class clazz = grailsApplication.domainClasses.find { it.clazz.simpleName == className }.clazz
    def instance = clazz.findById(instanceId)
    def state = machineService.getCurrentStateOfInstance(instance)
    NotificationForState notify = notificationForStateService.findByState(state.id)
    notifyService.sendEmailToGroup(notify.groupNotification, instance.getNotificationData())
  }

}
