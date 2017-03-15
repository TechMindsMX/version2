package com.modulus.uno

class NotificationForState {

  def stateMachine
  Long groupNotificationId
  def orderClass

  static constraints = {
    stateMachine blank:false
    users blank: false
    name blank: false
  }

}
