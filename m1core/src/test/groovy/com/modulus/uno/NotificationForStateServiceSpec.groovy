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
    and:"A new notification for update"
      def notificationForUpdate = new NotificationForState(id:1, groupNotification:2, stateMachine:3, orderClass:"SaleOrderUpdated")
    when:"We want to update the state from the notification"
      def updatedNotification = service.updateNotification(notificationForUpdate)
    then:"We should get the notification updated"
      updatedNotification.id == 1
      updatedNotification.groupNotification == 2
      updatedNotification.stateMachine == 3
      updatedNotification.groupNotification != 1
      updatedNotification.stateMachine != 2
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
