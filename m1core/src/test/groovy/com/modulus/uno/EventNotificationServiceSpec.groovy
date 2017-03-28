package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock
import com.modulus.uno.machine.*

@TestFor(EventNotificationService)
@Mock([PurchaseOrder, State, User, GroupNotification, NotificationForState])
class EventNotificationServiceSpec extends Specification {

  MachineService machineService = Mock(MachineService)
  NotificationForStateService notificationForStateService = Mock(NotificationForStateService)
  NotifyService notifyService = Mock(NotifyService)

  def setup(){
    service.machineService = machineService
    service.notificationForStateService = notificationForStateService
    service.notifyService = notifyService
  }

  void "Send email notification when execute an event"(){
    given:"a class name and a instance id"
      GroupNotification group = new GroupNotification(notificationId:"123456qwer", name:"Contadores").save(validate:false)
      def order = new PurchaseOrder(providerName:"ProviderName").save(validate:false)
      State firstState = new State(name:"machine state", finalState:true).save(validate:false)
      NotificationForState notifyOne = new NotificationForState(groupNotification:1, stateMachine:1).save(validate:false)
      NotificationForState notifyTwo = new NotificationForState(groupNotification:1, stateMachine:1).save(validate:false)
      ArrayList<NotificationForState> notifys = [notifyOne, notifyTwo]

    when:"we want to notify a group"
      service.executeEvent(order.class.simpleName, 1)

    then:"We should call the services"
      1 * machineService.getCurrentStateOfInstance(_) >> firstState
      1 * notificationForStateService.findByState(_) >> notifys
      1 * notifyService.sendEmailToGroup(_, _)
  }

}
