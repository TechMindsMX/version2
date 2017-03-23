package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock

@TestFor(EventNotificationService)
@Mock([State, GroupNotification, NotificationForState])
class EventNotificationServiceSpec extends Specification {

  MachineService machineService = Mock(MachineService)
  NotificationForStateService notificationForStateService = Mock(NotificationForStateService)

  def setup(){
    service.machineService = machineService
    service.notificationForStateService = notificationForStateService
  }

  void "Send email notification when execute an event"(){
    given:"a class name and a instance id"
      def order = new PurchaseOrder(providerName:"ProviderName").save(validate:false)
      State secondState = new State(name:"second state", finalState:true).save(validate:false)
      GroupNotification group = new GroupNotification(notificationId:"123456qwer", name:"Contadores").save(validate:false)
      NotificationForState notify = new NotificationForState(groupNotification:1, stateMachine:1).save(validate:false)
    and:
      machineService.getCurrentStateOfInstance(_) >> secondState
      notificationForStateService.findByState(_) >> notify
    when:
      service.executeEvent(order.class.simpleName, 1)
    then:
      true == false
      //3 * restService.sendEmailToEmailer(_)
  }

}
