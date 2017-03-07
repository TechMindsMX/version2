package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class EmailerClientService {

  WsliteRequestService wsliteRequestService
  NotifyService notifyService
  EmailerDataService emailerDataService

 //TODO: Externalizar URL
  def callEmailerServiceStorage(){
    wsliteRequestService.doRequest("http://emailerv2.modulusuno.com"){
      endpointUrl "/show"
    }.doit().json
  }

  def findAllSubjects(){
    def storageEmailer = callEmailerServiceStorage()
    def emailersList = emailerDataService.obtainSubjectList(storageEmailer)
  }

  def findAllContents(){
    def storageEmailer = callEmailerServiceStorage()
    def emailersList =  emailerDataService.obtainContentList(storageEmailer)
  }

  def findSubject(def idEmailer){
    def emailerStorage = findAllSubjects()
    def emailerFound =   emailerDataService.findEmailer(idEmailer, emailerStorage)
    emailerFound.subject
  }

  def findContent(def idEmailer){
    def emailerStorage = findAllContents()
    def emailerFound =   emailerDataService.findEmailer(idEmailer, emailerStorage)
    emailerFound.content
  }

  //Servicio para implementar en máquina de estados.
  def sendNotifyToGroup(def idGroup, def paramsEmailer){
    GroupNotification group = GroupNotification.findById(idGroup)
    def userEmails =   emailerDataService.obtainEmailList(group.users)
    notifyService.sendEmailNotifications(userEmails, group.notificationId, paramsEmailer)
  }

}
