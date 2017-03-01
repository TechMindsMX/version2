package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock

@TestFor(GroupNotificationService)
@Mock([GroupNotification, User])
class GroupNotificationServiceSpec extends Specification {

    def setup() {
    }

    def "Create a new notification group"() {
      given:"A users list"
        def userList = createUserList()
      and:"a notification id and a group name"
        def notificationId = "586d4944e1d4ae54524dd622"
        def groupName = "Contadores"
      when:"we want to save the group"
        def firstUserNotificationGroup = service.createGroup(groupName, notificationId, userList)
      then:"we should get"
        firstUserNotificationGroup.id == 1
        firstUserNotificationGroup.name == "Contadores"
        firstUserNotificationGroup.name != "Null-Contadores"
        firstUserNotificationGroup.notificationId == "586d4944e1d4ae54524dd622"
        firstUserNotificationGroup.users.contains(userList[0])
        firstUserNotificationGroup.users.contains(userList[1])
        firstUserNotificationGroup.users.contains(userList[2])
        firstUserNotificationGroup.users.contains(userList[3])
        firstUserNotificationGroup.users.contains(userList[4])
    }

    def "Update a notification group"() {
      given:"A groupNotification"
        GroupNotification firstGroup = createFirstUserGroup()
      and:"A new users list for update"
        def newUser1= new User().save(validate:false)
        def newUser2= new User().save(validate:false)
       ArrayList<User> newUserList = [newUser1, newUser2]
      and:"a  new notification id and a new name"
        def newNotificationId = "586d4944e1d4ae5diamon666"
        def newName = "ContadoresGroup"
      when:"we want to update the userList and the notificationId"
        service.updateGroup(firstGroup.id, newName, newUserList, newNotificationId)
      then:"we should get"
        firstGroup.name == "ContadoresGroup"
        firstGroup.notificationId == newNotificationId
        firstGroup.users.contains(newUser1)
        firstGroup.users.contains(newUser2)
    }

    def "Delete a notification group"(){
      given:"A group notification"
        GroupNotification firstGroup = createFirstUserGroup()
      when:"we want to delete a group notification"
        service.deleteGroup(firstGroup.id)
      then:"We shouldn't have any group notification"
        assert !GroupNotification.findById(firstGroup.id)
    }

    def "Get a list of notification groups"(){
      given:"Many notificationGroups"
        GroupNotification firstGroup = createFirstUserGroup()
        GroupNotification secondGroup = createFirstUserGroup()
        GroupNotification thirdGroup = createFirstUserGroup()
      when:"I want to know all the notification groups"
        def groupsList = service.getGroupsList()
      then:"We should get a list"
        groupsList.contains(firstGroup)
        groupsList.contains(secondGroup)
        groupsList.contains(thirdGroup)
    }

    def "Find and get a specific groupNotification"(){
        given:"A groupNotifications"
        GroupNotification firstGroup = createFirstUserGroup()
        GroupNotification secondGroup = createFirstUserGroup()
        when:"We want to get the groupNotification with the id 2"
        def idToFind = 2
        def groupTwo = service.getGroup(idToFind)
        then:"We should get the groupNotification with the id 2"
        groupTwo.id == 2
        groupTwo.id != 1
    }

    def "Get a user's list"(){
        given:"A set of users"
        def users = createUserList()
        and: "A list of id"
        def userIdList = [1,2]
        when: "We want to get a list of users"
        def userList = service.getUserList(userIdList, users)
        then: "We should get a list of users"
        userList.contains(users[0])
        userList.contains(users[1])
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
    }

      private createFirstUserGroup(){
        def userList = createUserList()
        def firstNotificationGroup = new GroupNotification(notificationId: "763gytdg327fgfg67fv5f", users: userList, name:"ModulusUnoGroup")
        firstNotificationGroup.save(validate:false)
        firstNotificationGroup
    }

       private createUserList(){
        def user1= new User(username:"User1").save(validate:false)
        def user2= new User(username:"User2").save(validate:false)
        def user3= new User(username:"User3").save(validate:false)
        def user4= new User(username:"User4").save(validate:false)
        def user5= new User(username:"User5").save(validate:false)
        ArrayList<User> userList = [user1, user2, user3, user4, user5]
    }

}
