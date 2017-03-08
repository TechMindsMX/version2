package com.modulus.uno

import spock.lang.Specification
import spock.lang.Unroll
import grails.test.mixin.TestFor

@TestFor(BusinessEntity)
class BusinessEntitySpec extends Specification {

  @Unroll
  void """when we have an business entity with rfc:#rfc, website: #website, type:#type"""(){
   given:"An business entity"
     def businessEntity = new BusinessEntity()
   when:"We assing values"
     businessEntity.rfc = rfc
     businessEntity.website = website
     businessEntity.type = type
   then:"We validate"
     result == businessEntity.validate()
   where:"We have following values"
     rfc              | website                 | type                        || result
     'XXXX010101XXX'  | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   || true
     'XXX010101XXX'   | 'http://iecce.com.mx'   | BusinessEntityType.MORAL    || true
     'XXXX010101XXX'  | null                    | BusinessEntityType.FISICA   || true
     'XXXX010101XXX'  | 'http://iecce.com.mx'   | BusinessEntityType.MORAL    || false
     'AAAAAAAAAAAA'   | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   || false
     'X'              | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   || false
     'XXXX'           | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   || false
     ''               | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   || false
     null             | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   || false
     'XXXX010101XXX'  | 'iecce.com.mx'          | BusinessEntityType.FISICA   || false
     'XXXX010101XXX'  | 'iecce'                 | BusinessEntityType.FISICA   || false
   }

  void "should format client"(){
  given:"An business entity"
    def businessEntity = new BusinessEntity(rfc:'XXXX010101XXX',website:'http://iecce.com.mx',type:BusinessEntityType.FISICA).save(validate:false)
  and:"Names"
    def composeNames = [new ComposeName(value:'Jose Luis', type:NameType.NOMBRE), new ComposeName(value:'De la Cruz', type:NameType.APELLIDO_PATERNO), new ComposeName(value:'Morales', type:NameType.APELLIDO_MATERNO)]
    businessEntity.names = composeNames
  when:"We format our client"
    def result = businessEntity.toString()
  then:"We expect formatted client"
    'Jose Luis De la Cruz Morales' == result
  }

  void "should format provider"(){
  given:"An business entity"
    def businessEntity = new BusinessEntity(rfc:'XXX010101XXX',website:'http://iecce.com.mx',type:BusinessEntityType.MORAL).save(validate:false)
  and:"Names"
    businessEntity.names = [new ComposeName(value:'Imaginn', type:NameType.RAZON_SOCIAL)]
  when:"We format our provider"
    def result = businessEntity.toString()
  then:"We expect formatted provider"
    'Imaginn' == result
  }
}
