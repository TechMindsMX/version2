package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import java.lang.Void as Should
import grails.test.mixin.Mock

@TestFor(AddressService)
@Mock([Company, Address, BusinessEntity])
class AddressServiceSpec extends Specification {

  BusinessEntityService businessEntityService = Mock(BusinessEntityService)
  CompanyService companyService = Mock(CompanyService)

  def setup() {
    service.businessEntityService = businessEntityService
    service.companyService = companyService
  }

  Should "get the address types for the organization without addresses"(){
    given:"a company"
      def company = createCompany()
    when:
      def addressTypes = service.findAddressTypeForOrganization(company.id)
    then:
      addressTypes.size() == 3
  }

  Should "get the address types for the organization with a legal Address"(){
    given:"a company"
      def company = createCompany()
    and: "the legal address"
      def address = new Address(street:"Bellas Artes",
                                streetNumber:205,
                                addressType:AddressType.FISCAL).save(validate:false)
      company.addToAddresses(address)
      company.save(validate:false)
    when:
      def addressTypes = service.findAddressTypeForOrganization(company.id)
    then:
      addressTypes.size() == 2
  }

  Should "Should get the address types map"(){
    given:"a company"
      def company = new Company(bussinessName:"MakingDevs").save(validate:false)
    when:
      def customAddressTypes = service.getAddresTypesForOrganization(company.id)
    then:
      customAddressTypes.first().key == "FISCAL"
  }

  Should "Should create an address for a business entity"(){
    given:"an address"
      def address = new Address(street:"Bellas Artes",
                                streetNumber:205,
                                addressType:AddressType.FISCAL).save(validate:false)
    when:
      def result = service.createAddressForAObject(address, 1, 0)
    then:
      1 * businessEntityService.createAddressForBusinessEntity(_, _)
  }

  Should "Should create an address for a company"(){
    given:"an address"
      def address = new Address(street:"Bellas Artes",
                                streetNumber:205,
                                addressType:AddressType.FISCAL).save(validate:false)
    when:
      def result = service.createAddressForAObject(address, 0, 1)
    then:
      1 * companyService.createAddressForCompany(_, _)
  }

  void "Should return all address type when businessEntity hasn't addresses"() {
    given:"A business entity"
      def businessEntity = new BusinessEntity(rfc:"AAA010101AAA")
      businessEntity.save(validate:false)
    when:
      def result = service.getAddressTypesForBusinessEntity(businessEntity)
    then:
      result.size() == AddressType.values().size()
  }

  void "Should return address types without Fiscal type when businessEntity has it already"() {
    given:"A business entity"
      def businessEntity = new BusinessEntity(rfc:"AAA010101AAA")
      def address = new Address(street:"Bellas Artes",
                                streetNumber:205,
                                addressType:AddressType.FISCAL).save(validate:false)
      businessEntity.addToAddresses(address)
      businessEntity.save(validate:false)
    when:
      def result = service.getAddressTypesForBusinessEntity(businessEntity)
    then:
      result.size() == 2
      result.find {it.key == "FISCAL"} == null
  }

  void "Should return address types without Fiscal type when edit an address and company has it already"() {
    given:"A company with address type Fiscal"
      def company = createCompany()
      def address = new Address(street:"Bellas Artes",
                                streetNumber:205,
                                addressType:AddressType.FISCAL).save(validate:false)
      def addressToEdit = new Address(street:"Developers",
                                streetNumber:205,
                                addressType:AddressType.SOCIAL).save(validate:false)

      company.addToAddresses(address)
      company.addToAddresses(addressToEdit)
      company.save(validate:false)
    when:
      def result = service.getAddressTypesForEditCompanyAddress(addressToEdit, company.id.toString())
    then:
      result.size() == 2
      result.find {it.key == "FISCAL"} == null
  }

  void "Should return address types with Fiscal type when edit an address of type Fiscal and company has it already"() {
    given:"A company with address type Fiscal"
      def company = createCompany()
      def address = new Address(street:"Bellas Artes",
                                streetNumber:205,
                                addressType:AddressType.FISCAL).save(validate:false)
      def addressTwo = new Address(street:"Developers",
                                streetNumber:205,
                                addressType:AddressType.SOCIAL).save(validate:false)

      company.addToAddresses(address)
      company.addToAddresses(addressTwo)
      company.save(validate:false)
    when:
      def result = service.getAddressTypesForEditCompanyAddress(address, company.id.toString())
    then:
      result.size() == 3
      result.find {it.key == "FISCAL"}
  }

  void "Should return address types without Fiscal type when edit an address and business entity has it already"() {
    given:"A businessEntity with address type Fiscal"
      def businessEntity = new BusinessEntity(rfc:"AAA010101AAA")
      def address = new Address(street:"Bellas Artes",
                                streetNumber:205,
                                addressType:AddressType.FISCAL).save(validate:false)
      def addressToEdit = new Address(street:"Developers",
                                streetNumber:205,
                                addressType:AddressType.SOCIAL).save(validate:false)

      businessEntity.addToAddresses(address)
      businessEntity.addToAddresses(addressToEdit)
      businessEntity.save(validate:false)
    when:
      def result = service.getAddressTypesForEditBusinessEntityAddress(addressToEdit, businessEntity)
    then:
      result.size() == 2
      result.find {it.key == "FISCAL"} == null
  }

  void "Should return address types with Fiscal type when edit an address of type Fiscal and business entity has it already"() {
    given:"A businessEntity with address type Fiscal"
      def businessEntity = new BusinessEntity(rfc:"AAA010101AAA")
      def address = new Address(street:"Bellas Artes",
                                streetNumber:205,
                                addressType:AddressType.FISCAL).save(validate:false)
      def addressTwo = new Address(street:"Developers",
                                streetNumber:205,
                                addressType:AddressType.SOCIAL).save(validate:false)

      businessEntity.addToAddresses(address)
      businessEntity.addToAddresses(addressTwo)
      businessEntity.save(validate:false)
    when:
      def result = service.getAddressTypesForEditBusinessEntityAddress(address, businessEntity)
    then:
      result.size() == 3
      result.find {it.key == "FISCAL"}
  }

  private def createCompany(){
    new Company(rfc:"ROS861224NHA",
                bussinessName:"MakingDevs",
                webSite:"",
                employeeNumbers:40,
                grossAnnualBilling:2000,
                status:CompanyStatus.CREATED).save()
  }

}
