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
      def emailerStorage = emailerClientService.findEmailerStorageSubjects()
      [
        emailers : emailerStorage,
        users:corporateService.findCorporateUsers(session.corporate.id)
      ]
    }

    def save(GroupNotificationCommand command){
      def group = command.toGroupNotification()
      group.users = User.findAllByIdInList(command.userList)
      groupNotificationService.saveNewGroup(group)
      redirect action:"index", method:"GET"
    }

    //TODO: Necesita Refactor
    def edit(){
      def emailerSubjects = emailerClientService.findEmailerStorageSubjects()
      def emailerContents = emailerClientService.findEmailerStorageContents()
      def groupNotification = GroupNotification.findById(params.id)
      def preview = emailerClientService.findContent(groupNotification.notificationId, emailerContents)
      def usersCorporate = corporateService.findCorporateUsers(session.corporate.id)
      def usersWithoutGroup = groupNotificationService.findUserListWithoutGroup(groupNotification.users, usersCorporate)

      [
        group: groupNotification,
        emailerStorage: emailerSubjects,
        emailerPreview: preview,
        usersEmpty: usersWithoutGroup
      ]
     }

    def update (GroupNotificationCommand command){
      def groupUpdated = command.toGroupNotificationUpdated()
      groupUpdated.users = User.findAllByIdInList(command.userList)
      groupNotificationService.updateGroup(groupUpdated)
      redirect action:"index", method:"GET"
    }

    def delete(){
      groupNotificationService.deleteGroup(params.id)
      redirect action:"index", method:"GET"
    }

}

