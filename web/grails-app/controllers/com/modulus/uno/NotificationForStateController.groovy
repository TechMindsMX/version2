package com.modulus.uno
import com.modulus.uno.machine.*
import com.modulus.uno.NotificationForState

class NotificationForStateController {

  static allowedMethods = [save: "POST"]

  def springSecurityService
  CompanyService companyService
  CorporateService corporateService
  NotificationForStateService notificationForStateService
  MachineryLinkService machineryLinkService

  def create(String id){
    def machine = Machine.findByUuid(id)
    [groups: GroupNotification.findAll(),
     states: machine.states.findAll{ state -> state.id != machine.initialState.id }]
  }

  def index(){
    [notifications:notificationForStateService.findBodyNotifications(State.findAll())]
  }

  def register(){
    User user =  springSecurityService.currentUser
    Corporate corporate = corporateService.findCorporateOfUser(user)
    ArrayList<Company> companies = companyService.findCompaniesByCorporateAndStatus(CompanyStatus.ACCEPTED,corporate.id)

    [entities:machineryLinkService.getClassesWithMachineryInterface(),
     companies:companies,
     machines: Machine.findAll()]
  }

  def save(NotificationForStateCommand command){
    NotificationForState notification = command.toNotification()
    notificationForStateService.createNotification(notification)
    redirect action:"index", method:"GET"
  }

  def edit(){
    def notify = NotificationForState.get(params.id.toLong())
    def state = State.findById(notify.stateMachine)
    Machine machine = state.machine
    [
      groups: GroupNotification.findAll(),
      notification: notify,
      states: machine.states.findAll{ machineState -> machineState.id != machine.initialState.id }
    ]
  }

  def update(NotificationForStateCommand command){
    def notifyToUpdate = command.toUpdateNotification()
    notifyToUpdate.id = params.notification.toLong()
    notificationForStateService.updateNotification(notifyToUpdate)
    redirect action:"index", method:"GET"
  }

  def delete(){
    notificationForStateService.deleteNotification(params.id.toLong())
    redirect action:"index", method:"GET"
  }
}
