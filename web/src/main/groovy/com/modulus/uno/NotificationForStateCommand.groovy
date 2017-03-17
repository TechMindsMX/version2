package com.modulus.uno
import com.modulus.uno.NotificationForState
import grails.validation.Validateable

class NotificationForStateCommand implements Validateable{

  Long notification
  Long state
  def orderClass
  Long groupId

  NotificationForState toNotification(){
    new NotificationForState(
      groupNotification: groupId,
      stateMachine:state )
  }

  NotificationForState toUpdateNotification(){
    new NotificationForState(
      id:notification,
      groupNotification: groupId,
      stateMachine:state )
  }

}
