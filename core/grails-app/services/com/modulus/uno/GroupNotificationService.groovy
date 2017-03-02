package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class GroupNotificationService {

  def corporateService

  def saveNewGroup(GroupNotification group){
    group.save()
  }

  def updateGroup(def updateParams){
     GroupNotification groupNotification = GroupNotification.findById(updateParams.id)
      groupNotification.name=updateParams.name
      groupNotification.users=updateParams.users
      groupNotification.notificationId = updateParams.notification
      groupNotification.save()
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

  def getUserList(def  userIdList){
   userIdList.collect{
     User.findById(it.toLong())
   }
  }

  def getUserListWithoutGroup(def usersWithGroup, def usersCorporate){
    def usersWithoutGroup = usersCorporate.findAll {
     !usersWithGroup.contains( it )
    }
  }

}
