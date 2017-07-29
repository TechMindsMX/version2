package com.modulus.uno.purchaseOrder

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import org.springframework.core.io.Resource
import org.springframework.core.io.ClassPathResource
import org.springframework.mock.web.MockMultipartFile

import com.modulus.uno.PurchaseOrder
import com.modulus.uno.Company
import com.modulus.uno.utils.TempFileService
import com.modulus.uno.BusinessException
import com.modulus.uno.CommissionException

@TestFor(PurchaseOrderDocumentsService)
@Mock([PurchaseOrder, Company])
class PurchaseOrderDocumentsServiceSpec extends Specification {

  File xmlWrong, xmlOkOneConcept, xmlOkMultipleConcepts
  Resource xmlWrongResource = new ClassPathResource("xmlWrong.xml")
  Resource xmlOkOneConceptResource = new ClassPathResource("xmlOkOneConcept.xml")
  Resource xmlOkMultipleConceptsResource = new ClassPathResource("xmlOkMultipleConcepts.xml")
  TempFileService tempFileService = Mock(TempFileService)

  def setup() {
    xmlWrong = xmlWrongResource.getFile()
    xmlOkOneConcept = xmlOkOneConceptResource.getFile()
    xmlOkMultipleConcepts = xmlOkMultipleConceptsResource.getFile()
    service.tempFileService = tempFileService
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
}
