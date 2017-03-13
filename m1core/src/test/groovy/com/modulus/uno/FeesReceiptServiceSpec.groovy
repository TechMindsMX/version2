package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(FeesReceiptService)
@Mock([FeesReceipt,User,Authorization])
class FeesReceiptServiceSpec extends Specification {

  DocumentService documentService = Mock(DocumentService)
  EmailSenderService emailSenderService = Mock(EmailSenderService)

  def setup(){
    service.documentService = documentService
    service.emailSenderService = emailSenderService
  }

  void "should add a document to the fees receipt"() {
    given:"An feesReceipt"
      FeesReceipt feesReceipt = new FeesReceipt(amount:100)
    and:"A document"
      Object document = new Object()
    when:"We add document"
      service.addDocumentToFeesReceipt(document, feesReceipt, 'feesReceipt')
    then:"We expect to call to document service"
    1 * documentService.uploadDocumentForOrder(document, 'feesReceipt', feesReceipt)
  }

  void "should add authorization to fees receipt"() {
    given:
      FeesReceipt feesReceipt = new FeesReceipt(amount:1000)
      feesReceipt.save(validate:false)
    and:
      User user = new User(username:"usuario")
      user.save(validate:false)
    when:
      feesReceipt = service.addAuthorizationToFeesReceipt(feesReceipt, user)
    then:
      feesReceipt.authorizations.size() == 1
  }

  void "should set status AUTORIZADA to fees receipt"() {
    given:
      FeesReceipt feesReceipt = new FeesReceipt(amount:1000)
      feesReceipt.save(validate:false)
    when:
      service.authorizeFeesReceipt(feesReceipt)
    then:
      feesReceipt.status == FeesReceiptStatus.AUTORIZADA
      1 * emailSenderService.notifyFeesReceiptChangeStatus(feesReceipt)
  }

  void "should set status POR_AUTORIZAR to fees receipt"() {
    given:
      FeesReceipt feesReceipt = new FeesReceipt(amount:1000)
      feesReceipt.save(validate:false)
    when:
      service.sendToAuthorize(feesReceipt)
    then:
      feesReceipt.status == FeesReceiptStatus.POR_AUTORIZAR
      1 * emailSenderService.notifyFeesReceiptChangeStatus(feesReceipt)
  }

}
