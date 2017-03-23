package com.modulus.uno
import grails.transaction.Transactional
import com.modulus.uno.machine.*

@Transactional
class NotificationForStateService {

  def createNotification(NotificationForState notify){
    notify.save()
    notify
  }

  def updateNotification(NotificationForState notify){
    NotificationForState notification = NotificationForState.findById(notify.id)
    notification.stateMachine = notify.stateMachine
    notification.groupNotification = notify.groupNotification
    notification.save()
  }

  def deleteNotification(def notifyId){
    NotificationForState notify = NotificationForState.findById(notifyId)
    notify.delete()
  }

  def findBodyNotifications(ArrayList<State> states){
    def notifications = NotificationForState.findAllByStateMachineInList(states*.id)
    ArrayList<Map> notificationForStatesBody = []
      notifications.each{ notify ->
        notificationForStatesBody << [id:notify.id ,stateName:  states.find{ it.id == notify.stateMachine }.name, groupName: GroupNotification.findById(notify.groupNotification).name]
      }
    notificationForStatesBody
  }

  def findByState(Long state){
    NotificationForState notify = NotificationForState.find{ stateMachine == state }
    println notify.dump()
      notify
  }

}
