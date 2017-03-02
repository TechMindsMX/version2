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

    def save(GroupNotificationCommand command){
      def group = command.getGroupNotification()
      group.users = groupNotificationService.getUserList(command.userList)
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

    def update (GroupNotificationCommand command){
      def updateParams = command.getParams()
      updateParams.users = groupNotificationService.getUserList(command.userList)
      groupNotificationService.updateGroup(updateParams)
      redirect action:"index", method:"GET"
    }

    def delete(){
      groupNotificationService.deleteGroup(params.id)
      redirect action:"index", method:"GET"
    }

}


