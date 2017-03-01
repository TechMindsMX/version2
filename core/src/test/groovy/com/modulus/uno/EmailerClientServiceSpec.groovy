package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock

@TestFor(EmailerClientService)
class EmailerClientServiceSpec extends Specification {

    def setup() {
    }


    def "Create a list with id, subject of every emailer on emailer app"() {
      given:"A data of all emailers"
      def emailerStorage = [
      ["_id":"123abc", "dateCreated":2223232, "version":2, "lastUpdate":234343, "subject":"uno", "content":"<b>contentido</b>"],
      ["_id":"123abc", "dateCreated":2223232, "version":2, "lastUpdate":234343, "subject":"uno", "content":"<b>contentido</b>"],
      ["_id":"123abc", "dateCreated":2223232, "version":2, "lastUpdate":234343, "subject":"uno", "content":"<b>contentido</b>"]
      ]
      when:"We want to list the emailers by id and subject"
      def emailerList = service.getEmailerList(emailerStorage)
      then:"We should get a list with id and subject"
      emailerList == [["id":"123abc", "subject":"uno"], ["id":"123abc", "subject":"uno"], ["id":"123abc", "subject":"uno"]]
    }

    def "Get a subject of emailer"(){
    given:"An id of emailer"
    def idEmailer = "123abc"
    and: "A list of emailers"
    def emailerList = [["id":"123abc", "subject":"Subject Found"], ["id":"223abc", "subject":"uno"], ["id":"323abc", "subject":"dos"]]
    when:"We want to know the subject"
    def subjectEmailer = service.getSubject(idEmailer, emailerList)
    then:"We should get"
    subjectEmailer == "Subject Found"
    }
}
