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
      def emailerStorage = emailerClientService.findAllSubjects()
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

    def edit(){
      def emailerSubjects = emailerClientService.findAllSubjects()
      def groupNotification = GroupNotification.findById(params.id)
      def emailerPreview = emailerClientService.findContent(groupNotification.notificationId)
      def usersCorporate = corporateService.findCorporateUsers(session.corporate.id)
      def usersWithoutGroup = groupNotificationService.findUserListWithoutGroup(groupNotification.users, usersCorporate)

      [
        group: groupNotification,
        subjects: emailerSubjects,
        preview: emailerPreview,
        usersEmpty: usersWithoutGroup
      ]
     }

    def update (GroupNotificationCommand command){
      def groupUpdated = command.toGroupNotificationUpdated()
      groupUpdated.users = User.findAllByIdInList(command.userList)
      groupUpdated.id = command.idGroup
      groupNotificationService.updateGroup(groupUpdated)
      redirect action:"index", method:"GET"
    }

    def delete(){
      groupNotificationService.deleteGroup(params.id)
      redirect action:"index", method:"GET"
    }

}

