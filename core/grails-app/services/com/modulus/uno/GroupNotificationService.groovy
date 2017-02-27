package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class GroupNotificationService {

  def corporateService

  def addNewGroup(def groupParams, def corporateId){
    //TODO: Problema al obtener los usuarios del checkbox, se estan registrando todos los usuarios del corpote.
   def corporateUsers = corporateService.findCorporateUsers(corporateId)
   createGroup(groupParams.nameGroup, groupParams.notificationId, corporateUsers)
  }

  def createGroup(String groupName, String notifyId, def usersList){
    def newGroup = new GroupNotification(name:groupName, notificationId:notifyId, users:usersList)
    newGroup.save()
  }

  def updateGroup(def groupId, String newNameGroup, ArrayList<User> newUserList, String newNotification){
    GroupNotification groupNotification = GroupNotification.findById(groupId)
      groupNotification.name=newNameGroup
      groupNotification.users=newUserList
      groupNotification.notificationId = newNotification
      groupNotification.save(validate:false)
  }

  def deleteGroup(def groupId){
    GroupNotification groupNotification = GroupNotification.findById(groupId)
    groupNotification.delete()
  }

  def getGroupsList(){
    GroupNotification.findAll()
  }

  def getGroup(def groupId){
    GroupNotification.findById(groupId)
  }

  //TODO: checar el toInteger()
  def getUserList(ArrayList<Integer> userIdList, ArrayList<User> userList){
    def users = userList.findAll{
    userIdList.contains(it.id.toInteger())
   }
  }

}
