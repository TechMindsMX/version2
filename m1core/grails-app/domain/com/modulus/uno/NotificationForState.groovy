package com.modulus.uno

class NotificationForState {

  Long groupNotification
  Long stateMachine
  String orderClass

  static constraints = {
    stateMachine blank:false
    groupNotification blank: false
    orderClass blank:false
  }

}
