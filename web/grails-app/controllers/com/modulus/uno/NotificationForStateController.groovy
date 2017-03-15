package com.modulus.uno
import com.modulus.uno.machine.*

class NotificationForStateController {

  static allowedMethods = [save: "POST"]

  NotificationForStateService notificationForStateService

  def index() {
    [machines: Machine.findAll()]
  }

  def create(){
    def machine = Machine.findById(params.id)
    [
      groups: GroupNotification.findAll(),
      states: machine.states
    ]
  }

  def show(){
    [
      machine:Machine.findById(params.machineId),
      notifications:[],
      groups:GroupNotification.findAll()
    ]
  }

  //TODO: OrderClass doesn't exist
  def save(NotificationForStateCommand command){
    NotificationForState notification = command.toNotification()
    notification.orderClass="ClassEmpty"
    notificationForStateService.createNotification(notification)
    redirect action:"index", method:"GET"
  }
}
