package com.modulus.uno

import spock.lang.Specification
import spock.lang.Unroll

class BusinessEntityCommandSpec extends Specification {

  @Unroll
  void """when we have an business entity with rfc:#rfc, website: #website, clientProviderType: clientProviderType, type:#type we expect result:#result"""(){
   given:"An business entity"
     def businessEntity = new BusinessEntityCommand()
   when:"We assing values"
     businessEntity.rfc = rfc
     businessEntity.website = website
     businessEntity.type = type
     businessEntity.clientProviderType = clientProviderType
     businessEntity.name = name
     businessEntity.lastName = lastName
     businessEntity.motherLastName = motherLastName
     businessEntity.businessName = businessName
   then:"We validate"
     result == businessEntity.validate()
   where:"We have following values"
     rfc              | website                 | type                        | clientProviderType | name | lastName | motherLastName | businessName || result
     'XXXX010101XXX'  | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   | LeadType.CLIENTE   | 'JL' | 'DLC'    | 'Morales'      | 'Jmetadata'  || true
     'XXX010101XXX'   | 'http://iecce.com.mx'   | BusinessEntityType.MORAL    | LeadType.CLIENTE   | 'JL' | 'DLC'    | 'Morales'      | 'Jmetadata'  || true
     'XXXX010101XXX'  | null                    | BusinessEntityType.FISICA   | LeadType.CLIENTE   | 'JL' | 'DLC'    | 'Morales'      | 'Jmetadata'  || true
     'XXXX010101XXX'  | 'http://iecce.com.mx'   | BusinessEntityType.MORAL    | LeadType.CLIENTE   | 'JL' | 'DLC'    | 'Morales'      | 'Jmetadata'  || false
     'AAAAAAAAAAAA'   | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   | LeadType.CLIENTE   | 'JL' | 'DLC'    | 'Morales'      | 'Jmetadata'  || false
     'X'              | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   | LeadType.CLIENTE   | 'JL' | 'DLC'    | 'Morales'      | 'Jmetadata'  || false
     'XXXX'           | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   | LeadType.CLIENTE   | 'JL' | 'DLC'    | 'Morales'      | 'Jmetadata'  || false
     ''               | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   | LeadType.CLIENTE   | 'JL' | 'DLC'    | 'Morales'      | 'Jmetadata'  || false
     null             | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   | LeadType.CLIENTE   | 'JL' | 'DLC'    | 'Morales'      | 'Jmetadata'  || false
     'XXXX010101XXX'  | 'iecce.com.mx'          | BusinessEntityType.FISICA   | LeadType.CLIENTE   | 'JL' | 'DLC'    | 'Morales'      | 'Jmetadata'  || false
     'XXXX010101XXX'  | 'iecce'                 | BusinessEntityType.FISICA   | LeadType.CLIENTE   | 'JL' | 'DLC'    | 'Morales'      | 'Jmetadata'  || false
     'XXXX010101XXX'  | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   | LeadType.CLIENTE   | ''   | 'DLC'    | 'Morales'      | 'Jmetadata'  || false
     'XXXX010101XXX'  | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   | LeadType.CLIENTE   | null | 'DLC'    | 'Morales'      | 'Jmetadata'  || false
     'XXXX010101XXX'  | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   | LeadType.CLIENTE   | 'JL' | ''       | 'Morales'      | 'Jmetadata'  || false
     'XXXX010101XXX'  | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   | LeadType.CLIENTE   | 'JL' | null     | 'Morales'      | 'Jmetadata'  || false
     'XXXX010101XXX'  | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   | LeadType.CLIENTE   | 'JL' | 'DLC'    | ''             | 'Jmetadata'  || false
     'XXXX010101XXX'  | 'http://iecce.com.mx'   | BusinessEntityType.FISICA   | LeadType.CLIENTE   | 'JL' | 'DLC'    | null           | 'Jmetadata'  || false
     'XXX010101XXX'   | 'http://iecce.com.mx'   | BusinessEntityType.MORAL    | LeadType.CLIENTE   | null | null     | null           | 'Jmetadata'  || true
     'XXX010101XXX'   | 'http://iecce.com.mx'   | BusinessEntityType.MORAL    | LeadType.CLIENTE   | null | null     | null           | ''           || false
     'XXX010101XXX'   | 'http://iecce.com.mx'   | BusinessEntityType.MORAL    | LeadType.CLIENTE   | null | null     | null           | null         || false
     'XXX010101XXX'   | 'http://iecce.com.mx'   | BusinessEntityType.MORAL    | LeadType.PROVEEDOR | null | null     | null           | 'Jmetadata'  || true
     'XXX010101XXX'   | 'http://iecce.com.mx'   | BusinessEntityType.MORAL    | null               | null | null     | null           | 'Jmetadata'  || false
     'XXX010101XXX'   | 'http://iecce.com.mx'   | BusinessEntityType.MORAL    | LeadType.CLIENTE_PROVEEDOR| null | null     | null           | 'Jmetadata'  || true
  }

}
