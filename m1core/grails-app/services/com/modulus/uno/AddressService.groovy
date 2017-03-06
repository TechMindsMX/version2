package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class AddressService {

  def businessEntityService
  def companyService

  def createAddressForAObject(Address address, Long businessEntityId = 0, Long companyId = 0) {
    def domain
    if (businessEntityId){
      domain = businessEntityService.createAddressForBusinessEntity(address, businessEntityId)

      return domain
    }
    if (companyId) {
      domain = companyService.createAddressForCompany(address, companyId)
    }
    domain
  }


  def getAddresTypesForOrganization(Long companyId){
    def customAddressTypes = []
    if(!companyId){
      customAddressTypes << [key:AddressType.FISCAL.name(),value:AddressType.FISCAL.name().toLowerCase().capitalize()]
      return customAddressTypes
    }
    def addressTypes = findAddressTypeForOrganization(companyId)

    addressTypes.each{ addressType ->
      customAddressTypes << [key:addressType.name(),value:addressType.name().toLowerCase().capitalize()]
    }

    customAddressTypes
  }

  def findAddressTypeForOrganization(Long companyId){
    def addresses = Company.get(companyId).addresses
    if(addresses.find{ it.addressType == AddressType.FISCAL })
      AddressType.values().findAll{ it.name() != AddressType.FISCAL.name() }
    else
      AddressType.values()
  }

}
