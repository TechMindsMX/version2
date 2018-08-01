package com.modulus.uno.businessEntity

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

import com.modulus.uno.BusinessEntity
import com.modulus.uno.Company
import com.modulus.uno.BusinessEntityService

@TestFor(BusinessEntitiesGroupService)
@Mock([BusinessEntitiesGroup, BusinessEntity, Company])
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

}
