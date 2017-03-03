package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock

@TestFor(GroupNotificationService)
@Mock([GroupNotification, User])
class GroupNotificationServiceSpec extends Specification {

 def "Update a GroupNotification"(){
    given: "An existent notification group"
    def oldGroup = createGroup()
    def oldGroupName = oldGroup.name
    and: "two new users, a new name, and a new notification Emailer Id that I want add to this group"
    def user6= new User(username:"User6").save(validate:false)
    def user7= new User(username:"User7").save(validate:false)
    def groupForUpdate = new GroupNotification(id: 1, notificationId: "1234567890qwerty", users: [user6, user7], name:"ModulusUnoGroupUpdated")
    when: "We want to update the group"
    def groupUpdated = service.updateGroup(groupForUpdate)
    then:"We should get"
    groupUpdated.name == "ModulusUnoGroupUpdated"
    groupUpdated.name != oldGroupName
    groupUpdated.notificationId == "1234567890qwerty"
    groupUpdated.users.contains(user6)
    groupUpdated.users.contains(user7)
  }

  def "Get a list of users without groupNotification"(){
      given:"A list of users of corporate"
      def usersCorporate = createUserList()
      and: "A list of users within a group"
      def usersGroup = [usersCorporate[0],usersCorporate[1]]
      when:"We want to know the users without groupNotification"
      def usersWithoutGroup = service.findUserListWithoutGroup(usersGroup, usersCorporate)
      then:"We should get"
      usersWithoutGroup.contains(usersCorporate[2])
      usersWithoutGroup == usersCorporate[2..4]
  }

  def "Delete a notification group"(){
    given:"A group notification"
      GroupNotification firstGroup = createGroup()
    when:"we want to delete a group notification"
      service.deleteGroup(firstGroup.id)
    then:"We shouldn't have any group notification"
      !GroupNotification.findById(firstGroup.id)
  }

  private createGroup(){
    def userList = createUserList()
    def firstNotificationGroup = new GroupNotification(notificationId: "763gytdg327fgfg67fv5f", users: userList, name:"ModulusUnoGroup")
    firstNotificationGroup.save(validate:false)
    firstNotificationGroup
  }

  private createUserList(){
    (1..5).collect {
      def u = new User(username:"User$it").save(validate:false)
      u
    }
  }

}
