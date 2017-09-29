package com.modulus.uno.purchaseOrder

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import org.springframework.core.io.Resource
import org.springframework.core.io.ClassPathResource
import org.springframework.mock.web.MockMultipartFile

import com.modulus.uno.PurchaseOrder
import com.modulus.uno.PurchaseOrderItem
import com.modulus.uno.Company
import com.modulus.uno.utils.TempFileService
import com.modulus.uno.BusinessException
import com.modulus.uno.CommissionException

@TestFor(PurchaseOrderDocumentsService)
@Mock([PurchaseOrder, PurchaseOrderItem, Company])
class PurchaseOrderDocumentsServiceSpec extends Specification {

  File xmlWrong, xmlOkOneConcept, xmlOkMultipleConcepts
  Resource xmlWrongResource = new ClassPathResource("xmlWrong.xml")
  Resource xmlOkOneConceptResource = new ClassPathResource("xmlOkOneConcept.xml")
  Resource xmlOkMultipleConceptsResource = new ClassPathResource("xmlOkMultipleConcepts.xml")
  TempFileService tempFileService = Mock(TempFileService)
  PurchaseOrderItemService purchaseOrderItemService = Mock(PurchaseOrderItemService)

  def setup() {
    xmlWrong = xmlWrongResource.getFile()
    xmlOkOneConcept = xmlOkOneConceptResource.getFile()
    xmlOkMultipleConcepts = xmlOkMultipleConceptsResource.getFile()
    service.tempFileService = tempFileService
    service.purchaseOrderItemService = purchaseOrderItemService
  }

  void "Thrown a exception when validate RFC from wrong XML"() {
    given:"The rfc company"
      String rfcCompany = "AAA010101AAA"
    and:"The document"
      def document = new MockMultipartFile("xmlWrong.xml", "", "plain/text", xmlWrong.getBytes())
    and:
      tempFileService.getTempFileFromMultipart(_, _) >> xmlWrong
    when:
      service.validateRfc(document, rfcCompany)
    then:
      thrown BusinessException
  }

  void "Should return true when validate RFC from XML"() {
    given:"The rfc company"
      String rfcCompany = "AAA010101AAA"
    and:"The document"
      def document = new MockMultipartFile("xmlOkOneConcept.xml", "", "plain/text", xmlOkOneConcept.getBytes())
    and:
      tempFileService.getTempFileFromMultipart(_, _) >> xmlOkOneConcept
    when:
      def result = service.validateRfc(document, rfcCompany)
    then:
      result
  }

  void "Should create item from xml file with one concept into purchase order without current items"() {
    given:"The purchase order with items"
      PurchaseOrder order = new PurchaseOrder().save(validate:false)
    and:"The document"
      def document = new MockMultipartFile("xmlOkOneConcept.xml", "xmlOkOneConcept.xml", "plain/text", xmlOkOneConcept.getBytes())
    and:
      tempFileService.getTempFileFromMultipart(_, _) >> xmlOkOneConcept
    when:
      def result = service.loadItemsToOrderFromDocumentXml(order, document)
    then:
      result.items.size() == 1
      result.items.first().name == "ALGO CINCO"
  }

  void "Should create item from xml file with two concept into purchase order without current items"() {
    given:"The purchase order with items"
      PurchaseOrder order = new PurchaseOrder().save(validate:false)
    and:"The document"
      def document = new MockMultipartFile("xmlOkMultipleConcepts.xml", "xmlOkMultipleConcepts.xml", "plain/text", xmlOkMultipleConcepts.getBytes())
    and:
      tempFileService.getTempFileFromMultipart(_, _) >> xmlOkMultipleConcepts
    when:
      def result = service.loadItemsToOrderFromDocumentXml(order, document)
    then:
      result.items.size() == 2
      result.items.first().name == "PRODUCTO CINCO"
      result.items[1].name == "PRODUCTO DOS ST"
  }

}
