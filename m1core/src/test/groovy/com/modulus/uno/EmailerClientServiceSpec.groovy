package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock

@TestFor(EmailerClientService)
@Mock([GroupNotification])
class EmailerClientServiceSpec extends Specification {

  def emailerDataService = Mock(EmailerDataService)
  def notifyService = Mock(NotifyService)
  def wsliteRequestService = Mock(WsliteRequestService)

  def setup() {
    service.emailerDataService = emailerDataService
    service.notifyService = notifyService
    service.wsliteRequestService = wsliteRequestService
  }

  void "Find all subjects of all emailers"(){
    given:""
    when:"We want to obtain the emailers by id and subject"
      def emailersBySubject = service.findAllSubjects()
    then:"We expect"
      1 * wsliteRequestService.doRequest(_,_) >> [doit:{-> [json: []]}]
      1 * emailerDataService.obtainSubjectList(_)
  }

  void "Find all contents of all emailers"(){
    given:
    when:"We want to obtain the emailers by id and content"
      def emailersByContent = service.findAllContents()
    then:"We expect the call for services"
      1 * wsliteRequestService.doRequest(_,_) >> [doit:{-> [json: []]}]
      1 * emailerDataService.obtainContentList(_)
  }

}
