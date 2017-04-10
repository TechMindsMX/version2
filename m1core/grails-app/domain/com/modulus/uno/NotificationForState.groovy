package com.modulus.uno

class NotificationForState {

  Long groupNotification
  Long stateMachine

  static constraints = {
    stateMachine blank:false
    groupNotification blank: false
  }

}
