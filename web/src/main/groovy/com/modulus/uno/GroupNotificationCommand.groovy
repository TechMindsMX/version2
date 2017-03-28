package com.modulus.uno
import com.modulus.uno.GroupNotification
import grails.validation.Validateable

class GroupNotificationCommand implements Validateable{

  Long idGroup
  String notificationId
  List<Long> userList
  String nameGroup

  GroupNotification toGroupNotification(){
    new GroupNotification(
      notificationId:notificationId,
      name:nameGroup)
  }

  GroupNotification toGroupNotificationUpdated(){
    new GroupNotification(
      id: idGroup,
      notificationId:notificationId,
      name:nameGroup)
  }
}
