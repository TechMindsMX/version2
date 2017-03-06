package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import com.amazonaws.services.s3.model.*
import grails.test.mixin.Mock
import org.springframework.mock.web.MockMultipartFile
import com.amazonaws.services.s3.model.*
import grails.plugin.awssdk.AmazonWebService
import spock.lang.Ignore

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(DocumentService)
@Mock([S3Asset,LegalRepresentativesAssets,Company,DepositOrder,User])
class DocumentServiceSpec extends Specification {

  def s3AssetService = Mock(S3AssetService)

  def setup() {
    service.s3AssetService = s3AssetService
  }


  void "search asset of legal representative by company"() {
    given:
      def legalAsset1 = new LegalRepresentativesAssets(asset:1,company:12).save()
      def legalAsset2 = new LegalRepresentativesAssets(asset:2,company:12).save()
    and:
      def aset1 = new S3Asset(status:true).save(validate:false)
      def aset2 = new S3Asset(status:true).save(validate:false)
      def aset3 = new S3Asset(status:false).save(validate:false)
    when:
      def listOfAssets = service.getDocumentsByCompanyForLegalRepresentative([aset1,aset2],12)
    then:
      listOfAssets.size() == 2
      listOfAssets.first().id == 1
  }

  void "delete asset from company"() {
    given:
      def company = new Company()
      company.rfc = "ROD861224NHA"
      company.bussinessName = "apple"
      company.employeeNumbers = 12
      company.grossAnnualBilling = 1200.00
      company.save()
    and:
      def asset = new S3Asset(originalName:"tumana").save(validate:false)
      company.addToDocuments(asset)
      company.save()
    when:
      service.deleteAssetOfRelationship(company,asset)
    then:
      company.documents.isEmpty() == true
  }

  def documents = []

  void "create a document file order"() {
    given:
      def asset = new S3Asset()
    and:
      DepositOrder.metaClass.addToDocuments = {
        s3Asset ->
          documents.add(asset)
      }
    and:
      def file = new MockMultipartFile("file.txt", "", "plain/text", [] as byte[])
      def type = "prueba"
    and:
      User user = new User().save(validate:false)
    and:
      Company company = new Company().save(validate:false)
    and:
      def depositOrder = Mock(DepositOrder)
    when:
      service.uploadDocumentForOrder(file,type,depositOrder)
    then:
      1 * s3AssetService.createTempFilesOfMultipartsFiles(file,type)
      documents.get(0) instanceof S3Asset
      1 * depositOrder.save()
  }

}
