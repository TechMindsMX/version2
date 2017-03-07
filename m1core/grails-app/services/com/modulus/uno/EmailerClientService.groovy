package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class EmailerClientService {

  WsliteRequestService wsliteRequestService
  NotifyService notifyService
  EmailerDataService emailerDataService

  def findAllSubjects(){
    def storageEmailer = obtainEmailerStorage()
    def emailersList = emailerDataService.obtainSubjectList(storageEmailer)
  }

  def findAllContents(){
    def storageEmailer = obtainEmailerStorage()
    def emailersList = emailerDataService.obtainContentList(storageEmailer)
  }

  def findSubject(def idEmailer){
    def emailerStorage = findAllSubjects()
    def emailerFound = emailerDataService.findEmailer(idEmailer, emailerStorage)
    emailerFound.subject
  }

  def findContent(def idEmailer){
    def emailerStorage = findAllContents()
    def emailerFound = emailerDataService.findEmailer(idEmailer, emailerStorage)
    emailerFound.content
  }

  def sendNotifyToGroup(def idGroup, def paramsEmailer){
    GroupNotification group = GroupNotification.findById(idGroup)
    def userEmails =   emailerDataService.obtainEmailList(group.users)
    notifyService.sendEmailNotifications(userEmails, group.notificationId, paramsEmailer)
  }

//TODO: Externalizar URL
  private obtainEmailerStorage(){
    wsliteRequestService.doRequest("http://emailerv2.modulusuno.com"){
      endpointUrl "/show"
    }.doit().json
  }
}
