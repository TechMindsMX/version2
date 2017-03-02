package com.modulus.uno

class GroupNotificationCommand{

  Long idGroup
  String notificationId
  List<Long> userList
  String nameGroup

  def toDomain(){
    def usersToGroup = GroupNotificationService.getUserList(userList)
    GroupNotification group = new GroupNotification(name:nameGroup, notificationId:notificationId, users: usersToGroup)
  }

  def toMap(){
    def usersToGroup = GroupNotificationService.getUserList(userList)
    def updateMap = ["id":idGroup, "notification": notificationId, "name":nameGroup, "users":usersToGroup]
  }

}
