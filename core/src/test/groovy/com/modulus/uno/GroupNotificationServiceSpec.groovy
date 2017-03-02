package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock

@TestFor(GroupNotificationService)
@Mock([GroupNotification, User])
class GroupNotificationServiceSpec extends Specification {

  def "Delete a notification group"(){
    given:"A group notification"
      GroupNotification firstGroup = createGroup()
    when:"we want to delete a group notification"
      service.deleteGroup(firstGroup.id)
    then:"We shouldn't have any group notification"
      !GroupNotification.findById(firstGroup.id)
  }

  def "Get a list of notification groups"(){
    given:"Many notificationGroups"
      GroupNotification firstGroup = createGroup()
      GroupNotification secondGroup = createGroup()
      GroupNotification thirdGroup = createGroup()
    when:"I want to know all the notification groups"
      def groupsList = service.getGroupsList()
    then:"We should get a list"
      groupsList.contains(firstGroup)
      groupsList.contains(secondGroup)
      groupsList.contains(thirdGroup)
  }

  def "Find and get a specific groupNotification"(){
      given:"A groupNotifications"
      GroupNotification firstGroup = createGroup()
      GroupNotification secondGroup = createGroup()
      when:"We want to get the groupNotification with the id 2"
      def idToFind = 2
      def groupTwo = service.getGroup(idToFind)
      then:"We should get the groupNotification with the id 2"
      groupTwo.id == 2
      groupTwo.id != 1
  }

  def "Get a list of users without groupNotification"(){
      given:"A list of users of corporate"
      def usersCorporate = createUserList()
      and: "A list of users within a group"
      def usersGroup = [usersCorporate[0],usersCorporate[1]]
      when:"We want to know the users without groupNotification"
      def usersWithoutGroup = service.getUserListWithoutGroup(usersGroup, usersCorporate)
      then:"We should get"
      usersWithoutGroup.contains(usersCorporate[2])
      usersWithoutGroup == usersCorporate[2..4]
  }
  def "Update a GroupNotification"(){
    given: "An existent notification group"
    def group = createGroup()
    def oldGroupName = group.name
    and: "two new users that I want add to this group"
    def user6= new User(username:"User6").save(validate:false)
    def user7= new User(username:"User7").save(validate:false)
    and: "idGroup, new name's group, new notificationId, new user's list "
    def map = [id:1, name:"newGroup", notification:"123456789", users:[user6,user7]]
    when: "We want to update the group"
    //def updatedGroup = service.updateGroup(map)
    service.updateGroup(map)
    then:"We should get"
    group.name == "newGroup"
    group.name != oldGroupName
    group.notificationId == "123456789"
    group.users.contains(user6)
    group.users.contains(user7)
  }

/* An existenbt group:
    - Add users to the group
    - Change the notification
    - Remove users from the group
    - Change the name's group
    - All at the same time
*/

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
