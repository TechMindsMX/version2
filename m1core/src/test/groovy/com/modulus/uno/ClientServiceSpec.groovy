package com.modulus.uno

import org.springframework.context.i18n.LocaleContextHolder as LCH

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Ignore

import com.modulus.uno.BusinessException
import com.modulus.uno.stp.StpClabeService

@TestFor(ClientService)
@Mock([Address,Company,BusinessEntity,ClientLink,ModulusUnoAccount,User])
class ClientServiceSpec extends Specification {

  def stpClabeService = Mock(StpClabeService)

  def setup(){
    service.stpClabeService = stpClabeService
    messageSource.addMessage('exception.client.already.exist', LCH.getLocale(), 'A client with same RFC already exist for this company')
  }

  void "Add client to a given company"(){
  given:"An existing business entity and a given company"
    def be = new BusinessEntity(rfc:'XXX010101XXX',website:'http://iecce.com.mx',type:BusinessEntityType.MORAL).save(validate:false)
    def company = new Company(rfc:'ROS861224NHA', bussinessName:'businessName', employeeNumbers:10, grossAnnualBilling:100000).save()
  when:"Assign the client behvaior"
    service.addClientToCompany(be, company)
    def isClient = service.isClientOfThisCompany(be, company)
    def clientLink = ClientLink.findByTypeAndClientRef("BusinessEntity", 'XXX010101XXX')
  then:"I should demonstrate the BE is a client of the company"
    clientLink
    clientLink.type == "BusinessEntity"
    clientLink.clientRef == be.rfc
    clientLink.company == company
    isClient
  }

  void "should not save a be since already exist"(){
  given:"An existing business entity and a given company"
    def be1 = new BusinessEntity(rfc:'XXX010101XXX',website:'http://iecce.com.mx',type:BusinessEntityType.FISICA).save(validate:false)
    def company = new Company(rfc:'ROS861224NHA', bussinessName:'businessName', employeeNumbers:10, grossAnnualBilling:100000).save()
    service.addClientToCompany(be1, company)
    assert service.isClientOfThisCompany(be1, company)
  and:"Another business entity"
    def be2 = new BusinessEntity(rfc:'XXX010101XXX',website:'http://iecce.com.mx',type:BusinessEntityType.FISICA).save(validate:false)
  when:"We save another business entity with same RFC"
    service.addClientToCompany(be2, company)
  then:"We expect Exception"
    thrown BusinessException
  }


  void "should get clients by company"(){
    given:"A business entity and given a company"
      def be = new BusinessEntity(rfc:'XXX010101XXX',website:'http://iecce.com.mx',type:BusinessEntityType.FISICA).save(validate:false)
      def company = new Company(rfc:'ROS861224NHA', bussinessName:'businessName', employeeNumbers:10, grossAnnualBilling:100000).save()
    and:"We assign client to company"
      service.addClientToCompany(be, company)
    when:"We get clients"
      def result = service.getClientsFromCompany(company)
    then:"We expect our client"
      result.size() == 1
      result.contains(be)
   }

  void "Should generate a subaccount stp to client"() {
    given: "A company"
      Company company = new Company(accounts:[], legalRepresentatives:[]).save(validate:false)
      ModulusUnoAccount modulusUnoAccount = new ModulusUnoAccount(integraUuid:"uuidCompany", stpClabe:"646180111900010007").save(validate:false)
      company.accounts << modulusUnoAccount
      company.save(validate:false)
    and:"A client link"
      ClientLink clientLink = new ClientLink(type:'CLIENTE',clientRef:"cliente",company:company).save(validate:false)
      clientLink.save(validate:false)
    and:
      stpClabeService.generateSTPSubAccount(_, _) >> "TheSubAccountStp"
    when: "Generate the subaccount"
      def result = service.generateSubAccountStp(clientLink)
    then:
      result.stpClabe == "TheSubAccountStp"
  }

  void "Should delete a client link"() {
    given: "A company"
      Company company = new Company().save(validate:false)
    and:"The RFC"
      ClientLink clientLink = new ClientLink(type:'CLIENTE',clientRef:"TheRFC",company:company).save(validate:false)
      clientLink.save(validate:false)
    and:
      String rfc = "TheRFC"
    when: "Generate the subaccount"
      def result = service.deleteClientLinkForRfcAndCompany(rfc, company)
    then:
      !ClientLink.findByClientRefAndCompany(rfc, company) 
  }


}
