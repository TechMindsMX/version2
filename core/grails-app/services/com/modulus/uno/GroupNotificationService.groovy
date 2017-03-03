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
