package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class EmailerClientService {

  WsliteConnectionService wsliteConnectionService

  def getEmailerStorage(){
    def storage = wsliteConnectionService.get("http://emailerv2.modulusuno.com", "/show")
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
