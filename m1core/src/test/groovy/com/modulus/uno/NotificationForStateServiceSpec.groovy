package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock
import com.modulus.uno.machine.*

@TestFor(NotificationForStateService)
@Mock([NotificationForState, GroupNotification, State])
class NotificationForStateServiceSpec extends Specification {

  def setup() {
  }

  void "Create a new notification per State"(){
    given:"A new notification per state from machine"
      def newNotification = new NotificationForState(groupNotification:10, stateMachine:6)
    when:"We want to save the new notification"
      def notify = service.createNotification(newNotification)
    then:"We should get 1 notification"
      notify.groupNotification == 10
      notify.stateMachine == 6
  }

  void "Modify a state machine from existing notification"(){
    given:"A notification"
      def oldNotification = new NotificationForState(groupNotification:1, stateMachine:2).save(validate:false)
    and:"A new notification for update"
      def notificationForUpdate = new NotificationForState(id:1, groupNotification:2, stateMachine:3)
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
      def notification = new NotificationForState(groupNotification:1, stateMachine:2).save(validate:false)
    when:"We delete the notification"
      service.deleteNotification(notification.id)
    then:"We shouldn't have the notification"
      !NotificationForState.findById(1)
  }

  void "Get notifications for states"(){
    given:"A list of states"
      def states = (1..10).collect{
        def s = new State(name:"State$it").save(validate:false)
      }
    and:"A list of groups"
      def groups = (1..3).collect{
        def s = new GroupNotification(name:"Group$it").save(validate:false)
      }
    and:"A notifications for state"
      def notify1 = new NotificationForState(groupNotification:1, stateMachine:2).save(validate:false)
      def notify2 = new NotificationForState(groupNotification:2, stateMachine:4).save(validate:false)
    when:"We want to know the notification's body for states"
      def contentNotification = service.findBodyNotifications(states)
    then:"We should get the state and groupName of every notification"
      contentNotification == [[id:1, stateName:"State2", groupName:"Group1"], [id:2, stateName:"State4", groupName:"Group2"]]
  }

  void "Find the notification by state id"(){
    given:"A state id"
      def notification = new NotificationForState(groupNotification:1, stateMachine:2).save(validate:false)
      Long state = 2
    when:"I want to know the notification"
      def notify = service.findByState(state)
    then:"We should get a list with the notification"
      notify.size == 1
      notify[0].stateMachine == 2
      notify[0].groupNotification == 1
  }

  void "Find all  notifications with a state id"(){
    given:"A state id"
      def notification = new NotificationForState(groupNotification:1, stateMachine:2).save(validate:false)
      def notification2 = new NotificationForState(groupNotification:2, stateMachine:2).save(validate:false)
      def notification3 = new NotificationForState(groupNotification:3, stateMachine:2).save(validate:false)
      Long state = 2
    when:"I want to know the notification"
      def notify = service.findByState(state)
    then:"We should get a list with the notification"
      notify.size == 3
  }

  void "Delete group notifications associate"(){
    given:"A group id"
      Long group = 1
    and:"a notifications"
      def notification1 = new NotificationForState(groupNotification:1, stateMachine:1).save(validate:false)
      def notification2 = new NotificationForState(groupNotification:1, stateMachine:2).save(validate:false)
      def notification3 = new NotificationForState(groupNotification:1, stateMachine:3).save(validate:false)
    when:"We want to delete the notifications with the same group"
      service.deleteGroupNotifications(group)
    then:"We couldn't found the notifications"
      !NotificationForState.findById(1)
      !NotificationForState.findById(2)
      !NotificationForState.findById(3)
  }
 }
