package com.modulus.uno

class GroupNotificationController {

    def emailerClientService
    def groupNotificationService
    def corporateService

    static allowedMethods = [save: "POST", update: "POST"]

    //ok
    def create() {
      def emailerStorage = emailerClientService.getEmailerStorage()
      render (view:"create", model: [emailers : emailerStorage, users:corporateService.findCorporateUsers(session.corporate.id)])
    }

    //ok
    def save(GroupNotificationCommand groupNotificationCommand){
      def usersCorporate = corporateService.findCorporateUsers(session.corporate.id)
      groupNotificationService.addNewGroup(groupNotificationCommand, usersCorporate)
      render (view:"show", model: [groups: groupNotificationService.getGroupsList()])
    }

    //TODO: falta usuarios
    def show() {
      render (view:"show", model: [groups: groupNotificationService.getGroupsList()])
    }

    //ok
    def edit(){
      def groupNotification = groupNotificationService.getGroup(params.id)
      def emailerStorage = emailerClientService.getEmailerStorage()
      def usersCorporate = corporateService.findCorporateUsers(session.corporate.id)
      render (view:"edit", model: [group: groupNotification, emailer: emailerStorage, users:usersCorporate])
     }

    //TODO: Probar
    def update(){
      log.info "------ Updating-----"*10
      //groupNotificationService.updateGroup(params.id, params.name, params.users, params.notifyId)
      //render (view:"show", model: [groups: groupNotificationService.getGroupsList()])
    }

    //ok
    def delete(){
      groupNotificationService.deleteGroup(params.id)
      render (view:"show", model: [groups: groupNotificationService.getGroupsList()])
    }

    /*

Carlo:
[ ] No haz revisado que show con el update y el delete.
[ ] Tienes probelmas con guardar los usuarios desde la vista.
[ ] En el servicio para guardar estas necesitando el corporate.session pero eso no va.

     */

}
