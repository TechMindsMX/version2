package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock

@TestFor(NotificationForStateService)
@Mock([NotificationForState])
class NotificationForStateServiceSpec extends Specification {

  def setup() {
  }

  void "Create a new notification per State"(){
    given:"A new notification per state from machine"
      def newNotification = new NotificationForState(groupNotification:10, stateMachine:6, orderClass: "saleOrder")
    when:"We want to save the new notification"
      def notify = service.createNotification(newNotification)
    then:"We should get 1 notification"
      notify.groupNotification == 10
      notify.stateMachine == 6
      notify.orderClass == "saleOrder"
  }

  void "Modify a state machine from existing notification"(){
    given:"A notification"
      def oldNotification = new NotificationForState(groupNotification:1, stateMachine:2, orderClass:"SaleOrder").save(validate:false)
    and:"A new State Machine id"
      Long stateToUpdate = 3
    when:"We want to update the state from the notification"
      def updatedNotification = service.updateState(oldNotification.id, stateToUpdate)
    then:"We should get the notification updated"
      updatedNotification.groupNotification == 1
      updatedNotification.stateMachine == 3
  }

  void "Delete an existing notification"(){
    given:"An existing notification"
      def notification = new NotificationForState(groupNotification:1, stateMachine:2, orderClass:"SaleOrder").save(validate:false)
    when:"We delete the notification"
      service.deleteNotification(notification.id)
    then:"We shouldn't have the notification"
      !NotificationForState.findById(1)
  }

 }
