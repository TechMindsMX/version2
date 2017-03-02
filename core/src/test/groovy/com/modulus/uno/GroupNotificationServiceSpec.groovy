package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock

@TestFor(GroupNotificationService)
@Mock([GroupNotification, User])
class GroupNotificationServiceSpec extends Specification {

    def "Delete a notification group"(){
      given:"A group notification"
        GroupNotification firstGroup = createUserGroup()
      when:"we want to delete a group notification"
        service.deleteGroup(firstGroup.id)
      then:"We shouldn't have any group notification"
        !GroupNotification.findById(firstGroup.id)
    }

    def "Get a list of notification groups"(){
      given:"Many notificationGroups"
        GroupNotification firstGroup = createUserGroup()
        GroupNotification secondGroup = createUserGroup()
        GroupNotification thirdGroup = createUserGroup()
      when:"I want to know all the notification groups"
        def groupsList = service.getGroupsList()
      then:"We should get a list"
        groupsList.contains(firstGroup)
        groupsList.contains(secondGroup)
        groupsList.contains(thirdGroup)
    }

    def "Find and get a specific groupNotification"(){
        given:"A groupNotifications"
        GroupNotification firstGroup = createUserGroup()
        GroupNotification secondGroup = createUserGroup()
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
    }

      private createUserGroup(){
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
