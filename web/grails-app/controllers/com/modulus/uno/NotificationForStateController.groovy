package com.modulus.uno
import com.modulus.uno.machine.*

class NotificationForStateController {

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
}
