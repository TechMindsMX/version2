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

  def getAddressTypesForBusinessEntity(BusinessEntity businessEntity){
    def customAddressTypes = []
    def addressTypes

    if(businessEntity.addresses.find{ it.addressType == AddressType.FISCAL })
      addressTypes = AddressType.values().findAll{ it.name() != AddressType.FISCAL.name() }
    else
      addressTypes = AddressType.values()

    addressTypes.each{ addressType ->
      customAddressTypes << [key:addressType.name(),value:addressType.name().toLowerCase().capitalize()]
    }

    customAddressTypes
  }

  def getAllAddressTypes(){
    def customAddressTypes = []
    def addressTypes = AddressType.values()

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

  def getAddressTypesForEditCompanyAddress(Address address, String companyId){
    def customAddressTypes = []
    def addressTypes
    Company company = Company.get(companyId)
    if (company.addresses.find {it.addressType == AddressType.FISCAL}) {
      addressTypes = address.addressType == AddressType.FISCAL ? AddressType.values() : AddressType.values().findAll{ it.name() != AddressType.FISCAL.name() }
    } else {
      addressTypes = AddressType.values()
    }

    addressTypes.each{ addressType ->
      customAddressTypes << [key:addressType.name(),value:addressType.name().toLowerCase().capitalize()]
    }

    customAddressTypes
  }

  def getAddressTypesForEditBusinessEntityAddress(Address address, BusinessEntity businessEntity){
    def customAddressTypes = []
    def addressTypes
    if (businessEntity.addresses.find {it.addressType == AddressType.FISCAL}) {
      addressTypes = address.addressType == AddressType.FISCAL ? AddressType.values() : AddressType.values().findAll{ it.name() != AddressType.FISCAL.name() }
    } else {
      addressTypes = AddressType.values()
    }

    addressTypes.each{ addressType ->
      customAddressTypes << [key:addressType.name(),value:addressType.name().toLowerCase().capitalize()]
    }

    customAddressTypes
  }

  def createAddressForBusinessEntityFromRowBusinessEntity(BusinessEntity businessEntity, Map rowBusinessEntity) {
    Address address = new Address(
      street:rowBusinessEntity.CALLE,
      streetNumber:rowBusinessEntity.NUMEXTERIOR,
      suite:rowBusinessEntity.NUMINTERIOR,
      zipCode:rowBusinessEntity.CODIGO_POSTAL,
      colony:rowBusinessEntity.COLONIA,
      country:rowBusinessEntity.PAIS,
      city:rowBusinessEntity.CIUDAD,
      town:rowBusinessEntity."DELEGACION/MUNICIPIO",
      federalEntity:rowBusinessEntity.ENTIDAD_FEDERATIVA?:rowBusinessEntity.CIUDAD,
      addressType:rowBusinessEntity.TIPO_DE_DIRECCION
    )

    address.save()
    log.info "Address ${address.dump()}"
    if (address.id) {
      businessEntity.addToAddresses(address)
      businessEntity.save()
    }
    address
  }

}
