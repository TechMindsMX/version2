package com.modulus.uno

import grails.converters.JSON
import com.modulus.uno.saleorder.SaleOrder

class Address {
  String street
  String streetNumber
  String suite
  String zipCode
  String colony
  String neighboorhood
  String country
  String city
  String town
  String federalEntity
  AddressType addressType

  static belongsTo = [Company, BusinessEntity, SaleOrder]

  static constraints = {
    street blank:false,nullable:false
    streetNumber blank:false,nullable:false
    suite nullable:true
    zipCode blank:false,nullable:false
    colony blank:true,nullable:true
    neighboorhood blank:true,nullable:true
    country blank:false,nullable:false
    city blank:false,nullable:false
    federalEntity blank:false,nullable:false
    town blank:false,nullable:false
  }

  String toString(){
    "$street #$streetNumber C.P $zipCode $federalEntity"
  }

  String info(){
    "$street  $streetNumber Col. ${neighboorhood ?: colony}  $country  $city  $town C.P $zipCode  $federalEntity Tipo:$addressType"
  }


   static marshaller = {
     JSON.registerObjectMarshaller(Address, 1) { m ->
       return [
       id: m.id,
       street: m.street,
       streetNumber: m.streetNumber,
       suite: m.suite,
       zipCode: m.zipCode,
       colony: m.colony,
       neighboorhood: m.neighboorhood,
       country: m.country,
       city: m.city,
       town: m.town,
       federalEntity: m.federalEntity,
       addressType: m.addressType
       ]
     }
   }

}
