package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class EmailerDataService {

  def obtainEmailList(List<User> users){
    users.collect{ user ->
      user.profile.email
    }
  }

  def obtainSubjectList(List emailerStorage){
    emailerStorage.collect{ emailer ->
      ["id":emailer._id, "subject":emailer.subject]
    }
  }

  def obtainContentList(List emailerStorage){
    emailerStorage.collect{ emailer ->
      ["id":emailer._id, "content":emailer.content]
    }
  }

  def findEmailer(String idEmailer, List emailerStorage){
    emailerStorage.find{
      it.containsValue(idEmailer)
    }
  }
}
