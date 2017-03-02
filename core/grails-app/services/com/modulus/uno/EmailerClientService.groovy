package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class EmailerClientService {

  WsliteRequestService wsliteRequestService
  NotifyService notifyService

  def getEmailerStorage(){
    def storage = wsliteRequestService.doRequest("http://emailerv2.modulusuno.com"){
      endpointUrl "/show"
    }.doit().json
    def emailersList = getEmailerList(storage)
  }

  def getSubject(def idEmailer, def emailers){
    def emailer = emailers.find{
      it.containsValue(idEmailer)
    }
    emailer.subject
  }

  def sendNotifyToGroup(def idGroup, def paramsEmailer){
    GroupNotification group = GroupNotification.findById(idGroup)
    def userEmails = getEmails(group.users)
    notifyService.sendEmailNotifications(userEmails, group.notificationId, paramsEmailer)
  }

  private getEmails(def users){
    users.collect{ user ->
      user.profile.email
    }
  }

  private getEmailerList(def emailerStorage){
    def emailerList
    emailerList = emailerStorage.collect{ emailer ->
      ["id":emailer._id, "subject":emailer.subject]
    }
  }

}
