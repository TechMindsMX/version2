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
    def machine = Machine.findById(params.machineId)
    [
      machine: machine,
      notifications:NotificationForState.findAll(),
      groups:GroupNotification.findAll(),
      states: machine.states
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
    NotificationForState notify = NotificationForState.findById(params.id.toLong())
    [
      groups: GroupNotification.findAll(),
      notification: notify]
  }

  def update(){
  }

  //TODO
  def delete(){
    notificationForStateService.deleteNotification(params.id)
    redirect action:"index", method:"GET"
  }
}
