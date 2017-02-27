package com.modulus.uno

class GroupNotificationController {

    def emailerClientService
    def groupNotificationService
    def corporateService

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

    //TODO: Acomodar vista para mostrar los grupos
    //ok
    def show() {
      render (view:"show", model: [groups: groupNotificationService.getGroupsList()])
    }

    //TODO: probar: mostrar datos en el formulario
    def edit(){
      def groupNotification = groupNotificationService.getGroup(params.id)
      def emailerStorage = emailerClientService.getEmailerStorage()
      //render (view:"edit", model: [group: groupNotification, emailers:emailerStorage, users:corporateService.findCorporateUsers(session.corporate.id)])
      render (view:"edit", model: [group: groupNotification])
     }

    //TODO: Probar
    def update(){
      log.info "------mexican debugger-----"*10
      log.info params.dump()
      groupNotificationService.updateGroup(params.id, params.name, params.users, params.notifyId)
      render (view:"show", model: [groups: groupNotificationService.getGroupsList()])
    }

    //TODO: probar esto
    def delete(){
      log.info "------mexican debugger-----"*10
      log.info params.id
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
