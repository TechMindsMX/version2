package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class EmailerClientService {

  WsliteRequestService wsliteRequestService
  NotifyService notifyService

  def findAllSubjects(){
    def storageEmailer = callEmailerServiceStorage()
    def emailersList = obtainSubjectList(storageEmailer)
  }

  def findAllContents(){
    def storageEmailer = callEmailerServiceStorage()
    def emailersList = obtainContentList(storageEmailer)
  }

  def findSubject(def idEmailer){
    def emailerStorage = findAllSubjects()
    def emailerFound = findEmailer(idEmailer, emailerStorage)
    emailerFound.subject
  }

  def findContent(def idEmailer){
    def emailerStorage = findAllContents()
    def emailerFound = findEmailer(idEmailer, emailerStorage)
    emailerFound.content
  }

  def findEmailer(def idEmailer, def emailerStorage){
    emailerStorage.find{
      it.containsValue(idEmailer)
    }
  }

  //Servicio para implementar en máquina de estados.
  def sendNotifyToGroup(def idGroup, def paramsEmailer){
    GroupNotification group = GroupNotification.findById(idGroup)
    def userEmails = obtainEmailList(group.users)
    notifyService.sendEmailNotifications(userEmails, group.notificationId, paramsEmailer)
  }

  //TODO: Externalizar URL
  private callEmailerServiceStorage(){
    wsliteRequestService.doRequest("http://emailerv2.modulusuno.com"){
      endpointUrl "/show"
    }.doit().json
  }

  private obtainEmailList(def users){
    users.collect{ user ->
      user.profile.email
    }
  }

  private obtainSubjectList(def emailerStorage){
    emailerStorage.collect{ emailer ->
      ["id":emailer._id, "subject":emailer.subject]
    }
  }

  private obtainContentList(def emailerStorage){
    emailerStorage.collect{ emailer ->
      ["id":emailer._id, "content":emailer.content]
    }
  }
}
