package com.modulus.uno
import com.modulus.uno.GroupNotification

class GroupNotificationCommand{

  Long idGroup
  String notificationId
  List<Long> userList
  String nameGroup

  GroupNotification getGroupNotification(){
    new GroupNotification(
                                 notificationId:notificationId,
                                 name:nameGroup)
  }

  def getParams(){
    ["id":idGroup,"name":nameGroup, "notification":notificationId]
  }
}
