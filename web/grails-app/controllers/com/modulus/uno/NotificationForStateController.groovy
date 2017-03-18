package com.modulus.uno
import com.modulus.uno.machine.*
import com.modulus.uno.NotificationForState

class NotificationForStateController {

  static allowedMethods = [save: "POST"]

  NotificationForStateService notificationForStateService

  def create(){
    def machine = Machine.findById(params.machineId)
    [
      groups: GroupNotification.findAll(),
      states: machine.states
    ]
  }

  def index(){
    def notificationsBody = notificationForStateService.findBodyNotifications(State.findAll())
    [
      machines: Machine.findAll(),
      notifications:notificationsBody
    ]
  }

  //TODO: OrderClass doesn't exist
  def save(NotificationForStateCommand command){
    NotificationForState notification = command.toNotification()
    notification.orderClass="ClassEmpty"
    notificationForStateService.createNotification(notification)
    redirect action:"index", method:"GET"
  }

  def edit(){
    [
      groups: GroupNotification.findAll(),
      notification: NotificationForState.get(params.id.toLong()),
      states: State.findAll()
    ]
  }

  def update(NotificationForStateCommand command){
    def notifyToUpdate = command.toUpdateNotification()
    notifyToUpdate.id = params.notification.toLong()
    notificationForStateService.updateNotification(notifyToUpdate)
    redirect action:"index", method:"GET"
  }

  def delete(){
    notificationForStateService.deleteNotification(params.id)
    redirect action:"index", method:"GET"
  }
}
