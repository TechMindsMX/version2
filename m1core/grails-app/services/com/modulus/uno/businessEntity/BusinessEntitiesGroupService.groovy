package com.modulus.uno.businessEntity

import grails.transaction.Transactional
import com.modulus.uno.BusinessEntity
import com.modulus.uno.BusinessEntityService
import com.modulus.uno.Company
import com.modulus.uno.User

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

  List checkGroupsForCompanies(List<Company> companies) {
    companies.collect { company ->
      def groups = BusinessEntitiesGroup.findAllByCompany(company)
      if (groups) {
        company.metaClass.hasGroups = true
      } else {
        company.metaClass.hasGroups = false
      }
      return company
    }
  }

  List<BusinessEntitiesGroup> getAvailableGroupsForCompanyAndUser(Company company, User user) {
    List<BusinessEntitiesGroup> allCompanyGroups = BusinessEntitiesGroup.findAllByCompany(company)
    List<BusinessEntitiesGroup> currentGroupsForUserInCompany = user.businessEntitiesGroups.findAll { group -> group.company == company }.toList()
    allCompanyGroups - currentGroupsForUserInCompany
  }

  @Transactional
  User addBusinessEntitiesGroupToUser(User user, BusinessEntitiesGroup group) {
    user.addToBusinessEntitiesGroups(group)
    user.save()
    user
  }

  @Transactional
  User deleteBusinessEntitiesGroupFromUser(User user, BusinessEntitiesGroup group) {
    user.removeFromBusinessEntitiesGroups(group)
    user.save()
    user
  }

  List<BusinessEntity> findBusinessEntitiesByKeyword(User user, Company company, String dataQuery) {
    List<BusinessEntity> allClientsForUser = []
    List<BusinessEntitiesGroup> allClientsGroupsForUserInCompany = user.businessEntitiesGroups.findAll { group -> group.company == company && group.type == BusinessEntitiesGroupType.CLIENTS }.toList()
    allClientsGroupsForUserInCompany.each { group ->
      println "Business Entities group: ${group.id} - ${group.businessEntities}"
      allClientsForUser.addAll(group.businessEntities.toList())
    }
    println "All clients for user: ${allClientsForUser}"
    List<BusinessEntity> clients = dataQuery ? allClientsForUser.findAll { client -> client.rfc.contains(dataQuery) || client.toString().contains(dataQuery) } : allClientsForUser
    clients
  }

}
