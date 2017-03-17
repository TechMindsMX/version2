package com.modulus.uno
import grails.transaction.Transactional

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

}
