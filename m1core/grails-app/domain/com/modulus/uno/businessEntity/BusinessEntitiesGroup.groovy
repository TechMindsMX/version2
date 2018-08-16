package com.modulus.uno.businessEntity

import com.modulus.uno.Company
import com.modulus.uno.BusinessEntity

class BusinessEntitiesGroup {

  Date dateCreated
  Date lastUpdated

  String description
  BusinessEntitiesGroupType type

  static belongsTo = [company:Company]
  static hasMany = [businessEntities:BusinessEntity]

}
