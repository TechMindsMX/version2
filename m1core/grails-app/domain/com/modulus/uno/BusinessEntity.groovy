package com.modulus.uno

import grails.converters.JSON

class BusinessEntity implements ClientBusinessEntity, ProviderBusinessEntity, EmployeeBusinessEntity {

  def businessEntityService

  String rfc
  String website
  String uuid = UUID.randomUUID().toString().replace('-','')[0..15]
  String email

  BusinessEntityType type
  BusinessEntityStatus status = BusinessEntityStatus.TO_AUTHORIZE

  String artemisaId

  static hasMany = [
  addresses:Address,
  names:ComposeName,
  banksAccounts:BankAccount
  ]

  static fetchMode = [names: 'eager']

  static constraints = {
    artemisaId nullable:true
    website nullable:true,blank:false,size:5..50,url:true
    rfc(blank:false,size:10..50, validator: { val, obj ->
      if(obj.type == BusinessEntityType.FISICA && !(val ==~ /^[A-Z]{4}([0-9]{2})(1[0-2]|0[1-9])([0-3][0-9])([A-Z0-9]{3})$/) ) {
        return false
      } else if (obj.type == BusinessEntityType.MORAL && !(val ==~ /^[A-Z]{3}([0-9]{2})(1[0-2]|0[1-9])([0-3][0-9])([A-Z0-9]{3})$/) ) {
        return false
      }
    })
    status nullable:false
    email nullable:true, blank:true, email:true,size:6..200
  }

  String toString(){
    String name
    String lastName
    String motherLastName
    String razonSocial
    String fullName

    if(type == BusinessEntityType.FISICA){
      names.each {
        name = (it.type == NameType.NOMBRE) ? it.value : name
      }
      names.each {
        lastName  = (it.type == NameType.APELLIDO_PATERNO) ? it.value : lastName
      }
      names.each {
        motherLastName = (it.type == NameType.APELLIDO_MATERNO) ? it.value : motherLastName
      }
      fullName = "${name} ${lastName} ${motherLastName}"
    }
    if(type == BusinessEntityType.MORAL){
      names.each {
        razonSocial = (it.type == NameType.RAZON_SOCIAL) ? it.value : razonSocial
      }
      fullName = razonSocial
    }
    fullName
  }

  static marshaller = {
    JSON.registerObjectMarshaller(BusinessEntity, 1) { m ->
      return [
      id: m.id,
      rfc: m.rfc,
      website: m.website,
      uuid: m.uuid,
      artemisaId: m.artemisaId,
      type: m.type,
      addresses: m.addresses,
      //names: m.names,
      banksAccounts: m.banksAccounts
      ]
    }
  }

  @Override
  String getCurp() {
    Company company = Company.list().find { it.businessEntities.id.contains(this.id) }
    EmployeeLink employee = EmployeeLink.findByEmployeeRefAndCompany(this.rfc, company)
    employee?.curp ?: ""
  }

  @Override
  String getNumber() {
    Company company = Company.list().find { it.businessEntities.id.contains(this.id) }
    EmployeeLink employee = EmployeeLink.findByEmployeeRefAndCompany(this.rfc, company)
    employee?.number ?: ""
  }

  def getBusinessEntityType(){
    businessEntityService.getClientProviderType(this.rfc)
  }

}
