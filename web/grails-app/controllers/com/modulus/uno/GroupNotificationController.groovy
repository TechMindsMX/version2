package com.modulus.uno

class GroupNotificationController {

  static allowedMethods = [save: "POST"]

    def emailerClientService
    def groupNotificationService
    def corporateService

    def index() {
      [groups: GroupNotification.findAll()]
    }

    def create() {
      def emailerStorage = emailerClientService.getEmailerStorage()
      [
        emailers : emailerStorage,
        users:corporateService.findCorporateUsers(session.corporate.id)
      ]
    }

    def save(GroupNotificationCommand command){
      def group = command.getGroupNotification()
      group.users = User.findAllByIdInList(command.userList)
      groupNotificationService.saveNewGroup(group)
      redirect action:"index", method:"GET"
    }

    def edit(){
      def emailerStorage = emailerClientService.getEmailerStorage()
      def groupNotification = GroupNotification.findById(params.id)
      def usersCorporate = corporateService.findCorporateUsers(session.corporate.id)
      def usersWithoutGroup = groupNotificationService.findUserListWithoutGroup(groupNotification.users, usersCorporate)

      [
        group: groupNotification,
        emailer: emailerStorage,
        usersEmpty: usersWithoutGroup
      ]
     }

    def update (GroupNotificationCommand command){
      def updateParams = command.getParams()
      updateParams.users = User.findAllByIdInList(command.userList)
      groupNotificationService.updateGroup(updateParams)
      redirect action:"index", method:"GET"
    }

    def delete(){
      groupNotificationService.deleteGroup(params.id)
      redirect action:"index", method:"GET"
    }

}

