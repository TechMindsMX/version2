package com.modulus.uno

class GroupNotificationController {

  static allowedMethods = [save: "POST", otherMethod: "POST"]

    def emailerClientService
    def groupNotificationService
    def corporateService

    def create() {
      def emailerStorage = emailerClientService.getEmailerStorage()
      render (view:"create", model: [emailers : emailerStorage, users:corporateService.findCorporateUsers(session.corporate.id)])
    }

    def save(GroupNotificationCommand groupNotificationCommand){
      def usersCorporate = corporateService.findCorporateUsers(session.corporate.id)
      groupNotificationService.addNewGroup(groupNotificationCommand, usersCorporate)
      render (view:"show", model: [groups: groupNotificationService.getGroupsList()])
    }

    def show() {
      render (view:"show", model: [groups: groupNotificationService.getGroupsList()])
    }

    def edit(){
      def groupNotification = groupNotificationService.getGroup(params.id)
      def emailerStorage = emailerClientService.getEmailerStorage()
      def usersCorporate = corporateService.findCorporateUsers(session.corporate.id)
      def usersWithoutGroup = groupNotificationService.getUserListWithoutGroup(groupNotification.users, usersCorporate)
      render (view:"edit", model: [group: groupNotification, emailer: emailerStorage, usersEmpty: usersWithoutGroup])
     }

    def update (GroupNotificationCommand groupNotificationCommand){
      def usersCorporate = corporateService.findCorporateUsers(session.corporate.id)
      groupNotificationService.editGroup(groupNotificationCommand, usersCorporate)
      render (view:"show", model: [groups: groupNotificationService.getGroupsList()])
    }


    def delete(){
        groupNotificationService.deleteGroup(params.id)
      render (view:"show", model: [groups: groupNotificationService.getGroupsList()])
    }

}


