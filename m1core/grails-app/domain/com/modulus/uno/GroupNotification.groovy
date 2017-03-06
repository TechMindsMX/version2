package com.modulus.uno

class GroupNotification {

  String notificationId
  String name

  static hasMany = [
    users:User
  ]

  static constraints = {
    notificationId blank:false
    users blank: false
    name blank: false
  }
}
