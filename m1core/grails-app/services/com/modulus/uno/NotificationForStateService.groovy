package com.modulus.uno

class NotificationForStateService {

  def createNotification(NotificationForState notify){
    notify.save()
    notify
  }

  def updateState(Long notifyId, Long newStateId){
    NotificationForState notification = NotificationForState.findById(notifyId)
    notification.stateMachine = newStateId
    notification.save()
    notification
  }

  def deleteNotification(Long notifyId){
    NotificationForState notify = NotificationForState.findById(notifyId)
    notify.delete()
  }

}
