package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class GroupNotificationService {

  def corporateService

  def saveNewGroup(GroupNotification group){
    group.save()
  }

  def updateGroup(GroupNotification groupUpdated){
    GroupNotification groupNotification = GroupNotification.findById(groupUpdated.id)
    groupNotification.name=groupUpdated.name
    groupNotification.users=groupUpdated.users
    groupNotification.notificationId =groupUpdated.notificationId
    groupNotification.save()
    groupNotification
  }

  def deleteGroup(def groupId){
    GroupNotification groupNotification = GroupNotification.findById(groupId)
    groupNotification.delete()
  }

  def findUserListWithoutGroup(def usersWithGroup, def usersCorporate){
    usersCorporate - usersWithGroup
  }

}
