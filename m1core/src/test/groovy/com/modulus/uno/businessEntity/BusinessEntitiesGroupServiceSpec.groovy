package com.modulus.uno.businessEntity

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

import com.modulus.uno.BusinessEntity
import com.modulus.uno.Company
import com.modulus.uno.User
import com.modulus.uno.ComposeName
import com.modulus.uno.BusinessEntityService
import com.modulus.uno.NameType

@TestFor(BusinessEntitiesGroupService)
@Mock([BusinessEntitiesGroup, BusinessEntity, Company, User, ComposeName])
class BusinessEntitiesGroupServiceSpec extends Specification {

  BusinessEntityService businessEntityService = Mock(BusinessEntityService)

  def setup() {
    service.businessEntityService = businessEntityService
  }

  void "Should get the business entities availables for a group of CLIENTS type without current business entities"() {
    given:"The group"
      Company company = new Company().save(validate:false)
      BusinessEntity businessEntity1 = new BusinessEntity(rfc:"RFC1").save(validate:false)
      BusinessEntity businessEntity2 = new BusinessEntity(rfc:"RFC2").save(validate:false)
      BusinessEntity businessEntity3 = new BusinessEntity(rfc:"RFC3").save(validate:false)
      company.addToBusinessEntities(businessEntity1)
      company.addToBusinessEntities(businessEntity2)
      company.addToBusinessEntities(businessEntity3)
      company.save(validate:false)
      BusinessEntitiesGroup group = new BusinessEntitiesGroup(company:company, type:BusinessEntitiesGroupType.CLIENTS, businessEntities:[]).save(validate:false)
    and:
      businessEntityService.findBusinessEntityByKeyword(_, _, _) >> [businessEntity1, businessEntity2, businessEntity3]
    when:
      def result = service.getBusinessEntitiesAvailablesForGroup(group)
    then:
      result.size() == 3
  }

  void "Should get the business entities availables for a group of CLIENTS type with current business entities"() {
    given:"The group"
      Company company = new Company().save(validate:false)
      BusinessEntity businessEntity1 = new BusinessEntity(rfc:"RFC1").save(validate:false)
      BusinessEntity businessEntity2 = new BusinessEntity(rfc:"RFC2").save(validate:false)
      BusinessEntity businessEntity3 = new BusinessEntity(rfc:"RFC3").save(validate:false)
      company.addToBusinessEntities(businessEntity1)
      company.addToBusinessEntities(businessEntity2)
      company.addToBusinessEntities(businessEntity3)
      company.save(validate:false)
      BusinessEntitiesGroup group = new BusinessEntitiesGroup(company:company, type:BusinessEntitiesGroupType.CLIENTS).save(validate:false)
      group.addToBusinessEntities(businessEntity1)
      group.save(validate:false)
    and:
      businessEntityService.findBusinessEntityByKeyword(_, _, _) >> [businessEntity1, businessEntity2, businessEntity3]
    when:
      def result = service.getBusinessEntitiesAvailablesForGroup(group)
    then:
      result.size() == 2
  }

  void "Should get all clients from users clients groups in company"() {
    given:"The business entities"
      ComposeName name1 = new ComposeName(value:"CLIENT 1", type:NameType.RAZON_SOCIAL).save(validate:false) 
      ComposeName name2 = new ComposeName(value:"CLIENT 2", type:NameType.RAZON_SOCIAL).save(validate:false) 
      ComposeName name3 = new ComposeName(value:"CLIENT 3", type:NameType.RAZON_SOCIAL).save(validate:false) 
      ComposeName name4 = new ComposeName(value:"CLIENT 4", type:NameType.RAZON_SOCIAL).save(validate:false) 
      ComposeName name5 = new ComposeName(value:"CLIENT 5", type:NameType.RAZON_SOCIAL).save(validate:false) 
      ComposeName name6 = new ComposeName(value:"CLIENT 6", type:NameType.RAZON_SOCIAL).save(validate:false) 
      BusinessEntity businessEntity1 = new BusinessEntity(rfc:"RFC1").save(validate:false)
      businessEntity1.addToNames(name1)
      businessEntity1.save(validate:false)
      BusinessEntity businessEntity2 = new BusinessEntity(rfc:"RFC2").save(validate:false)
      businessEntity2.addToNames(name2)
      businessEntity2.save(validate:false)
      BusinessEntity businessEntity3 = new BusinessEntity(rfc:"RFC3").save(validate:false)
      businessEntity3.addToNames(name3)
      businessEntity3.save(validate:false)
      BusinessEntity businessEntity4 = new BusinessEntity(rfc:"RFC4").save(validate:false)
      businessEntity4.addToNames(name4)
      businessEntity4.save(validate:false)
      BusinessEntity businessEntity5 = new BusinessEntity(rfc:"RFC5").save(validate:false)
      businessEntity5.addToNames(name5)
      businessEntity5.save(validate:false)
      BusinessEntity businessEntity6 = new BusinessEntity(rfc:"RFC6").save(validate:false)
      businessEntity6.addToNames(name6)
      businessEntity6.save(validate:false)
    and: "The company"
      Company company = new Company(rfc:"Company").save(validate:false)
    and:"The groups"
      BusinessEntitiesGroup group1 = new BusinessEntitiesGroup(description:"G1", type:BusinessEntitiesGroupType.CLIENTS, company:company).save(validate:false)
      group1.addToBusinessEntities(businessEntity1)
      group1.addToBusinessEntities(businessEntity2)
      group1.save(validate:false)
      BusinessEntitiesGroup group2 = new BusinessEntitiesGroup(description:"G2", type:BusinessEntitiesGroupType.PROVIDERS, company:company).save(validate:false)
      group2.addToBusinessEntities(businessEntity3)
      group2.addToBusinessEntities(businessEntity4)
      group2.save(validate:false)
      BusinessEntitiesGroup group3 = new BusinessEntitiesGroup(description:"G3", type:BusinessEntitiesGroupType.CLIENTS, company:company).save(validate:false)
      group3.addToBusinessEntities(businessEntity5)
      group3.addToBusinessEntities(businessEntity6)
      group3.save(validate:false)
    and:"The user"
      User user = new User(username:"user").save(validate:false)
      user.addToBusinessEntitiesGroups(group1)
      user.addToBusinessEntitiesGroups(group2)
      user.addToBusinessEntitiesGroups(group3)
      user.save(validate:false)
    when:
      def result = service.findBusinessEntitiesByKeyword(user, company, "RFC3")
    then:
      result.size() == 6
  }

}
