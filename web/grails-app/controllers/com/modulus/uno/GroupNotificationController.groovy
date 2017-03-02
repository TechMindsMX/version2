package com.modulus.uno

class GroupNotificationController {

  static allowedMethods = [save: "POST"]

    def emailerClientService
    def groupNotificationService
    def corporateService

    def index() {
      [groups: groupNotificationService.getGroupsList()]
    }

    def create() {
      def emailerStorage = emailerClientService.getEmailerStorage()
      render (view:"create", model: [emailers : emailerStorage, users:corporateService.findCorporateUsers(session.corporate.id)])
    }

    //TODO: Pasar al command
    def save(GroupNotificationCommand command){
      def groupUsers = groupNotificationService.getUserList(command.userList)
      GroupNotification group = new GroupNotification(name:command.nameGroup, notificationId:command.notificationId, users:groupUsers)
      //groupNotificationService.saveNewGroup(group.toDomain())
      groupNotificationService.saveNewGroup(group)
      redirect action:"index", method:"GET"
    }

    def edit(){
      def emailerStorage = emailerClientService.getEmailerStorage()
      def groupNotification = groupNotificationService.getGroup(params.id)
      def usersCorporate = corporateService.findCorporateUsers(session.corporate.id)
      def usersWithoutGroup = groupNotificationService.getUserListWithoutGroup(groupNotification.users, usersCorporate)
      render (view:"edit", model: [group: groupNotification, emailer: emailerStorage, usersEmpty: usersWithoutGroup])
     }

    //TODO: Pasar al command
    def update (GroupNotificationCommand command){
      def groupUsers = groupNotificationService.getUserList(command.userList)
      def updateParams = ["id":command.idGroup, "name":command.nameGroup, "notification":command.notificationId, "users":groupUsers]
      //groupNotificationService.updateGroup(command.toMap())
      groupNotificationService.updateGroup(updateParams)
      redirect action:"index", method:"GET"
    }

    def delete(){
      groupNotificationService.deleteGroup(params.id)
      redirect action:"index", method:"GET"
    }

}


