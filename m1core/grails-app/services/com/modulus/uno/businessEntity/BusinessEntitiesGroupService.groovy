package com.modulus.uno.businessEntity

import grails.transaction.Transactional
import com.modulus.uno.BusinessEntity
import com.modulus.uno.BusinessEntityService

class BusinessEntitiesGroupService {

  BusinessEntityService businessEntityService

  List<BusinessEntity> getBusinessEntitiesAvailablesForGroup(BusinessEntitiesGroup group) {
    String type = "CLIENT"
    if (group.type == BusinessEntitiesGroupType.PROVIDERS) { type = "PROVIDER" }
    if (group.type == BusinessEntitiesGroupType.EMPLOYEES) { type = "EMPLOYEE" }
    List<BusinessEntity> allBusinessEntityInCompany = businessEntityService.findBusinessEntityByKeyword("", type, group.company)
    List<BusinessEntity> businessEntitiesAvailables = allBusinessEntityInCompany - (group.businessEntities ?: [])
    businessEntitiesAvailables
  }

  @Transactional
  BusinessEntitiesGroup addBusinessEntityToGroup(BusinessEntitiesGroup businessEntitiesGroup, String businessEntityId) {
    BusinessEntity businessEntity = BusinessEntity.get(businessEntityId)
    businessEntitiesGroup.addToBusinessEntities(businessEntity)
    businessEntitiesGroup.save()
    businessEntitiesGroup
  }

  @Transactional
  BusinessEntitiesGroup deleteBusinessEntityFromGroup(BusinessEntitiesGroup businessEntitiesGroup, String businessEntityId) {
    BusinessEntity businessEntity = BusinessEntity.get(businessEntityId)
    businessEntitiesGroup.removeFromBusinessEntities(businessEntity)
    businessEntitiesGroup.save()
    businessEntitiesGroup
  }

}
