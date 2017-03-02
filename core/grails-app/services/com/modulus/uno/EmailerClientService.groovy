package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class EmailerClientService {

  WsliteRequestService wsliteRequestService

  def getEmailerStorage(){
    def storage = wsliteRequestService.doRequest("http://emailerv2.modulusuno.com"){
      endPointUrl "/show"
    }.doit().json
    def emailersList = getEmailerList(storage)
  }

  def getSubject(def idEmailer, def emailers){
    def emailer = emailers.find{
     it.containsValue(idEmailer)
    }
    emailer.subject
  }

  private getEmailerList(def emailerStorage){
    def emailerList
    emailerList = emailerStorage.collect{ emailer ->
      ["id":emailer._id, "subject":emailer.subject]
    }
  }

}
