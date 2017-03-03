package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class EmailerClientService {

  WsliteRequestService wsliteRequestService
  NotifyService notifyService

  def findEmailerStorageSubjects(){
    def storage = wsliteRequestService.doRequest("http://emailerv2.modulusuno.com"){
      endpointUrl "/show"
    }.doit().json
    def emailersList = findEmailerSubjects(storage)
  }

  def findEmailerStorageContents(){
    def storage = wsliteRequestService.doRequest("http://emailerv2.modulusuno.com"){
      endpointUrl "/show"
    }.doit().json
    def emailersList = findEmailerContents(storage)
  }

  def findSubject(def idEmailer, def emailers){
    def emailer = emailers.find{
      it.containsValue(idEmailer)
    }
    emailer.subject
  }

  def findContent(def idEmailer, def emailers){
    def emailer = emailers.find{
      it.containsValue(idEmailer)
    }
    emailer.content
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

  private findEmailerSubjects(def emailerStorage){
    emailerStorage.collect{ emailer ->
      ["id":emailer._id, "subject":emailer.subject]
    }
  }

  private findEmailerContents(def emailerStorage){
    emailerStorage.collect{ emailer ->
      ["id":emailer._id, "content":emailer.content]
    }
  }
}
