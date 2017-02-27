package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class GroupNotificationService {

  def corporateService

  //TODO: No sé si necesita refactor junto a createGroup.
  //CHECHAR: Long2Integer y toInteger
  def addNewGroup(def command, ArrayList<User> users){
    def usersList = getUserList(command.userList, users)
    createGroup(command.nameGroup, command.notificationId, usersList)
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
  def getUserList(def  userIdList, def userList){
   def users = userList.findAll{
    userIdList.contains(it.id.toInteger())
   }
  }

}
