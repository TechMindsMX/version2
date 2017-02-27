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
        def user1= new User(username:"User1",enabled:true,
        profile:new Profile(name:"User1", email:"user1@me.com")).save(validate:false)
        def user2= new User(username:"User2",enabled:true,
        profile:new Profile(name:"User2", email:"user2@me.com")).save(validate:false)
        def user3= new User(username:"User3", enabled:true,
        profile:new Profile(name:"User3", email:"user3@me.com")).save(validate:false)
        def user0= new User(username:"User0", enabled:true,
        profile:new Profile(name:"User0", email:"user0@me.com")).save(validate:false)
        ArrayList<User> userList = [user1, user2, user3]
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
        assert firstUserNotificationGroup.users.contains(user1)
        assert firstUserNotificationGroup.users.contains(user2)
        assert firstUserNotificationGroup.users.contains(user3)
        assert !firstUserNotificationGroup.users.contains(user0)
    }

    def "Update a notification group"() {
      given:"A groupNotification"
        GroupNotification firstGroup = createFirstUserGroup()
      and:"A new users list for update"
        def user4= new User(username:"newUser1",enabled:true,
        profile:new Profile(name:"newUser1", email:"user1new@me.com")).save(validate:false)
        def user5= new User(username:"newUser2",enabled:true,
        profile:new Profile(name:"newUser2", email:"user2new@me.com")).save(validate:false)
        def user6= new User(username:"newUser3", enabled:true,
        profile:new Profile(name:"newUser3", email:"user3new@me.com")).save(validate:false)
        ArrayList<User> newUserList = [user4, user5, user6]
      and:"a  new notification id and a new name"
        def newNotificationId = "586d4944e1d4ae5diamon666"
        def newName = "ContadoresGroup"
      when:"we want to update the userList and the notificationId"
        service.updateGroup(firstGroup.id, newName, newUserList, newNotificationId)
      then:"we should get"
        firstGroup.name == "ContadoresGroup"
        firstGroup.notificationId == newNotificationId
        assert firstGroup.users.contains(user4)
        assert firstGroup.users.contains(user5)
        assert firstGroup.users.contains(user6)
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
        def user1= new User(username:"User1",enabled:true,
        profile:new Profile(name:"User1", email:"user1@me.com")).save(validate:false)
        def user2= new User(username:"User2",enabled:true,
        profile:new Profile(name:"User2", email:"user2@me.com")).save(validate:false)
        def user3= new User(username:"User3", enabled:true,
        profile:new Profile(name:"User3", email:"user3@me.com")).save(validate:false)
        def listUser = [user1,user2,user3]
        and: "A list of id"
        def userIdList = [1,2]
        when: "We want to get a list of users"
        def userList = service.getUserList(userIdList,listUser)
        then: "We should get a list of users"
        userList.contains(user1)
        userList.contains(user2)
    }

      private createFirstUserGroup(){
        def user1= new User(username:"User1",enabled:true,
        profile:new Profile(name:"User1", email:"user1@me.com")).save(validate:false)
        def user2= new User(username:"User2",enabled:true,
        profile:new Profile(name:"User2", email:"user2@me.com")).save(validate:false)
        def user3= new User(username:"User3", enabled:true,
        profile:new Profile(name:"User3", email:"user3@me.com")).save(validate:false)
        ArrayList<User> userList = [user1, user2, user3]
        def firstNotificationGroup = new GroupNotification(notificationId: "763gytdg327fgfg67fv5f", users: userList, name:"ModulusUnoGroup")
        firstNotificationGroup.save(validate:false)
        firstNotificationGroup

    }

}
