package com.modulus.uno

import org.springframework.context.i18n.LocaleContextHolder as LCH

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(ProviderService)
@Mock([Address,Company,BusinessEntity,ProviderLink])
class ProviderServiceSpec extends Specification {

  def emailSenderService = Mock(EmailSenderService)

  def setup(){
    service.emailSenderService = emailSenderService
    messageSource.addMessage('exception.provider.already.exist', LCH.getLocale(), 'A provider with same RFC already exist for this company')
  }

  void "Add provider to a given company"(){
  given:"An existing business entity and a given company"
    def be = new BusinessEntity(rfc:'XXX010101XXX',website:'http://iecce.com.mx',type:BusinessEntityType.FISICA).save(validate:false)
    def company = new Company(rfc:'ROS861224NHA', bussinessName:'businessName', employeeNumbers:10, grossAnnualBilling:100000).save()
  when:"Assign the provider behvaior"
    service.addProviderToCompany(be, company)
    def isProvider = service.isProviderOfThisCompany(be, company)
    def providerLink = ProviderLink.findByTypeAndProviderRef("BusinessEntity", 'XXX010101XXX')
  then:"I should demonstrate the BE is a provider of the company"
    providerLink
    providerLink.type == "BusinessEntity"
    providerLink.providerRef == be.rfc
    providerLink.company == company
    isProvider
    1 * emailSenderService.sendEmailForNewProvider(company, be)
  }

  void "should not save a be since already exist"(){
  given:"An existing business entity and a given company"
    def be1 = new BusinessEntity(rfc:'XXX010101XXX',website:'http://iecce.com.mx',type:BusinessEntityType.FISICA).save(validate:false)
    def company = new Company(rfc:'ROS861224NHA', bussinessName:'businessName', employeeNumbers:10, grossAnnualBilling:100000).save()
    service.addProviderToCompany(be1, company)
    assert service.isProviderOfThisCompany(be1, company)
  and:"Another business entity"
    def be2 = new BusinessEntity(rfc:'XXX010101XXX',website:'http://iecce.com.mx',type:BusinessEntityType.FISICA).save(validate:false)
  when:"We save another business entity with same RFC"
    service.addProviderToCompany(be2, company)
  then:"We expect Exception"
    thrown BusinessException
  }

  void "should get providers by company"(){
  given:"A business entity and given a company"
    def be = new BusinessEntity(rfc:'XXX010101XXX',website:'http://iecce.com.mx',type:BusinessEntityType.FISICA).save(validate:false)
    def company = new Company(rfc:'ROS861224NHA', bussinessName:'businessName', employeeNumbers:10, grossAnnualBilling:100000).save()
  and:"We assign provider to company"
    service.addProviderToCompany(be, company)
  when:"We get providers"
    def result = service.getProvidersFromCompany(company)
  then:"We expect our provider"
    result.size() == 1
    result.contains(be)
  }


}
